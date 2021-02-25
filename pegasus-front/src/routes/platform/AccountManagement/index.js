import React from "react";
import {connect} from 'dva';
import {Button, Form, Input} from 'antd';
import Table from '../../../components/Table';
import AccountModal from './AccountModal/AccountModal';
import PasswordDrawer from './PasswordDrawer';

@Form.create({name: 'accountForm'})
@connect(({loading, account}) => ({
  accountList: account.accountList,
  accountListLoading: loading.effects['role/findAccountAll']
}))
export default class RoleManagement extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }

  componentDidMount() {
    this.init();
  }

  init = () => {
    const {dispatch} = this.props;
    dispatch({
      type: 'account/findAccountAll'
    });
  }

  onPasswordDrawer = drawer => {
    this.passwordDrawer = drawer;
  }

  onAccountModal = modal => {
    this.accountModal = modal;
  }

  handleCreateAccount = () => {
    this.accountModal.handleOpenModal({
      title: '新建帐号'
    });
  }

  handleEditAccount = row => {
    this.accountModal.handleOpenModal({
      title: '编辑帐号',
      ...row
    });
  }

  handleModifyPassword = row => {
    this.passwordDrawer.handleOpenDrawer({
      title: '修改密码',
      ...row
    });
  }

  render() {
    const {
      accountList,
      accountListLoading
    } = this.props;


    const columns = [
      {
        title: '帐号',
        dataIndex: 'username',
        key: 'username',
      },
      {
        title: '昵称',
        dataIndex: 'nickName',
        key: 'nickName',
        width: 300
      },
      {
        title: '手机号',
        dataIndex: 'phone',
        key: 'phone',
        width: 400
      },
      {
        title: '邮箱',
        dataIndex: 'email',
        key: 'email',
        width: 400
      },
      {
        title: '帐号状态',
        dataIndex: 'isActive',
        key: 'isActive',
        width: 100,
        fixed: 'right',
        render: text => text ? '已激活' : '未激活'
      },
      {
        title: '操作',
        dataIndex: 'action',
        key: 'action',
        width: 300,
        fixed: 'right',
        render: (text, row) => {
          return (<div>
            <a style={{marginLeft: '15px'}} onClick={() => this.handleEditAccount(row)} target={'_blank'}>
              编辑
            </a>
            <a style={{marginLeft: '15px'}} onClick={() => this.handleModifyPassword(row)} target={'_blank'}>
              修改密码
            </a>
          </div>);
        }
      }
    ];
    return (
      <React.Fragment>
        <div style={{margin: '0 10px 20px 10px'}}>
          <Button type="primary" onClick={() => this.handleCreateAccount()}>新建帐号</Button>
          <Input.Search
            placeholder={'请输入帐号'}
            onSearch={value => this.handleCreateAccount()}
            style={{width: 200, marginLeft: '81%'}}
          />
        </div>
        <Table
          scroll={{x: '105%'}}
          loading={accountListLoading}
          columns={columns}
          dataSource={accountList}
        />
        <PasswordDrawer
          onPasswordDrawer={(drawer) => this.onPasswordDrawer(drawer)}
        />
        <AccountModal
          onAccountModal={(modal) => this.onAccountModal(modal)}
        />
      </React.Fragment>
    )
  }
}