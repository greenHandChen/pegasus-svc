import {Button, Checkbox, Form, Icon, Input} from 'antd';
import React from 'react';
import style from './index.css';

@Form.create({name: 'login'})
export default class Login extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }

  handleSubmit = e => {
    e.preventDefault();
    this.props.form.validateFields((err, values) => {
      if (!err) {
        console.log('Received values of form: ', values);
      }
    });
  };

  render() {
    const {getFieldDecorator} = this.props.form;
    return (
      <div style={
        {
          width: '20%',
          marginTop: '10%',
          marginLeft: '40%',
          border: '1px #efefef solid',
          padding: '45px'
        }
      }
      >
        <Form onSubmit={this.handleSubmit} className={style['login-form']}>
          <Form.Item>
            {getFieldDecorator('username', {
              rules: [{required: true, message: 'Please input your username!'}],
            })(
              <Input
                prefix={<Icon type="user" style={{color: 'rgba(0,0,0,.25)'}}/>}
                placeholder="用户名"
              />,
            )}
          </Form.Item>
          <Form.Item>
            {getFieldDecorator('password', {
              rules: [{required: true, message: 'Please input your Password!'}],
            })(
              <Input
                prefix={<Icon type="lock" style={{color: 'rgba(0,0,0,.25)'}}/>}
                type="password"
                placeholder="密码"
              />,
            )}
          </Form.Item>
          <Form.Item>
            {getFieldDecorator('remember', {
              valuePropName: 'checked',
              initialValue: true,
            })(<Checkbox>记住密码</Checkbox>)}
            <a className={style["login-form-forgot"]} href="">
              忘记密码
            </a>
            <Button type="primary" htmlType="submit" className={style["login-form-button"]}>
              登录
            </Button>
            Or <a href="">注册</a>
          </Form.Item>
        </Form>
      </div>
    );
  }
}