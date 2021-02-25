package com.pegasus.activiti.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.pegasus.activiti.constant.ActivitiConstants;
import com.pegasus.activiti.custom.cmd.MultiNodeCounterSignAddCommand;
import com.pegasus.activiti.custom.cmd.MultiNodeCounterSignReduceCommand;
import com.pegasus.activiti.custom.cmd.MultiNodeJumpCommand;
import com.pegasus.activiti.domain.LeaveApplication;
import com.pegasus.activiti.mapper.ProcessInstanceMapper;
import com.pegasus.activiti.mapper.TaskMapper;
import com.pegasus.activiti.repository.ProcessInstanceDao;
import com.pegasus.activiti.repository.TaskDao;
import com.pegasus.activiti.service.IActivitiService;
import com.pegasus.activiti.service.ILeaveApplicationService;
import com.pegasus.activiti.vo.ProcessInstanceVO;
import com.pegasus.activiti.vo.ProcessNodeVO;
import com.pegasus.activiti.vo.TaskVO;
import com.pegasus.common.exception.CommonException;
import com.pegasus.common.pagehelper.custom.PageResponse;
import com.pegasus.common.pagehelper.custom.utils.PageHelperUtil;
import com.pegasus.platform.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.UserTask;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by enHui.Chen on 2019/8/29.
 */
@Slf4j
@Service
public class ActivitiServiceImpl implements IActivitiService {
    private static final String FIND_PROCESS_DEFINITION = "SELECT * FROM act_re_model where 1 = 1 %s";
    private static final String FIND_PROCESS_DEFINITION_TEMPLATE = "SELECT arm.* FROM " +
            "act_re_model arm, " +
            "act_re_deployment ard " +
            "WHERE " +
            "arm.DEPLOYMENT_ID_ IS NOT NULL " +
            "AND arm.EDITOR_SOURCE_EXTRA_VALUE_ID_ IS NOT NULL " +
            "AND arm.EDITOR_SOURCE_VALUE_ID_ IS NOT NULL " +
            "AND ard.ID_ = arm.DEPLOYMENT_ID_ ";
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ILeaveApplicationService leaveApplicationService;
    @Autowired
    private TaskDao taskDao;
    @Autowired
    private IUserService userService;
    @Autowired
    private ProcessEngineConfigurationImpl processEngineConfiguration;
    @Autowired
    private ProcessInstanceDao processInstanceDao;
    @Autowired
    private ProcessInstanceMapper processInstanceMapper;
    @Autowired
    private TaskMapper taskMapper;

    @Override
    public List<Model> findProcessDefinition(String name) {
        String sql = String.format(FIND_PROCESS_DEFINITION, StringUtils.isNotEmpty(name) ? "and name_ like '%" + name + "%'" : "");
        return repositoryService.createNativeModelQuery().parameter("sql", sql).list();
    }

