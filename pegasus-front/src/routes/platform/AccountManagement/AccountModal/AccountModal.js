import React from "react";
import Modal from '../../../../components/Modal';
import {stringIsEmpty} from '../../../../utils/util';
import Table from '../../../../components/Table';
import Switch from '../../../../components/Switch';
import {Button, Col, Form, Input, Row,} from 'antd';
import {connect} from 'dva';
import DispatchRoleModal from "./DispatchRoleModal";

@Form.create({name: 'accountForm'})
@connect(({loading, account}) => ({
  userRoleList: account.userRoleList,
  userRoleListLoading: loading.effects['account/findRoleListByUserId']
}))
export default class AccountModal extends React.Component {
  constructor(props) {
    super(props);
    this.props.onAccountModal(this);
    this.state = {
      id: null,// 主键
      title: null,// 标题
      switchValue: null,// 是否启用
      selectedRowKeys: []
    };
  }

  onRefModal = modal => {
    this.onRefModal = modal;
  }

  /**
   * @Author: enHui.Chen
   * @Description: 打开MODAL框的回调
   * @Data 2019/10/21
   */
  handleOpenModal = options => {
    this.onRefModal.handleOpenModal();
    if (options && options.id) {
      const {dispatch} = this.props;

      dispatch({
        type: 'account/findRoleListByUserId',
        payload: {
          userId: options.id
        }
      });

      dispatch({
        type: 'account/findAccountByUserId',
        payload: {
          id: options.id
        }
      }).then(res => {
        if (res) {
          this.setState({
            ...options,
            accountForm: res
          });
        }
      });
      return;
    }
    this.setState({
      ...options
    });

  }

  /**
   * @Author: enHui.Chen
   * @Description:
   * @Data 2019/10/18
   */
  onDispatchRoleModal = modal => {
    this.dispatchRoleModal = modal;
  }

  /**
   * @Author: enHui.Chen
   * @Description: 打开分配角色的MODAL
   * @Data 2019/10/21
   */
  handleOpenDispatchRoleModal = () => {
    const id = this.state.id;
    if (!id) {
      // TODO
      return;
    }
    this.dispatchRoleModal.handleOpenModal({
      id
    });
  }

