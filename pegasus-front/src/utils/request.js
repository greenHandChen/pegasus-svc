import fetch from "dva/fetch";
import notification from "../components/Notification";
import {ACCESS_TOKEN, API_HOST, AUTH_URL} from "../../config/config";
import {filterNullValObj, generateUrl, getStorage} from "./util";

const DEFAULT_OPTIONS = {
  credentials: 'include',// 同源跨域携带COOKIE
  headers: {
    Pragma: 'no-cache',
    'Cache-Control': 'no-cache',
  }
}

/**
 * @Author: enHui.Chen
 * @Description: 初始化请求数据
 * @Data 2019/11/8
 */
export function request(url, options) {
  let newOptions = {...DEFAULT_OPTIONS, ...options};
  let newUrl = `${API_HOST}${url}`;

  if (newOptions.method === 'POST' ||
    newOptions.method === 'DELETE' ||
    newOptions.method === 'PUT' ||
    newOptions.method === 'PATCH') {
    if (newOptions.body instanceof FormData) {
      newOptions.headers = {
        ...newOptions.headers,
        Accept: 'application/json'
      }
    } else {
      newOptions.body = JSON.stringify(newOptions.body);
      newOptions.headers = {
        ...newOptions.headers,
        Accept: 'application/json',
        'Content-Type': 'application/json; charset=utf-8'
      }
    }
  }

  // 拼接URL参数
  if (newOptions.query) {
    newUrl = generateUrl(newUrl, filterNullValObj(newOptions.query));
  }

  const accessToken = getStorage(ACCESS_TOKEN);

  if (accessToken) {
    newOptions.headers = {
      ...newOptions.headers,
      Authorization: `bearer ${accessToken}`
    }
  }

  return fetch(newUrl, newOptions)
    .then(res => {
      // 401未授权
      if (res.status === 401) {
        // const cacheLocation = encodeURIComponent(window.location.href);
        // return window.location.href = `${AUTH_URL}&redirect_uri=${cacheLocation}`;
        return window.location.href = `${AUTH_URL}`;
      }
      // no-content
      if (res.status === 204) {
        return {};
      }
      return newOptions.responseType === 'text' ? res.text : res.json();
    }).catch(err => {

      if (err) {
        notification.error({
          description: JSON.stringify(err)
        });
      } else {
        notification.error();
      }

    });
}

/**
 * @Author: enHui.Chen
 * @Description: 初始化请求分页数据
 * @Data 2019/11/8
 */
export function initPageParams(page) {
  if (page) {
    return {
      page: page.current - 1,
      size: page.pageSize
    }
  }
  return {
    page: 0,
    size: 10
  }
}