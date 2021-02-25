import React from "react";
import {connect} from 'dva';
import moment from 'moment';
import {Divider, Form, Table, Tag} from 'antd';
import BreathTag from '../../../components/BreathTag';

@Form.create({name: 'processDefinition'})
@connect(({processDefinition}) => ({
  processDefinitionList: processDefinition.processDefinitionList
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
      type: 'processDefinition/getProcessDefinitionList',
      payload: {
        name
      }
    });
  }

  render() {
    const {
      processDefinitionList,
    } = this.props;


    const columns = [
      {
        title: '流程名称',
        dataIndex: 'name',
        width: '20%'
      },
      {
        title: '流程编码',
        dataIndex: 'key',
        width: '15%'
      },
      {
        title: '创建时间',
        dataIndex: 'createTime',
        width: '25%',
        render: text => (moment(text).format('YYYY-MM-DD HH:mm:ss'))
      },
      {
        title: '最后修改时间',
        dataIndex: 'lastUpdateTime',
        width: '25%',
        render: text => (moment(text).format('YYYY-MM-DD HH:mm:ss'))
      },
      {
        title: '操作',
        dataIndex: 'id',
        width: '15%',
        render: text => (
          <div style={{textAlign: 'center'}}>
            <a href={`http://localhost:8079/activiti/modeler.html?modelId=${text}`} target={'_blank'}>
              <Tag color="blue">编辑</Tag>
            </a>
            <Divider type="vertical"/>
            <a onClick={() => this.handleDeployProcessDefinition(text)}>
              <BreathTag color='green' content='可发布'/>
            </a>
            <Divider type="vertical"/>
            <a onClick={() => this.handleDeleteProcessDefinition(text)}>
              <Tag color="red">删除</Tag>
            </a>
          </div>
        ),
      },
    ];

    return (
      <React.Fragment>
        <Table
          columns={columns}
          bordered
          dataSource={processDefinitionList}
        />
      </React.Fragment>
    )
  }
}