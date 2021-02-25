/**
 * @Author: enHui.Chen
 * @Description: 过滤obj的value为空的属性
 * @Data 2019/8/29
 */
export function filterNullValObj(obj) {
  const rst = {};
  if (obj && typeof obj === 'object') {
    Object.keys(obj).forEach(key => {
      let val = obj[key];
      if (val !== '' && val !== null && typeof val !== 'undefined') {
        rst[key] = val;
      }
    })
  }
  return rst;
}

/**
 * @Author: enHui.Chen
 * @Description: 拼接URL参数
 * @Data 2019/8/29
 */
export function generateUrl(oldUrl, obj) {
  let newUrl = oldUrl;
  const params = Object.keys(obj);
  if (obj && typeof obj !== 'undefined' && typeof obj === 'object' && params.length > 0) {
    params.forEach(key => {
      let val = obj[key];
      if (Array.isArray(val)) {
        val.forEach(v => newUrl += generateUrlParam(newUrl, key, v));
        return true;
      }
      newUrl += generateUrlParam(newUrl, key, val);
    });
  }
  return newUrl;
}

function generateUrlParam(url, key, val) {
  return `${url.indexOf('?') !== -1 ? '&' : '?'}${key}=${val}`;
}

/**
 * @Author: enHui.Chen
 * @Description: 获取/设置本地cookie
 * @Data 2019/9/2
 */
export function getStorage(key) {
  return JSON.parse(localStorage.getItem(key));
}

export function setStorage(key, value) {
  localStorage.setItem(key, JSON.stringify(value));
}

export function removeStorage(key) {
  localStorage.removeItem(key);
}


/**
 * @Author: enHui.Chen
 * @Description: 简单的深拷贝(不拷贝方法)
 * @Data 2019/9/2
 */
export function deepCopy(obj) {
  return JSON.parse(JSON.stringify(obj));
}

/**
 * @Author: enHui.Chen
 * @Description: 判断字符串是否为空
 * @Data 2019/10/21
 */
export function stringIsEmpty(val) {
  return val === '' || val === null || typeof val === 'undefined';
}