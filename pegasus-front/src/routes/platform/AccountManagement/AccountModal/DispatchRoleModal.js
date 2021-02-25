import React from "react";
import Table from '../../../../components/Table';
import Modal from '../../../../components/Modal';
import {connect} from 'dva';

@connect(({loading, role}) => ({
  allocatableRoleList: role.allocatableRoleList,
  allocatableRoleListLoading: loading.effects['role/findAccountAllocatableRole']
}))
export default class DispatchRoleModal extends React.Component {
  constructor(props) {
    super(props);
    this.props.onDispatchRoleModal(this);
    this.state = {
      id: null,
      selectedRowKeys: []
    };
  }

  onRefModal = modal => {
    this.onRefModal = modal;
  }

  handleOpenModal = options => {
    this.onRefModal.handleOpenModal();
    const {dispatch} = this.props;

    dispatch({
      type: 'role/findAccountAllocatableRole',
      payload: {
        userId: options.id
      }
    });

    this.setState({
      id: options.id
    })
  }

  handleCancelModal = () => {
    this.setState({
      id: null,
      selectedRowKeys: []
    });
  }

  handleConfirmModal = () => {
    const {
      dispatch
    } = this.props;

    const {
      id,
      selectedRowKeys
    } = this.state;

    dispatch({
      type: 'account/dispatchRole',
      payload: {
        userId: id,
        roleIds: selectedRowKeys
      }
    });

    this.handleCancelModal();
  }

  handleSelectChange = selectedRowKeys => {
    this.setState({
      ...this.state,
      selectedRowKeys
    });
  };

  render() {
    const {
      allocatableRoleList,
      allocatableRoleListLoading
    } = this.props;

    const {
      selectedRowKeys
    } = this.state;

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

    const rowSelection = {
      selectedRowKeys,
      onChange: this.handleSelectChange,
    };

    const content = (
      <React.Fragment>
        <Table
          columns={tableColumns}
          dataSource={allocatableRoleList}
          loading={allocatableRoleListLoading}
          rowSelection={rowSelection}
          pagination={{
            defaultPageSize: 5
          }}
        />
      </React.Fragment>
    );

    return (
      <Modal
        width={'800px'}
        title={'分配角色'}
        onRefModal={this.onRefModal}
        onCancel={this.handleCancelModal}
        onOk={this.handleConfirmModal}
        modalContent={content}
      >

      </Modal>
    )
  }

}

