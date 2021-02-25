import React from "react";
import {connect} from 'dva';
import {Divider, Form, Tag} from 'antd';
import Table from '../../../components/Table';

@Form.create({name: 'processTodoTask'})
@connect(({loading, processTodoTask}) => ({
  todoTaskList: processTodoTask.todoTaskList,
  todoTaskLoading: loading.effects['processTodoTask/findTaskAdminAssignee']
}))
export default class ProcessDefinition extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }

  componentDidMount() {
    this.handleGetProcessDefinitionList();
  }

  handleGetProcessDefinitionList = (name) => {
    const {dispatch} = this.props;
    dispatch({
      type: 'processTodoTask/findTaskAdminAssignee',
      payload: {
        name
      }
    });
  }

  handleApprove = (taskId) => {
    this.props.dispatch({
      type: 'processTodoTask/completeTask',
      payload: {
        taskId,
        outcome: 'APPROVED'
      }
    })
  }

  handleReject = (taskId) => {
    this.props.dispatch({
      type: 'processTodoTask/completeTask',
      payload: {
        taskId,
        outcome: 'REJECTED'
      }
    })
  }

  render() {
    const {
      todoTaskList,
      todoTaskLoading
      /*
            form: {getFieldDecorator}
      */
    } = this.props;


    const columns = [
      {
        title: '流程实例Id',
        dataIndex: 'processInstanceId',
        width: '10%'
      },
      {
        title: '流程名称',
        dataIndex: 'processName',
        width: '20%'
      },
      {
        title: '审批环节',
        dataIndex: 'name',
        width: '10%'
      },
      {
        title: '任务持有人',
        dataIndex: 'assigneeName',
        width: '10%'
      },
      {
        title: '任务申请人',
        dataIndex: 'startUserName',
        width: '10%'
      },
      {
        title: '创建日期',
        dataIndex: 'createdTime',
        width: '10%'
      },
      {
        title: '优先级',
        dataIndex: 'priority',
        width: '20%'
      },
      {
        title: '操作',
        dataIndex: 'id',
        width: '10%',
        render: (text) => (
          <div style={{textAlign: 'center'}}>
            <a onClick={() => this.handleApprove(text)}>
              <Tag color="blue">同意</Tag>
            </a>
            <Divider type="vertical"/>
            <a onClick={() => this.handleReject(text)}>
              <Tag color="red">拒绝</Tag>
            </a>
          </div>
        ),
      },
    ];

    return (
      <React.Fragment>
        <Table
          loading={todoTaskLoading}
          columns={columns}
          dataSource={todoTaskList}
        />
      </React.Fragment>
    )
  }
}