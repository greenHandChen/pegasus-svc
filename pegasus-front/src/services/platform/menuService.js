import {ALL} from "../../../config/config";

/**
 * @Author: enHui.Chen
 * @Description: 初始化菜单-test
 * @Data 2019/8/30
 */
import {request} from "../../utils/request";

export async function findMenuAll(params) {
  return request(`/${ALL}/v1/menu/findMenuAll`, {
    method: 'GET'
  })
}


/**
 * @Author: enHui.Chen
 * @Description: 角色ID查询菜单
 * @Data 2019/9/26
 */
export async function initMenuByRoleId(params) {
  return request(`/${ALL}/v1/menu/initMenuByRoleId?roleId=1`, {
    method: 'GET',
    query: params
  })
}

/**
 * @Author: enHui.Chen
 * @Description: 启用/禁用菜单
 * @Data 2019/9/26
 */
export async function activeOrForbidMenu(params) {
  return request(`/${ALL}/v1/menu/activeOrForbidMenu/menu`, {
    method: 'POST',
    body: params
  })
}

/**
 * @Author: enHui.Chen
 * @Description: 新建/更新菜单
 * @Data 2019/9/26
 */
export async function createOrUpdateMenu(params) {
  return request(`/${ALL}/v1/menu/createOrUpdate/menu`, {
    method: 'POST',
    body: params
  })
}

/**
 * @Author: enHui.Chen
 * @Description: 根据菜单ID获取菜单详情
 * @Data 2019/9/26
 */
export async function findMenuById(params) {
  return request(`/${ALL}/v1/menu/findMenuById`, {
    method: 'GET',
    query: params
  })
}

/**
 * @Author: enHui.Chen
 * @Description: 根据RoleID获取平铺菜单数据
 * @Data 2019/9/26
 */
export async function findTiledMenuByRoleId(params) {
  return request(`/${ALL}/v1/menu/findTiledMenuByRoleId`, {
    method: 'GET',
    query: params
  })
}

/**
 * @Author: enHui.Chen
 * @Description: 根据菜单名称模糊查询
 * @Data 2019/9/26
 */
export async function findMenuByMenuName(params) {
  return request(`/${ALL}/v1/menu/findMenuByMenuName`, {
    method: 'GET',
    query: params
  })
}