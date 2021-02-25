import React from "react";
import {connect} from 'dva';
import Table from '../../../components/Table';
import PageTable from '../../../components/PageTable';
import Modal from '../../../components/Modal';
import Notification from '../../../components/Notification';
import DeliverModal from "./DeliverModal";
import {initPageParams} from '../../../utils/request';
import {Dropdown, Form, Icon, Menu} from 'antd';
import InputLov from "../../../components/InputLov";

@Form.create({name: 'test'})

@connect(({loading, processMonitor}) => ({
  processMonitorList: processMonitor.processMonitorList,
  processMonitorPagination: processMonitor.processMonitorPagination,
  processNodeList: processMonitor.processNodeList,
  counterSignTaskList: processMonitor.counterSignTaskList,
  processMonitorListLoading: loading.effects['processMonitor/findProcessInstanceMonitor'],
  processNodeListLoading: loading.effects['processMonitor/findProcessJumpNode'],
  counterSignTaskListLoading: loading.effects['processMonitor/findCounterSignAddOrReduceTask']
}))
export default class ProcessMonitor extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      taskId: null,
      selectedRowKeys: []
    }
  }

  componentDidMount() {
    this.handleFindProcessInstanceMonitor();
  }

  onJumpTaskModal = modal => {
    this.jumpTaskModal = modal;
  }

  onFinishTaskModal = modal => {
    this.finishTaskModal = modal;
  }

  onRefDeliverModal = modal => {
    this.deliverModal = modal;
  }

  onCounterSignAddModal = modal => {
    this.counterSignAddModal = modal;
  }

  onCounterSignReduceModal = modal => {
    this.counterSignReduceModal = modal;
  }

  /**
   * @Author: enHui.Chen
   * @Description: 初始化流程监控数据
   * @Data 2019/10/29
   */
  handleFindProcessInstanceMonitor = (page) => {
    const {dispatch} = this.props;
    dispatch({
      type: 'processMonitor/findProcessInstanceMonitor',
      payload: {
        ...initPageParams(page)
      }
    });
  }

  /**
   * @Author: enHui.Chen
   * @Description: 打开任务跳转modal
   * @Data 2019/10/29
   */
  handleOpenJumpModal = (row) => {
    this.jumpTaskModal.handleOpenModal();
    this.props.dispatch({
      type: 'processMonitor/findProcessJumpNode',
      payload: {
        processDefinitionId: row.procDefId
      }
    });
    this.setState({
      ...this.state,
      taskId: row.taskId
    })
  }

  /**
   * @Author: enHui.Chen
   * @Description: 任务跳转modal的确认
   * @Data 2019/10/29
   */
  handleJumpNodeConfirmModal = () => {
    const {
      taskId,
      selectedRowKeys
    } = this.state;
    if (!taskId || !selectedRowKeys) {
      Notification.warning({
        message: '操作失败',
        description: '请选择需要跳转的节点'
      });
    }
    this.props.dispatch({
      type: 'processMonitor/jumpTask',
      payload: {
        taskId,
        destinationActivitiId: selectedRowKeys[0]
      }
    });
  }

  /**
   * @Author: enHui.Chen
   * @Description: 打开任务终止modal
   * @Data 2019/10/29
   */
  handleOpenFinishModal = (row) => {
    this.finishTaskModal.handleOpenModal();
    this.setState({
      ...this.state,
      id: row.id
    })
  }

  /**
   * @Author: enHui.Chen
   * @Description: 任务终止modal的确认
   * @Data 2019/10/29
   */
  handleFinishNodeConfirmModal = () => {
    const {
      id
    } = this.state;
    this.props.dispatch({
      type: 'processMonitor/finishTask',
      payload: {id}
    });
  }

  /**
   * @Author: enHui.Chen
   * @Description: 节点任务挂起/激活
   * @Data 2019/10/29
   */
  handleTaskSuspendOrActive = (row) => {
    this.props.dispatch({
      type: 'processMonitor/suspendOrActiveTask',
      payload: {
        id: row.id
      }
    });
  }

  /**
   * @Author: enHui.Chen
   * @Description: 节点任务转交
   * @Data 2019/10/31
   */
  handleOpenTaskDeliverModal = (row) => {
    this.deliverModal.handleOpenModal({
      processInstanceId: row.id
    });
  }

  /**
   * @Author: enHui.Chen
   * @Description: 节点任务加签
   * @Data 2019/11/8
   */
  handleCounterSignAdd = (row) => {
    // eslint-disable-next-line react/no-direct-mutation-state
    this.state.counterSignAddRow = row;
    this.counterSignAddModal.handleOpenModal();
  }

  /**
   * @Author: enHui.Chen
   * @Description: 节点任务加签确认
   * @Data 2019/11/14
   */
  handleCounterSignAddConfirm = () => {
    const {
      dispatch,
      form
    } = this.props;
    form.validateFieldsAndScroll(rst => {
      if (!rst) {
        dispatch({
          type: 'processMonitor/counterSignAddTask',
          payload: {
            taskId: this.state.counterSignAddRow.taskId,
            assignee: form.getFieldsValue().userId
          }
        });
      }
      // eslint-disable-next-line react/no-direct-mutation-state
      this.state.counterSignAddRow = null;
    })
  }

  /**
   * @Author: enHui.Chen
   * @Description: 节点任务减签
   * @Data 2019/11/8
   */
  handleCounterSignReduce = (row) => {
    this.counterSignReduceModal.handleOpenModal();
    const {
      dispatch
    } = this.props;
    dispatch({
      type: 'processMonitor/findCounterSignAddOrReduceTask',
      payload: {
        processInstanceId: row.id,
        taskDefKey: row.taskDefKey
      }
    })
  }

  /**
   * @Author: enHui.Chen
   * @Description: 节点任务减签确认
   * @Data 2019/11/8
   */
  handleCounterSignReduceConfirm = (row) => {
    const {
      dispatch
    } = this.props;
    dispatch({
      type: 'processMonitor/counterSignReduceTask',
      payload: {
        taskId: row.id
      }
    });
    this.counterSignReduceModal.handleCloseModal();
  }

  /**
   * @Author: enHui.Chen
   * @Description: modal的取消
   * @Data 2019/10/29
   */
  handleNodeCloseModal = () => {
    this.setState({
      id: null,
      taskId: null,
      selectedRowKeys: []
    })
  }

  /**
   * @Author: enHui.Chen
   * @Description: table的单行点击事件
   * @Data 2019/10/29
   */
  handleTableRowClick = (row, event) => {
    this.setState({
      ...this.state,
      selectedRowKeys: Array.isArray(row) ? row : [row.nodeId]
    })
  }

  /**
   * @Author: enHui.Chen
   * @Description: 计算流程实例占的行数量,计算的字段
   * @Data 2019/11/7
   */
  handleCalProcessInstanceRow = (processMonitorList, calField) => {
    if (!processMonitorList || processMonitorList === []) {
      return [];
    }
    let startIndex = 0;
    let endIndex = 0;
    let rowCountArray = [];
    processMonitorList.forEach((row, index) => {
      // 最后一条数据
      if (index === processMonitorList.length - 1) {
        const rowCountObj = rowCountArray[rowCountArray.length - 1];
        if (rowCountObj && rowCountObj.endIndex !== index) {
          rowCountArray.push({
            startIndex: index,
            endIndex: index
          })
        }
        return true;
      }

      // 拿当前数据和下一条数据进行对比
      if (row[calField] !== processMonitorList[index + 1][calField]) {
        rowCountArray.push({
          startIndex,
          endIndex
        })
        startIndex = index + 1;
        endIndex = index + 1;
        return true;
      }
      // 如果数据主键一致,自增
      endIndex++;
    });
    return rowCountArray;
  }

  /**
   * @Author: enHui.Chen
   * @Description: 渲染表格行
   * @Data 2019/11/7
   */
  handleRenderTableCell = (rowCountArray, text, index) => {
    let rowCountObj = null;
    rowCountArray.forEach(rowCount => {
      if (index >= rowCount.startIndex && index <= rowCount.endIndex) {
        rowCountObj = rowCount;
        return true;
      }
    });

    // 只存在一行
    if (!rowCountObj) {
      return text;
    }

    const {
      startIndex,
      endIndex
    } = rowCountObj;

    // 起始行
    if (index === startIndex) {
      return {
        children: text,
        props: {
          rowSpan: endIndex - startIndex + 1
        }
      }
    }
    // 中间行
    if (index > startIndex && index <= endIndex) {
      return {
        children: text,
        props: {
          rowSpan: 0
        }
      }
    }
  }

  render() {
    const {
      processMonitorList,
      processMonitorPagination,
      processNodeList,
      processMonitorListLoading,
      processNodeListLoading,
      counterSignTaskList,
      counterSignTaskListLoading,
      form: {getFieldDecorator}
    } = this.props;

    const {
      selectedRowKeys
    } = this.state;

    const rowCountArray = this.handleCalProcessInstanceRow(processMonitorList, 'id') || [];

    const columns = [
      {
        title: '流程实例Id',
        dataIndex: 'id',
        width: 150,
        render: (text, row, index) => {
          return this.handleRenderTableCell(rowCountArray, text, index);
        }
      },
      {
        title: '流程名称',
        dataIndex: 'processName',
        width: 200
      },
      {
        title: '审批环节',
        key: 'name',
        dataIndex: 'name',
        width: 200
      },
      {
        title: '任务持有人',
        key: 'assignees',
        dataIndex: 'assignees'
      },
      {
        title: '任务申请人',
        dataIndex: 'startUserName',
        width: 200
      },
      {
        title: '开始日期',
        dataIndex: 'startTime',
        width: 200
      },
      {
        title: '结束日期',
        dataIndex: 'endTime',
        width: 200
      },
      {
        title: '流程状态',
        dataIndex: 'suspensionState',
        width: 100,
        fixed: 'right',
        render: text => !text ? '已结束' : text === 1 ? '运行中' : '挂起'
      },
      {
        title: '操作',
        key: 'taskId',
        dataIndex: 'taskId',
        width: 245,
        fixed: 'right',
        render: (text, row) => {
          if (text) {
            return (<div>
              <a style={{marginLeft: '10px'}} onClick={() => this.handleOpenTaskDeliverModal(row)} target={'_blank'}>
                转交
              </a>
              <a style={{marginLeft: '10px'}} onClick={() => this.handleOpenJumpModal(row)} target={'_blank'}>
                跳转
              </a>
              <a style={{marginLeft: '10px'}} onClick={() => this.handleTaskSuspendOrActive(row)} target={'_blank'}>
                {row.suspensionState === 1 ? '挂起' : '激活'}
              </a>
              <a style={{marginLeft: '10px', marginRight: '10px'}} onClick={() => this.handleOpenFinishModal(row)}
                 target={'_blank'}>
                终止
              </a>
              <Dropdown overlay={(
                <Menu>
                  <Menu.Item>
                    <a onClick={() => this.handleCounterSignAdd(row)} target={'_blank'}>
                      加签
                    </a>
                  </Menu.Item>
                  <Menu.Item>
                    <a onClick={() => this.handleCounterSignReduce(row)} target={'_blank'}>
                      减签
                    </a>
                  </Menu.Item>
                </Menu>
              )}>
                <a className="ant-dropdown-link" target={'_blank'}>
                  加/减签 <Icon type="down"/>
                </a>
              </Dropdown>
            </div>);
          }
        }
      },
    ];

    // 定义modal的table-start
    const modalContentColumns = [
      {
        title: '节点id',
        dataIndex: 'nodeId',
        width: 300
      },
      {
        title: '节点名称',
        dataIndex: 'nodeName',
        width: 150
      }
    ];

    const rowSelection = {
      type: 'radio',
      selectedRowKeys,
      onChange: row => this.handleTableRowClick(row)
    };

    const modalContent = (
      <Table
        rowKey={row => row.nodeId}
        rowSelection={rowSelection}
        loading={processNodeListLoading}
        columns={modalContentColumns}
        dataSource={processNodeList}
        onRow={row => {
          return {
            onClick: event => this.handleTableRowClick(row, event)
          }
        }}
      />
    );
    // 定义modal的table-end

    return (
      <React.Fragment>
        <PageTable
          rowKey={row => `${row.id}/${row.taskId}`}
          scroll={{x: '105%'}}
          loading={processMonitorListLoading}
          columns={columns}
          dataSource={processMonitorList}
          pagination={processMonitorPagination}
          onChange={(page) => this.handleFindProcessInstanceMonitor(page)}
        />

        {/* 转交1 */}
        <Modal
          width={'600px'}
          title={'选择跳转节点'}
          onRefModal={this.onJumpTaskModal}
          onOk={this.handleJumpNodeConfirmModal}
          onCancel={this.handleNodeCloseModal}
          modalContent={modalContent}
        />

        {/* 终止 */}
        <Modal
          title={'提示'}
          width={'300px'}
          onRefModal={this.onFinishTaskModal}
          onOk={this.handleFinishNodeConfirmModal}
          onCancel={this.handleNodeCloseModal}
          modalContent={'确定要终止流程吗?'}
        />

        {/* 转交2 */}
        <DeliverModal
          title={'请选择转交节点'}
          onRefDeliverModal={this.onRefDeliverModal}
        />

        {/* 加签 */}
        <Modal
          title={'人员加签'}
          onOk={this.handleCounterSignAddConfirm}
          onRefModal={this.onCounterSignAddModal}
          modalContent={
            <Form>
              <Form.Item>
                {getFieldDecorator('userId', {
                  rules: [
                    {
                      required: true,
                      message: '请选择加签人员!',
                    },
                  ]
                })(
                  <InputLov
                    code={'EMPLOYEE_LOV'}
                    mapping={{
                      text: 'fullName',
                      value: 'userId'
                    }}
                  />
                )}
              </Form.Item>
            </Form>
          }
        />

        {/* 减签 */}
        <Modal
          title={'人员减签'}
          width={450}
          onOk={this.handleCounterSignAddConfirm}
          onRefModal={this.onCounterSignReduceModal}
          modalContent={
            <Table
              rowKey={row => row.id}
              loading={counterSignTaskListLoading}
              columns={[
                {
                  title: '节点id',
                  dataIndex: 'id',
                  key: 'id',
                  width: 150
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
                }
              ]}
              dataSource={counterSignTaskList}
              onRow={row => {
                return {
                  onDoubleClick: event => this.handleCounterSignReduceConfirm(row, event)
                }
              }}
            />
          }
        />
      </React.Fragment>
    )
  }
}