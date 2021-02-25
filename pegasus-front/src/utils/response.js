import notification from '../components/Notification';

const SUCCESS = "2";
const ERROR = "5";

/**
 * @Author: enHui.Chen
 * @Description: 处理返回的请求
 * @Data 2019/9/4
 */
export function response(res) {
  if (res && res.status) {
    const prefixCode = String(res.status).substring(0, 1);
    switch (prefixCode) {
      case SUCCESS:
        notification.success({
          message: '请求成功'
        });
        break;
      case ERROR:
        notification.warning({
          message: '请求失败',
          description: res.message
        });
        break;
      default :
        notification.info({
          message: '未知的响应'
        });
    }
  } else {
    notification.success({
      message: '请求成功'
    });
  }
  return res;
}

/**
 * @Author: enHui.Chen
 * @Description: 处理分页参数
 * @Data 2019/9/4
 */
export function paginationUtil(data) {
  if (data && typeof data !== 'undefined') {
    return {
      total: data.total,
      pageSize: data.size
    }
  }
  return {};
}