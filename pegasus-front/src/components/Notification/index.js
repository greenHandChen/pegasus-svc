import {notification} from 'antd';

/**
 * @Author: enHui.Chen
 * @Description: 全局默认配置
 * @Data 2019/10/22
 */
notification.config({
  placement: 'bottomRight',
  bottom: 50,
  duration: 3,
});

export default {
  /**
   * @Author: enHui.Chen
   * @Description: 操作成功提示
   * @Data 2019/10/22
   */
  success(options = {message: '操作成功', description: ''}) {
    notification.success(options);
  },

  /**
   * @Author: enHui.Chen
   * @Description: 操作信息提示
   * @Data 2019/10/22
   */
  info(options = {message: '操作提示', description: ''}) {
    notification.info(options);
  },

  /**
   * @Author: enHui.Chen
   * @Description: 操作警告提示
   * @Data 2019/10/22
   */
  warning(options = {message: '操作失败', description: ''}) {
    notification.warning(options);
  },

  /**
   * @Author: enHui.Chen
   * @Description: 操作错误提示
   * @Data 2019/10/22
   */
  error(options = {message: '操作错误', description: '出现了未预料到的错误'}) {
    notification.error(options);
  }
}
