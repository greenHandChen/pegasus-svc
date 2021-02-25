import {request} from "../../utils/request";
import {ALL} from "../../../config/config";

/**
 * @Author: enHui.Chen
 * @Description: 根据员工姓名模糊搜索员工信息
 * @Data 2019/9/27
 */
export async function findEmployeeByName(params) {
  return request(`/${ALL}/v1/employee/find/employeeByName/${params.value}`, {
    method: 'GET'
  })
}