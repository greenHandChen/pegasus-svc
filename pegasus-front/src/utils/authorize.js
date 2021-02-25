import {getStorage, removeStorage} from './util';
import {ACCESS_TOKEN} from '../../config/config';

/**
 * @Author: enHui.Chen
 * @Description: 根据code授权码获取access_token
 * @Data 2019/9/4
 */
export async function getAccessToken(code) {
  await window.dvaApp._store.dispatch({
    type: 'login/getAccessToken',
    payload: {code}
  });
}

/**
 * @Author: enHui.Chen
 * @Description: 移除token
 * @Data 2019/9/5
 */
export function removeAccessToken() {
  removeStorage(ACCESS_TOKEN);
}

/**
 * @Author: enHui.Chen
 * @Description: 获取本地accessToken
 * @Data 2019/9/5
 */
export function getLocalAccessToken() {
  return getStorage(ACCESS_TOKEN);
}