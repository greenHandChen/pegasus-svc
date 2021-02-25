package com.pegasus.activiti.custom.parser;

import com.pegasus.activiti.constant.ActivitiConstants;
import com.pegasus.activiti.custom.listener.UserTaskCompleteListener;
import com.pegasus.common.utils.ApplicationContextUtil;
import org.activiti.bpmn.model.*;
import org.activiti.engine.delegate.BaseTaskListener;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.factory.ActivityBehaviorFactory;
import org.activiti.engine.impl.bpmn.parser.handler.UserTaskParseHandler;
import org.springframework.context.ApplicationContext;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by enHui.Chen on 2019/9/23.
 */
public class CuxUserTaskParseHandler extends UserTaskParseHandler {

    @Override
    protected void executeParse(BpmnParse bpmnParse, UserTask userTask) {
        // 设置默认的behavior
        super.executeParse(bpmnParse, userTask);
        // 设置用户的监听器事件
        this.setUserTaskListenerEvent(userTask);
        // 设置用户多实例
        this.setMultiInstanceLoop(userTask);
        // 设置审批结果为拒绝时,自动结束
        // this.setAutoEnd(bpmnParse, userTask);
    }

    private void setUserTaskListenerEvent(UserTask userTask) {
        ApplicationContext applicationContext = ApplicationContextUtil.getApplicationContext();
        UserTaskCompleteListener userTaskCompleteListener = applicationContext.getBean("userTaskCompleteListener", UserTaskCompleteListener.class);
        // complete事件监听器
        ActivitiListener completeListener = new ActivitiListener();
        completeListener.setEvent(BaseTaskListener.EVENTNAME_COMPLETE);
        completeListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_INSTANCE);
        // completeListener.setOnTransaction("committed");
        completeListener.setInstance(userTaskCompleteListener);
        userTask.getTaskListeners().add(completeListener);
    }

    private void setMultiInstanceLoop(UserTask userTask) {
        // 设置用户任务-多实例
        MultiInstanceLoopCharacteristics multiInstance = new MultiInstanceLoopCharacteristics();
        multiInstance.setInputDataItem(ActivitiConstants.ASSIGNEE_LIST);// 候选组变量
        multiInstance.setElementVariable("assignee");// 设置实例任务的持有人(候选组变量的元素)
        multiInstance.setSequential(false);// 默认并行顺序执行(串行顺序执行请使用多个任务节点)

        // 设置完成条件
        String completionCondition = "${nrOfRejected > 0}";// 默认:一票否决
        multiInstance.setCompletionCondition(completionCondition);

        // 设置任务持有人
        userTask.setAssignee("${assignee}");// 用户任务的审批人(取自候选组变量的元素)
        userTask.setLoopCharacteristics(multiInstance);
    }

    private void setAutoEnd(BpmnParse bpmnParse, UserTask userTask) {
        ActivityBehaviorFactory activityBehaviorFactory = ApplicationContextUtil.getApplicationContext().getBean("cuxBehaviorFactory", ActivityBehaviorFactory.class);

        List<SequenceFlow> outgoings = userTask.getOutgoingFlows();
        if (outgoings == null || outgoings.isEmpty()) {
            return;
        }

        SequenceFlow s0 = outgoings.get(0); // userTask 原始连出线

        FlowElement nextNode = s0.getTargetFlowElement();
        if (nextNode instanceof Task) {
            ExclusiveGateway exclusiveGateway = new ExclusiveGateway();
            exclusiveGateway.setId("eg_auto_" + UUID.randomUUID().toString());
            exclusiveGateway.setParentContainer(userTask.getParentContainer());
            exclusiveGateway
                    .setBehavior(activityBehaviorFactory.createExclusiveGatewayActivityBehavior(exclusiveGateway));

            outgoings.remove(s0);
            SequenceFlow s1 = new SequenceFlow();
            outgoings.add(s1); // s1 替换 s0, (id 保持不变)
            s1.setValues(s0); // s1 copy s0 属性
            s1.setSourceFlowElement(userTask);
            s1.setTargetFlowElement(exclusiveGateway);
            s1.setWaypoints(s0.getWaypoints());
            exclusiveGateway.setIncomingFlows(Arrays.asList(s1));

            SequenceFlow s2 = new SequenceFlow(); // 选择网关默认连出线, 连向 s0 原来的 target
            s2.setId("sf_auto_" + UUID.randomUUID().toString());
            s2.setSourceFlowElement(exclusiveGateway);
            s2.setSourceRef(exclusiveGateway.getId());
            s2.setTargetFlowElement(nextNode);
            s2.setTargetRef(nextNode.getId());
            ((Task) nextNode).getIncomingFlows().remove(s0);
            ((Task) nextNode).getIncomingFlows().add(s2);

            SequenceFlow s3 = new SequenceFlow(); // 从选择网关 连向 endevent
            s3.setId("sf_auto_" + UUID.randomUUID().toString());
            s3.setSourceFlowElement(exclusiveGateway);
            s3.setSourceRef(exclusiveGateway.getId());

            EndEvent endEvent = new EndEvent();
            endEvent.setId("end_auto_" + UUID.randomUUID().toString());

            s3.setTargetFlowElement(endEvent);
            s3.setTargetRef(endEvent.getId());
            s3.setConditionExpression("${outcome =='REJECTED'}");

            exclusiveGateway.setOutgoingFlows(Arrays.asList(s2, s3));

            exclusiveGateway.setDefaultFlow(s2.getId());

            bpmnParse.getSequenceFlows().put(s1.getId(), s1);
            bpmnParse.getSequenceFlows().put(s2.getId(), s2);
            bpmnParse.getSequenceFlows().put(s3.getId(), s3);
            userTask.getParentContainer().addFlowElement(s1);
            userTask.getParentContainer().addFlowElement(s2);
            userTask.getParentContainer().addFlowElement(s3);
        }
    }

    private EndEvent findMainProcessEndEvent(UserTask userTask) {
        Collection<FlowElement> flowElements = userTask.getParentContainer().getFlowElements();
        for (FlowElement flowElement : flowElements) {
            if (flowElement instanceof EndEvent) {
                return (EndEvent) flowElement;
            }
        }
        return null;
    }
}

