import React from "react";
import Table from '../../../components/Table';
import Modal from '../../../components/Modal';
import FetchSelect from '../../../components/FetchSelect';
import Dialog from '../../../components/Dialog';
import {connect} from 'dva';
import {Form} from "antd";

@Form.create({name: 'deliverForm'})
@connect(({loading, processMonitor}) => ({
  deliverTaskList: processMonitor.deliverTaskList,
  deliverTaskListLoading: loading.effects['processMonitor/findDeliverTask']
}))
export default class DeliverModal extends React.Component {
  constructor(props) {
    super(props);
    this.props.onRefDeliverModal(this);
    this.state = {
      selectedRowKeys: []
    };
  }

  onRefModal = modal => {
    this.onRefModal = modal;
  }

  /**
   * @Author: enHui.Chen
   * @Description: 打开modal
   * @Data 2019/10/31
   */
  handleOpenModal = options => {
    this.onRefModal.handleOpenModal();
    this.handleDeliverTaskList(options.processInstanceId);
  }

  /**
   * @Author: enHui.Chen
   * @Description: 初始化转交列表
   * @Data 2019/10/31
   */
  handleDeliverTaskList = (processInstanceId) => {
    this.props.dispatch({
      type: 'processMonitor/findDeliverTask',
      payload: {processInstanceId}
    });
    this.setState({
      id: processInstanceId
    })
  }

  /**
   * @Author: enHui.Chen
   * @Description: 批量转交
   * @Data 2019/11/1
   */

  handleBatchDeliverTask = () => {
    const {
      form,
      dispatch
    } = this.props;
    dispatch({
      type: 'processMonitor/deliverTask',
      payload: {
        processInstanceId: this.state.id,
        deliverTargets: form.getFieldsValue()
      }
    });
  }

  /**
   * @Author: enHui.Chen
   * @Description: 确认按钮
   * @Data 2019/11/1
   */
  handleConfirmModal = () => {
    Dialog.renderDialog({
      content: '确定要进行批量转交吗?',
      onOk: () => this.handleBatchDeliverTask()
    });
    return true;
  }

  /**
   * @Author: enHui.Chen
   * @Description: 取消按钮
   * @Data 2019/10/31
   */
  handleCancelModal = () => {
    this.setState({
      id: null,
      selectedRowKeys: []
    });
  }

  /**
   * @Author: enHui.Chen
   * @Description: table的单行点击事件
   * @Data 2019/10/29
   */
  handleTableRowClick = (row, event) => {
    this.setState({
      ...this.state,
      selectedRowKeys: Array.isArray(row) ? row : [row.id]
    })
  }

  render() {
    const {
      title,
      deliverTaskList,
      deliverTaskListLoading,
      form: {getFieldDecorator}
    } = this.props;

    const {
      selectedRowKeys
    } = this.state;

    const tableColumns = [
      {
        title: '流程任务Id',
        dataIndex: 'id',
      },
      {
        title: '审批环节',
        dataIndex: 'name',
        key: 'name',
        width: 150
      },
      {
        title: '任务持有人',
        dataIndex: 'assigneeName',
        key: 'assigneeName',
        width: 150
      },
      {
        title: '转交对象',
        dataIndex: 'deliverTarget',
        key: 'deliverTarget',
        width: 170,
        render: (text, row) =>
          <Form>
            <Form.Item style={{margin: 0}}>
              {getFieldDecorator(`${row.id}-deliverTarget`)(
                <FetchSelect
                  placeholder={'请选择转交对象'}
                  actionType={'employee/findEmployeeByName'}
                  mapping={
                    {
                      text: 'showName',
                      value: 'userId'
                    }
                  }
                />
              )}
            </Form.Item>
          </Form>
      }
    ]

    const rowSelection = {
      type: 'radio',
      selectedRowKeys,
      onChange: row => this.handleTableRowClick(row)
    };

    const content = (
      <React.Fragment>
        <Table
          columns={tableColumns}
          dataSource={deliverTaskList}
          loading={deliverTaskListLoading}
          rowSelection={rowSelection}
          pagination={{
            defaultPageSize: 5
          }}
          onRow={row => {
            return {
              onClick: event => this.handleTableRowClick(row, event)
            }
          }}
        />
      </React.Fragment>
    );

    return (
      <React.Fragment>
        <Modal
          width={'700px'}
          title={title}
          onRefModal={this.onRefModal}
          onCancel={this.handleCancelModal}
          onOk={this.handleConfirmModal}
          modalContent={content}
        />
      </React.Fragment>
    )
  }

}

