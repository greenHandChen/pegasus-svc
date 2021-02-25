/**
 * @Author: enHui.Chen
 * @Description: 全局配置
 * @Data 2019/8/28
 */
export const APP_NAME = 'Pegasus';
export const API_HOST = `${process.env.API_HOST}`;
export const _CREATE = 'CREATE';// 创建
export const _UPDATE = 'UPDATE';// 更新
export const _DELETE = 'DELETE';// 删除
/**
 * @Author: enHui.Chen
 * @Description: 后端路由
 * @Data 2020/9/15
 */
export const ALL = 'all';
export const OAUTH = 'oauth';

/**
 * @Author: enHui.Chen
 * @Description: 授权登录相关
 * @Data 2019/9/4
 */
export const CLIENT_ID = `${process.env.CLIENT_ID}`;
export const CLIENT_SECRET = '123456';
export const ACCESS_TOKEN = 'access_token';

export const AUTH_LOGOUT_URL = `${API_HOST}/${OAUTH}/logout`;
// (授权码简化模式)
// 1.访问后台资源,资源受保护,重定向到login页面
// 2.输入用户名密码后,且同意授权,重定向到指定的重定向页面且携带token
// 3.用户携带token即可访问受保护的资源
export const AUTH_URL = `${API_HOST}/${OAUTH}/oauth/authorize?response_type=token&client_id=${CLIENT_ID}`;
// (授权码模式)
export const ACCESS_TOKEN_URL = `${API_HOST}/${OAUTH}/oauth/token?client_id=${CLIENT_ID}&client_secret=${CLIENT_SECRET}&grant_type=authorization_code`;