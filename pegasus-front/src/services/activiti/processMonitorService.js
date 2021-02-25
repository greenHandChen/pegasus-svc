import {request} from "../../utils/request";
import {ALL} from "../../../config/config";

/**
 * @Author: enHui.Chen
 * @Description: 获取流程监控
 * @Data 2019/8/28
 */
export async function findProcessInstanceMonitor(params) {
  return request(`/${ALL}/v1/activiti/find/processInstanceMonitor`, {
    method: 'GET',
    query: params
  });
}

/**
 * @Author: enHui.Chen
 * @Description: 获取流程跳转节点列表
 * @Data 2019/8/28
 */
export async function findProcessJumpNode(params) {
  return request(`/${ALL}/v1/activiti/find/processJumpNode/${params.processDefinitionId}`, {
    method: 'GET'
  });
}

/**
 * @Author: enHui.Chen
 * @Description: 获取流程转交节点列表
 * @Data 2019/8/28
 */
export async function findDeliverTask(params) {
  return request(`/${ALL}/v1/activiti/find/deliverTask/${params.processInstanceId}`, {
    method: 'GET'
  });
}

/**
 * @Author: enHui.Chen
 * @Description: 获取流程转交节点列表
 * @Data 2019/8/28
 */
export async function findCounterSignAddOrReduceTask(params) {
  return request(`/${ALL}/v1/activiti/find/counterSignAddOrReduceTask`, {
    method: 'GET',
    query: params
  });
}

/**
 * @Author: enHui.Chen
 * @Description: 节点任务跳转
 * @Data 2019/8/28
 */
export async function jumpTask(params) {
  return request(`/${ALL}/v1/activiti/jump/task`, {
    method: 'POST',
    query: params
  });
}

/**
 * @Author: enHui.Chen
 * @Description: 节点任务终止
 * @Data 2019/8/28
 */
export async function finishTask(params) {
  return request(`/${ALL}/v1/activiti/delete/task`, {
    method: 'POST',
    query: params
  });
}

/**
 * @Author: enHui.Chen
 * @Description: 节点任务挂起/激活
 * @Data 2019/8/28
 */
export async function suspendOrActiveTask(params) {
  return request(`/${ALL}/v1/activiti/suspendOrActive/task`, {
    method: 'POST',
    query: params
  });
}

/**
 * @Author: enHui.Chen
 * @Description: 节点任务挂起/激活
 * @Data 2019/8/28
 */
export async function deliverTask(params) {
  return request(`/${ALL}/v1/activiti/deliver/task`, {
    method: 'POST',
    body: params.deliverTargets
  });
}

/**
 * @Author: enHui.Chen
 * @Description: 节点任务加签
 * @Data 2019/8/28
 */
export async function counterSignAddTask(params) {
  return request(`/${ALL}/v1/activiti/counterSignAdd/task/${params.taskId}/${params.assignee}`, {
    method: 'POST'
  });
}

/**
 * @Author: enHui.Chen
 * @Description: 节点任务减签
 * @Data 2019/8/28
 */
export async function counterSignReduceTask(params) {
  return request(`/${ALL}/v1/activiti/counterSignReduce/task/${params.taskId}`, {
    method: 'POST'
  });
}
