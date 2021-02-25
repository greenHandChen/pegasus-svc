import {request} from "../../utils/request";
import {ALL} from "../../../config/config";

/**
 * @Author: enHui.Chen
 * @Description: 获取流程模版
 * @Data 2019/8/28
 */
export async function findTaskAdminAssignee(params) {
  return request(`/${ALL}/v1/activiti/find/task/adminAssignee`, {
    method: 'GET',
    query: params
  });
}

/**
 * @Author: enHui.Chen
 * @Description: 审批动作-完成任务
 * @Data 2019/8/28
 */
export async function completeTask(params) {
  return request(`/${ALL}/v1/activiti/complete/task`, {
    method: 'POST',
    query: params
  });
}

