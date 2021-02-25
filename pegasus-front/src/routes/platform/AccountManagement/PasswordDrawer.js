import React from "react";
import {Form, Input} from "antd";
import Drawer from '../../../components/Drawer'
import {connect} from 'dva';
import {response} from '../../../utils/response';

@Form.create({name: "passwordForm"})
@connect()
export default class PasswordDrawer extends React.Component {
  constructor(props) {
    super(props);
    this.props.onPasswordDrawer(this);
    this.state = {
      id: null,// 当前主键ID
      username: null,//用户名
      title: null
    };
  }

  onRefDrawer = drawer => {
    this.onRefDrawer = drawer;
  }

  /**
   * @Author: enHui.Chen
   * @Description: 打开Drawer
   * @Data 2019/9/28
   */
  handleOpenDrawer = options => {
    this.onRefDrawer.handleOpenDrawer();
    this.openInit(options);
  }

  /**
   * @Author: enHui.Chen
   * @Description: 打开初始化
   * @Data 2019/9/28
   */
  openInit = (options) => {
    this.setState({
      ...options
    });
  }

  /**
   * @Author: enHui.Chen
   * @Description: 确定按钮
   * @Data 2019/9/28
   */
  handleConfirmBtn = () => {
    const {id} = this.state;
    const {
      form,
      dispatch
    } = this.props;
    form.validateFieldsAndScroll(rst => {
      if (!rst) {
        dispatch({
          type: 'account/modifyPassword',
          payload: {
            ...(form.getFieldsValue()),
            id: id
          }
        }).then(res => {
          response(res);
          if (res && !res.code) {
            this.onRefDrawer.handleCloseDrawer();
          }
        })
      }
    });
  }

  /**
   * @Author: enHui.Chen
   * @Description: 二次密码校验
   * @Data 2019/10/18
   */
  handleValidatorAgainPassword = (rule, val, callback) => {
    const {form} = this.props;
    const password = form.getFieldValue('password');
    if (password && password !== val) {
      callback("两次输入的密码不一致!");
    } else {
      callback();
    }
  }

  /**
   * @Author: enHui.Chen
   * @Description: 密码校验
   * @Data 2019/10/18
   */
  handleValidatorPassword = (rule, val, callback) => {
    const {form} = this.props;
    const againPassword = form.getFieldValue('againPassword');
    if (againPassword) {
      form.validateFields(['againPassword'], {force: true});
    }
    callback();
  }

  render() {
    const {
      form: {getFieldDecorator}
    } = this.props;

    const {
      username,
      title
    } = this.state;

    const formLayout = {
      labelCol: {span: 6},
      wrapperCol: {span: 16}
    }

    const content = (

      <Form {...formLayout}>
        <Form.Item label="帐号">
          {getFieldDecorator('username', {
            initialValue: username
          })(<Input disabled/>)}
        </Form.Item>
        <Form.Item label="原密码">
          {getFieldDecorator('oldPassword', {
            rules: [
              {
                required: true
              }
            ]
          })(<Input.Password/>)}
        </Form.Item>
        <Form.Item label="新密码" hasFeedback>
          {getFieldDecorator('password', {
            rules: [
              {
                required: true,
                validator: this.handleValidatorPassword
              }
            ]
          })(<Input.Password/>)}
        </Form.Item>
        <Form.Item label="确认新密码" hasFeedback>
          {getFieldDecorator('againPassword', {
            rules: [
              {
                required: true,
                validator: this.handleValidatorAgainPassword
              }
            ]
          })(<Input.Password/>)}
        </Form.Item>
      </Form>
    );

    return (
      <Drawer
        width={'400px'}
        title={title}
        onRefDrawer={this.onRefDrawer}
        onOkNoClose={this.handleConfirmBtn}
        content={content}
      />
    )
  }

}