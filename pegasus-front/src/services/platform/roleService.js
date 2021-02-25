import {request} from "../../utils/request";
import {ALL} from "../../../config/config";

/**
 * @Author: enHui.Chen
 * @Description: 初始化角色列表
 * @Data 2019/9/27
 */
export async function findRoleAll(params) {
  return request(`/${ALL}/v1/role/findRoleAll`, {
    method: 'GET'
  })
}

/**
 * @Author: enHui.Chen
 * @Description: 初始化角色列表
 * @Data 2019/9/27
 */
export async function findRoleAllExcludeAdmin(params) {
  return request(`/${ALL}/v1/role/findRoleAll/excludeAdmin`, {
    method: 'GET'
  })
}

/**
 * @Author: enHui.Chen
 * @Description: 批量权限分配
 * @Data 2019/9/26
 */
export async function roleMenuBatchDispatch(params) {
  return request(`/${ALL}/v1/role/roleMenu/batchDispatch/${params.roleId}`, {
    method: 'POST',
    body: params.menus
  })
}

/**
 * @Author: enHui.Chen
 * @Description: 创建/编辑角色
 * @Data 2019/9/26
 */
export async function roleCreateOrEdit(params) {
  return request(`/${ALL}/v1/role/roleCreateOrEdit`, {
    method: 'POST',
    body: params
  })
}

/**
 * @Author: enHui.Chen
 * @Description: 复制/继承角色
 * @Data 2019/9/26
 */
export async function roleCopyOrExtend(params) {
  return request(`/${ALL}/v1/role/roleCopyOrExtend`, {
    method: 'POST',
    body: params
  })
}

/**
 * @Author: enHui.Chen
 * @Description: 启用/禁用角色
 * @Data 2019/9/26
 */
export async function roleActive(params) {
  return request(`/${ALL}/v1/role/roleActive`, {
    method: 'PUT',
    body: params
  })
}

/**
 * @Author: enHui.Chen
 * @Description: 获取账户可分配角色
 * @Data 2019/9/26
 */
export async function findAccountAllocatableRole(params) {
  return request(`/${ALL}/v1/role/findAccountAllocatableRole/${params.userId}`, {
    method: 'GET'
  })
}