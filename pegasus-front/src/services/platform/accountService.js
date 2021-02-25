import {request} from "../../utils/request";
import {ALL} from "../../../config/config";

/**
 * @Author: enHui.Chen
 * @Description: 初始化帐号列表
 * @Data 2019/9/27
 */
export async function findAccountAll(params) {
  return request(`/${ALL}/v1/user/findAccountAll`, {
    method: 'GET'
  })
}

/**
 * @Author: enHui.Chen
 * @Description: 创建/编辑帐号
 * @Data 2019/9/27
 */
export async function createOrUpdateAccount(params) {
  return request(`/${ALL}/v1/user/createOrUpdateAccount`, {
    method: 'POST',
    body: params
  })
}

/**
 * @Author: enHui.Chen
 * @Description: 根据UserId查找帐号信息
 * @Data 2019/9/27
 */
export async function findAccountByUserId(params) {
  return request(`/${ALL}/v1/user/findAccountByUserId`, {
    method: 'GET',
    query: params
  })
}

/**
 * @Author: enHui.Chen
 * @Description: 根据UserId查找帐号信息
 * @Data 2019/9/27
 */
export async function findRoleListByUserId(params) {
  return request(`/${ALL}/v1/role/findRoleListByUserId`, {
    method: 'GET',
    query: params
  })
}

/**
 * @Author: enHui.Chen
 * @Description: 帐号角色分配
 * @Data 2019/9/27
 */
export async function dispatchRole(params) {
  return request(`/${ALL}/v1/user/dispatchRole/${params.userId}`, {
    method: 'POST',
    body: params.roleIds
  })
}

/**
 * @Author: enHui.Chen
 * @Description: 帐号角色删除
 * @Data 2019/9/27
 */
export async function deleteDispatchRole(params) {
  return request(`/${ALL}/v1/user/deleteDispatchRole/${params.userId}`, {
    method: 'DELETE',
    body: params.roleIds
  })
}

/**
 * @Author: enHui.Chen
 * @Description: 界面-修改密码
 * @Data 2019/9/27
 */
export async function modifyPassword(params) {
  return request(`/${ALL}/v1/user/modifyPassword`, {
    method: 'PUT',
    body: params
  })
}