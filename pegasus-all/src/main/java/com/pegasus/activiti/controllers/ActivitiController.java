package com.pegasus.activiti.controllers;


import com.pegasus.activiti.service.IActivitiService;
import com.pegasus.activiti.service.IApproveRuleService;
import com.pegasus.activiti.vo.ProcessInstanceVO;
import com.pegasus.activiti.vo.ProcessNodeVO;
import com.pegasus.activiti.vo.TaskVO;
import com.pegasus.common.pagehelper.custom.PageResponse;
import com.pegasus.platform.domain.Employee;
import com.pegasus.platform.service.IEmployeeService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/v1/activiti")
public class ActivitiController {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private IActivitiService activitiService;
    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private IApproveRuleService approveRuleService;

    /**
     * @Author: enHui.Chen
     * @Description: 创建模型
     * @Data 2019/8/20
     */
    @PostMapping("/create/processDefinition")
    public ResponseEntity<String> createProcessDefinition(@RequestBody Map<String, String> maps) throws IOException {
        if (CollectionUtils.isEmpty(maps) || StringUtils.isEmpty(maps.get("name")) || StringUtils.isEmpty(maps.get("key"))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String processDefinition = activitiService.createProcessDefinition(maps);
        return ResponseEntity.ok(processDefinition);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 获取模型
     * @Data 2019/8/20
     */
    @GetMapping("/find/processDefinition")
    public ResponseEntity<List<Model>> findProcessDefinition(@RequestParam(name = "name", required = false) String name) {
        List<Model> list = activitiService.findProcessDefinition(name);
        return ResponseEntity.ok(list);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 删除模型以及相应的编辑资源
     * @Data 2019/8/20
     */
    @DeleteMapping("/delete/processDefinition/{modelId}")
    public ResponseEntity deleteProcessDefinition(@PathVariable(name = "modelId") String modelId) {
        repositoryService.deleteModel(modelId);
        return new ResponseEntity(HttpStatus.OK);
    }


    /**
     * @Author: enHui.Chen
     * @Description: 部署流程定义
     * @Data 2019/8/20
     */
    @PostMapping("/deploy/processDefinition/{modelId}")
    public ResponseEntity<String> deployProcessDefinition(@PathVariable(name = "modelId") String modelId) throws Exception {
        return activitiService.deployProcessDefinition(modelId);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 获取流程定义模版
     * @Data 2019/9/16
     */
    @GetMapping("/find/processTest/template")
    public ResponseEntity<List<ProcessDefinition>> templateProcessDefinition() {
        return ResponseEntity.ok(activitiService.templateProcessDefinition());
    }

    /**
     * @Author: enHui.Chen
     * @Description: 获取流程发起人
     * @Data 2019/9/16
     */
    @GetMapping("/find/processTest/employee")
    public ResponseEntity<List<Employee>> findProcessTestEmployees() {
        return ResponseEntity.ok(employeeService.findProcessTestEmployees());
    }

    /**
     * @Author: enHui.Chen
     * @Description: 启动请假测试工作流
     * @Data 2019/8/20
     */
    @PostMapping("/start/processTest")
    public ResponseEntity startProcessTest(@RequestParam(name = "userId") String userId,
                                           @RequestParam(name = "amount") Long amount,
                                           @RequestParam(name = "processDefinitionId") String processDefinitionId) {
        activitiService.startProcessTest(userId, amount, processDefinitionId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }

    /**
     * @Author: enHui.Chen
     * @Description: 获取当前待办列表/用户
     * @Data 2019/9/18
     */
    @GetMapping("/find/task/currentAssignee")
    public ResponseEntity<List<TaskVO>> findTaskCurrentAssignee() {
        return ResponseEntity.ok(activitiService.findTaskCurrentAssignee());
    }

    /**
     * @Author: enHui.Chen
     * @Description: 获取当前待办列表/管理员
     * @Data 2019/9/18
     */
    @GetMapping("/find/task/adminAssignee")
    public ResponseEntity<List<TaskVO>> findTaskAdminAssignee() {
        return ResponseEntity.ok(activitiService.findTaskAdminAssignee());
    }

    /**
     * @Author: enHui.Chen
     * @Description: 获取审批规则列表
     * @Data 2019/8/20
     */
    @GetMapping("/approve-rule/list")
    public ResponseEntity<Map<String, Object>> findApproverRuleByIsEnabled(@RequestParam(name = "enabled") Boolean enabled) {
        Map<String, Object> layuiTable = new HashMap<>();
        layuiTable.put("data", approveRuleService.findApproverRuleByIsEnabled(enabled == null ? true : enabled));
        layuiTable.put("code", 0);
        return ResponseEntity.ok(layuiTable);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 获取流程监控列表
     * @Data 2019/8/20
     */
    @GetMapping("/find/processInstanceMonitor")
    public ResponseEntity<PageResponse<ProcessInstanceVO>> findProcessInstanceMonitor(Pageable pageRequest) {
        PageResponse<ProcessInstanceVO> processInstanceMonitor = activitiService.findProcessInstanceMonitor(pageRequest);
        return ResponseEntity.ok(processInstanceMonitor);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 获取流程跳转节点列表
     * @Data 2019/8/20
     */
    @GetMapping("/find/processJumpNode/{processDefinitionId}")
    public ResponseEntity<List<ProcessNodeVO>> findProcessJumpNode(@PathVariable(name = "processDefinitionId") String processDefinitionId) {
        List<ProcessNodeVO> processNodeVOS = activitiService.findProcessJumpNode(processDefinitionId);
        return ResponseEntity.ok(processNodeVOS);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 获取流程转交节点列表
     * @Data 2019/8/20
     */
    @GetMapping("/find/deliverTask/{processInstanceId}")
    public ResponseEntity<List<TaskVO>> findDeliverTask(@PathVariable(name = "processInstanceId") String processInstanceId) {
        List<TaskVO> taskVOS = activitiService.findDeliverTask(processInstanceId);
        return ResponseEntity.ok(taskVOS);
    }

     /**
       * @Author: enHui.Chen
       * @Description: 获取加/减签人员列表
       * @Data 2019/11/14
       */
     @GetMapping("/find/counterSignAddOrReduceTask")
     public ResponseEntity<List<TaskVO>> counterSignAddOrReduceTask(@RequestParam(name = "taskDefKey") String taskDefKey,
                                                                    @RequestParam(name = "processInstanceId") String processInstanceId) {
         List<TaskVO> taskVOS = activitiService.findCounterSignAddOrReduceTask(taskDefKey,processInstanceId);
         return ResponseEntity.ok(taskVOS);
     }

    /**
     * @Author: enHui.Chen
     * @Description: 节点任务审批
     * @Data 2019/8/20
     */
    @PostMapping("/complete/task")
    public ResponseEntity completeTask(@RequestParam(name = "taskId") String taskId,
                                       @RequestParam(name = "outcome", required = false) String outcome) {
        activitiService.completeTask(taskId, outcome);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 节点任务跳转
     * @Data 2019/10/25
     */
    @PostMapping("/jump/task")
    public ResponseEntity jumpTask(@RequestParam(name = "taskId") String taskId,
                                   @RequestParam(name = "destinationActivitiId") String destinationActivitiId) {
        activitiService.jumpTask(taskId, destinationActivitiId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 节点任务终止
     * @Data 2019/10/25
     */
    @PostMapping("/delete/task")
    public ResponseEntity deleteTask(@RequestParam(name = "id") String processInstanceId) {
        activitiService.deleteTask(processInstanceId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 节点任务挂起/激活
     * @Data 2019/10/25
     */
    @PostMapping("/suspendOrActive/task")
    public ResponseEntity suspendOrActiveTask(@RequestParam(name = "id") String processInstanceId) {
        activitiService.suspendOrActiveTask(processInstanceId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 节点任务转交(批量)
     * @Data 2019/10/25
     */
    @PostMapping("/deliver/task")
    public ResponseEntity batchDeliverTask(@RequestBody Map<String, String> deliverMap) {
        activitiService.batchDeliverTask(deliverMap);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 节点加签
     * @Data 2019/10/25
     */
    @PostMapping("/counterSignAdd/task/{taskId}/{assignee}")
    public ResponseEntity counterSignAddTask(@PathVariable(name = "taskId") String taskId,
                                             @PathVariable(name = "assignee") String assignee) {
        activitiService.counterSignAddTask(taskId, assignee);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    /**
     * @Author: enHui.Chen
     * @Description: 节点减签
     * @Data 2019/10/25
     */
    @PostMapping("/counterSignReduce/task/{taskId}")
    public ResponseEntity counterSignReduceTask(@PathVariable(name = "taskId") String taskId) {
        activitiService.counterSignReduceTask(taskId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}