import {request} from "../../utils/request";
import {ACCESS_TOKEN_URL} from "../../../config/config";

/**
 * @Author: enHui.Chen
 * @Description: 获取token
 * @Data 2019/8/28
 */
export async function getAccessToken(params) {
  return request(`${ACCESS_TOKEN_URL}`, {
    method: 'GET',
    query: params
  });
}