  /**
   * @Author: enHui.Chen
   * @Description: 删除角色
   * @Data 2019/10/21
   */
  handleDeleteDispatchRole = () => {
    const {
      id,
      selectedRowKeys
    } = this.state;
    this.props.dispatch({
      type: 'account/deleteDispatchRole',
      payload: {
        userId: id,
        roleIds: selectedRowKeys
      }
    });
    this.setState({
      ...this.state,
      selectedRowKeys: []
    })
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

  /**
   * @Author: enHui.Chen
   * @Description: 确认保存
   * @Data 2019/10/21
   */
  handleConfirmModal = () => {
    let vRst;
    const {
      id,
      accountForm,
      switchValue
    } = this.state;
    const {
      form,
      dispatch
    } = this.props;
    form.validateFieldsAndScroll(rst => {
      if (!(vRst = rst)) {
        const roleForm = form.getFieldsValue();
        roleForm.id = id;
        roleForm.isActive = switchValue;
        if (stringIsEmpty(roleForm.isActive)) {
          roleForm.isActive = !stringIsEmpty(id) ? accountForm.isActive : false;
        }
        dispatch({
          type: 'account/createOrUpdateAccount',
          payload: roleForm
        });
        this.handleCancelModal();
      }
    })
    return vRst;
  }

  /**
   * @Author: enHui.Chen
   * @Description: 取消
   * @Data 2019/10/21
   */
  handleCancelModal = () => {
    this.setState({
      id: null,
      switchValue: null,
      accountForm: {},
      userRoleList: [],
      selectedRowKeys: []
    });
  }

  /**
   * @Author: enHui.Chen
   * @Description: 获选是否激活的值
   * @Data 2019/10/21
   */
  getSwitchValue = (value) => {
    // eslint-disable-next-line react/no-direct-mutation-state
    this.state.switchValue = value;
  }

  /**
   * @Author: enHui.Chen
   * @Description: 处理复选框
   * @Data 2019/10/21
   */
  handleSelectChange = selectedRowKeys => {
    this.setState({
      ...this.state,
      selectedRowKeys
    });
  };

  render() {
    const {
      id,
      selectedRowKeys,
      switchValue,
      accountForm = {}
    } = this.state;
    const {
      userRoleList,
      userRoleListLoading,
      form: {getFieldDecorator}
    } = this.props;

    const formLayout = {
      labelCol: {span: 6},
      wrapperCol: {span: 16}
    }

    const rowSelection = {
      selectedRowKeys,
      onChange: this.handleSelectChange,
    };


    const tableColumns = [
      {
        title: '角色名称',
        dataIndex: 'roleName',
        key: 'roleName',
      },
      {
        title: '角色编码',
        dataIndex: 'roleCode',
        key: 'roleCode',
        width: 160
      },
      {
        title: '父级角色',
        dataIndex: 'parentRoleName',
        key: 'parentRoleName',
        width: 160
      },
      {
        title: '继承角色',
        dataIndex: 'extendRoleName',
        key: 'extendRoleName',
        width: 160
      },
      {
        title: '是否启用',
        dataIndex: 'isActive',
        key: 'isActive',
        width: 100,
        render: text => text ? '启用' : '禁用'
      }
    ]

    const content = (
      <React.Fragment>
        <Form className='ant-advanced-search-form' {...formLayout}>
          <Row>
            <Col span={11}>
              <Form.Item label="帐号">
                {getFieldDecorator('username', {
                  rules: [
                    {
                      required: true
                    }
                  ],
                  initialValue: id !== null ? accountForm.username : null
                })(<Input/>)}
              </Form.Item>
            </Col>
            <Col span={11}>
              <Form.Item label="昵称">
                {getFieldDecorator('nickName', {
                  rules: [
                    {
                      required: true
                    }
                  ],
                  initialValue: id !== null ? accountForm.nickName : null
                })(<Input/>)}
              </Form.Item>
            </Col>
          </Row>
          {
            id === null ? (
              <Row>
                <Col span={11}>
                  <Form.Item label="密码" hasFeedback>
                    {getFieldDecorator('password', {
                      rules: [
                        {
                          validator: this.handleValidatorPassword

                        },
                        {
                          required: true
                        }
                      ],
                    })(<Input.Password/>)}
                  </Form.Item>
                </Col>
                <Col span={11}>
                  <Form.Item label="确认密码" hasFeedback>
                    {getFieldDecorator('againPassword', {
                      rules: [
                        {
                          validator: this.handleValidatorAgainPassword
                        }
                      ],
                    })(<Input.Password/>)}
                  </Form.Item>
                </Col>
              </Row>
            ) : null
          }
          <Row>
            <Col span={11}>
              <Form.Item label="手机号">
                {getFieldDecorator('phone', {
                  rules: [
                    {
                      required: true
                    }
                  ],
                  initialValue: id !== null ? accountForm.phone : null
                })(<Input/>)}
              </Form.Item>
            </Col>
            <Col span={11}>
              <Form.Item label="邮箱">
                {getFieldDecorator('email', {
                  rules: [
                    {
                      type: 'email',
                      message: '邮箱格式无效!',
                    },
                    {
                      required: true
                    }
                  ],
                  initialValue: id !== null ? accountForm.email : null
                })(<Input/>)}
              </Form.Item>
            </Col>
          </Row>
          {/*<Row>
            <Col span={11}>
              <Form.Item label="出生日期">
                {getFieldDecorator('birthday', {
                  rules: [
                    {
                      required: true
                    }
                  ],
                  initialValue: accountForm.birthday && id !== null ? moment(accountForm.birthday, 'YYYY-MM-DD') : null
                })(<DatePicker placeholder={''}/>)}
              </Form.Item>
            </Col>
            <Col span={11}>
              <Form.Item label="住址">
                {getFieldDecorator('address', {
                  initialValue: id !== null ? accountForm.address : null
                })(<Input/>)}
              </Form.Item>
            </Col>
          </Row>*/}
          <Row>
            <Col span={11}>
              <Form.Item label="激活">
                {getFieldDecorator('isActive')(
                  <Switch
                    getSwitchValue={this.getSwitchValue}
                    defaultChecked={switchValue !== null ? switchValue : (accountForm.isActive !== null && id !== null ? accountForm.isActive : false)}
                  />
                )}
              </Form.Item>
            </Col>
            <Col span={11}>
              <Form.Item wrapperCol={{offset: 10}}>
                <Button onClick={() => this.handleDeleteDispatchRole()}>删除角色</Button>
                <Button type="primary" style={{marginLeft: '25px'}}
                        onClick={() => this.handleOpenDispatchRoleModal()}>分配角色</Button>
              </Form.Item>
            </Col>
          </Row>
        </Form>
        <Table
          columns={tableColumns}
          dataSource={id ? userRoleList : null}
          loading={userRoleListLoading}
          rowSelection={rowSelection}
          pagination={{
            defaultPageSize: 3
          }}
        />
        <DispatchRoleModal
          onDispatchRoleModal={this.onDispatchRoleModal}
        />
      </React.Fragment>
    );

    return (
      <Modal
        width={'800px'}
        title={this.state.title}
        onRefModal={this.onRefModal}
        onCancel={this.handleCancelModal}
        onOk={this.handleConfirmModal}
        modalContent={content}
      />
    )
  }

}