    @Override
    public List<ProcessDefinition> templateProcessDefinition() {
        return repositoryService.createNativeProcessDefinitionQuery().parameter("sql", FIND_PROCESS_DEFINITION_TEMPLATE).list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createProcessDefinition(Map<String, String> maps) throws UnsupportedEncodingException {
        String name = maps.get("name");
        String key = maps.get("key");
        String description = maps.get("description");
        //初始化一个空模型
        Model model = repositoryService.newModel();

        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        modelNode.put(ModelDataJsonConstants.MODEL_REVISION, ActivitiConstants.REVISION);

        model.setName(name);
        model.setKey(key);
        model.setMetaInfo(modelNode.toString());

        repositoryService.saveModel(model);
        String id = model.getId();

        //完善ModelEditorSource
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace",
                "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);
        repositoryService.addModelEditorSource(id, editorNode.toString().getBytes("utf-8"));
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity deployProcessDefinition(String modelId) throws IOException {
        //获取模型
        Model modelData = repositoryService.getModel(modelId);
        byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());

        if (bytes == null) {
            return ResponseEntity.ok("模型数据为空，请先设计流程并成功保存，再进行发布。");
        }

        JsonNode modelNode = new ObjectMapper().readTree(bytes);

        BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        if (model.getProcesses().size() == 0) {
            return ResponseEntity.ok("数据模型不符要求，请至少设计一条主线流程。");
        }

        // 发布流程
        Deployment deploy = repositoryService.createDeployment().category(modelData.getCategory()).name(modelData.getName())
                .key(modelData.getKey()).addBpmnModel(modelData.getKey() + ".bpmn20.xml", model).deploy();

        // 保存流程定义
        modelData.setDeploymentId(deploy.getId());
        repositoryService.saveModel(modelData);
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startProcessTest(String userId, Long amount, String processDefinitionId) {
        try {
            String realName = userService.findAccountByUserId(Long.valueOf(userId)).getNickName();
            LeaveApplication leaveApplication = new LeaveApplication();
            leaveApplication.setCreatedBy(userId);
            leaveApplication.setCreatedTime(new Date());
            leaveApplication.setStatus("APPROVING");
            leaveApplication.setTitle(realName + "的请假工作流测试");
            leaveApplicationService.insertSelective(leaveApplication);

            // 设置流程变量
            Map<String, Object> variable = new HashMap<>();
            variable.put("amount", amount);
            // 设置任务发起人
            Authentication.setAuthenticatedUserId(userId);

            // 设置businessKey
            String businessKey = "LeaveApplication:" + leaveApplication.getId();

            runtimeService.startProcessInstanceById(processDefinitionId, businessKey, variable);
        } finally {
            Authentication.setAuthenticatedUserId(null);
        }
    }

    @Override
    public List<TaskVO> findTaskCurrentAssignee() {
        return taskDao.findTaskCurrentAssignee();
    }

    @Override
    public List<TaskVO> findTaskAdminAssignee() {
        return taskDao.findTaskAdminAssignee();
    }

    @Override
    public void completeTask(String taskId, String outcome) {
        log.info("待办任务id:{},操作类型:{}", taskId, outcome);
        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
//        runtimeService.setVariable(historicTaskInstance.getProcessInstanceId(), ActivitiConstants.APPROVE_MARK, outcome);

        Map<String, Object> transientVariables = new HashMap<>();
        transientVariables.put(ActivitiConstants.APPROVE_MARK, outcome);
        taskService.complete(taskId, transientVariables);

    }

    @Override
    public void jumpTask(String taskId, String destinationActivitiId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        if (task.isSuspended()) {
            throw new CommonException("挂起的任务无法进行节点跳转!");
        }
        MultiNodeJumpCommand multiNodeJumpCommand = new MultiNodeJumpCommand(taskId, destinationActivitiId);
        processEngineConfiguration.getCommandExecutor().execute(multiNodeJumpCommand);
    }

    @Override
    public PageResponse<ProcessInstanceVO> findProcessInstanceMonitor(Pageable pageRequest) {
        PageResponse<ProcessInstanceVO> pageResponse = PageHelperUtil.doPageQuery(pageRequest,
                () -> processInstanceMapper.findProcessInstanceMonitor());
        List<ProcessInstanceVO> processInstanceVOS = pageResponse.getContent();
        if (CollectionUtils.isEmpty(processInstanceVOS)) {
            return null;
        }
        processInstanceVOS.forEach(processInstanceVO -> {
            String assignees = processInstanceVO.getAssignees();
            if (StringUtils.isEmpty(assignees)) {
                return;
            }
            String[] assigneesArray = processInstanceVO.getAssignees().split(",");
            StringBuilder sb = new StringBuilder();
            Arrays.asList(assigneesArray).forEach(assignee -> {
                if (StringUtils.isEmpty(assignee)) {
                    return;
                }
                if (StringUtils.isEmpty(sb.toString())) {
                    sb.append(assignee);
                } else {
                    sb.append(",").append(assignee);
                }
            });
            processInstanceVO.setAssignees(sb.toString());
        });

        return pageResponse;
    }

    @Override
    public List<ProcessNodeVO> findProcessJumpNode(String processDefinitionId) {
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
        Collection<FlowElement> flowElements = bpmnModel.getMainProcess().getFlowElements();
        List<ProcessNodeVO> processNodeVOS = new ArrayList<>();
        flowElements.forEach(flowElement -> {
            if (flowElement instanceof UserTask) {
                ProcessNodeVO processNodeVO = ProcessNodeVO.builder()
                        .nodeId(flowElement.getId())
                        .nodeName(flowElement.getName())
                        .nodeType("UserTask")
                        .build();
                processNodeVOS.add(processNodeVO);
            }
        });
        return processNodeVOS;
    }

    @Override
    public List<TaskVO> findDeliverTask(String processInstanceId) {
        List<String> processInstanceIds = new ArrayList<>();
        processInstanceIds.add(processInstanceId);
        return taskDao.findMonitorTaskByProcessInstanceIds(processInstanceIds);
    }

    @Override
    public void deleteTask(String processInstanceId) {
        if (StringUtils.isEmpty(processInstanceId)) {
            throw new CommonException("流程实例ID不能为空或流程实体不存在!");
        }
        runtimeService.deleteProcessInstance(processInstanceId, ActivitiConstants.NODE_FINISH);
    }

    @Override
    public void suspendOrActiveTask(String processInstanceId) {
        List<ProcessInstanceVO> processInstances = processInstanceDao.findProcessInstanceMonitorByProcessInstanceId(processInstanceId);
        if (CollectionUtils.isEmpty(processInstances)) {
            throw new CommonException("流程实例不存在:" + processInstanceId);
        }
        ProcessInstanceVO processInstanceVO = processInstances.get(0);
        if (processInstanceVO.getSuspensionState() == null || StringUtils.isNotEmpty(processInstanceVO.getEndTime())) {
            throw new CommonException("流程状态为已结束:" + processInstanceId);
        }

        // 挂起任务
        if (ActivitiConstants.ACTIVE.compareTo(processInstanceVO.getSuspensionState()) == 0) {
            runtimeService.suspendProcessInstanceById(processInstanceId);
        }

        // 激活任务
        if (ActivitiConstants.SUSPEND.compareTo(processInstanceVO.getSuspensionState()) == 0) {
            runtimeService.activateProcessInstanceById(processInstanceId);
        }
    }

    @Override
    public void batchDeliverTask(Map<String, String> deliverMap) {
        if (CollectionUtils.isEmpty(deliverMap)) {
            throw new CommonException("请选择转交对象!");
        }
        deliverMap.forEach((key, val) -> {
            String taskId = key.split("-")[0];
            String targetUserId = val;
            taskService.setAssignee(taskId, targetUserId);
        });
    }

    @Override
    public void counterSignAddTask(String taskId, String assignee) {
        MultiNodeCounterSignAddCommand multiNodeCounterSignAddCommand = new MultiNodeCounterSignAddCommand(taskId, assignee);
        processEngineConfiguration.getCommandExecutor().execute(multiNodeCounterSignAddCommand);
    }

    @Override
    public void counterSignReduceTask(String taskId) {
        MultiNodeCounterSignReduceCommand multiNodeCounterSignReduceCommand = new MultiNodeCounterSignReduceCommand(taskId);
        processEngineConfiguration.getCommandExecutor().execute(multiNodeCounterSignReduceCommand);
    }

    @Override
    public List<TaskVO> findCounterSignAddOrReduceTask(String taskDefKey, String processInstanceId) {
        return taskMapper.findCounterSignAddOrReduceTask(taskDefKey, processInstanceId);
    }
}
