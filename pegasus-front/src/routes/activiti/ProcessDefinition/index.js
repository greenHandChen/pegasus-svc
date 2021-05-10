import React from "react";
import {connect} from 'dva';
import moment from 'moment';
import {Button, Divider, Form, Input, Tag} from 'antd';
import BreathTag from '../../../components/BreathTag';
import Modal from '../../../components/Modal';
import Table from '../../../components/Table';

@Form.create({name: 'processDefinition'})
@connect(({loading, processDefinition}) => ({
  processDefinitionList: processDefinition.processDefinitionList,
  processDefinitionLoading: loading.effects['processDefinition/getProcessDefinitionList']
}))
export default class ProcessDefinition extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }

  componentDidMount() {
    this.handleGetProcessDefinitionList();
  }

  onRefModal = (modal) => {
    this.onRefModal = modal;
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

  handleCreateProcessDefinition = () => {
    const {
      dispatch,
      form
    } = this.props;
    form.validateFieldsAndScroll(err => {
      if (!err) {
        dispatch({
          type: 'processDefinition/createProcessDefinition',
          payload: form.getFieldsValue()
        });
      }
    });
  }

  handleDeleteProcessDefinition = (modelId) => {
    const {dispatch} = this.props;
    dispatch({
      type: 'processDefinition/deleteProcessDefinition',
      payload: {
        modelId
      }
    });
  }

  handleDeployProcessDefinition = (modelId) => {
    const {dispatch} = this.props;
    dispatch({
      type: 'processDefinition/deployProcessDefinition',
      payload: {
        modelId
      }
    });
  }

  render() {
    const {
      processDefinitionList,
      processDefinitionLoading,
      form: {getFieldDecorator}
    } = this.props;

    const createModal = {
      title: '新建流程',
      zIndex: 8,
      onRefModal: this.onRefModal,
      onOk: this.handleCreateProcessDefinition,
      modalContent: (
        <Form
          labelCol={
            {
              xs: {span: 24},
              sm: {span: 6}
            }
          }
          wrapperCol={
            {
              xs: {span: 24},
              sm: {span: 14, offset: 1}
            }
          }
        >
          <Form.Item label={'流程编码'}>
            {getFieldDecorator('key', {
              rules: [{
                required: true,
                message: '请输入流程编码'
              }]
            })(<Input placeholder={'请输入流程编码'}/>)}
          </Form.Item>
          <Form.Item label={'流程名称'}>
            {getFieldDecorator('name', {
              rules: [{
                required: true,
                message: '请输入流程名称'
              }]
            })(<Input placeholder={'请输入流程名称'}/>)}
          </Form.Item>
        </Form>
      )
    };

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
            <a href={`http://192.168.91.131:8079/all/activiti/modeler.html?modelId=${text}`} target={'_blank'}>
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
        <div style={{margin: '0 10px 20px 10px'}}>
          <Button type="primary" onClick={() => this.onRefModal.handleOpenModal()}>创建流程</Button>
          <Input.Search
            placeholder={'请输入流程名称'}
            onSearch={value => this.handleGetProcessDefinitionList(value)}
            style={{width: 200, marginLeft: '81%'}}
          />
        </div>
        <Table
          loading={processDefinitionLoading}
          columns={columns}
          dataSource={processDefinitionList}
        />
        <Modal {...createModal}/>
      </React.Fragment>
    )
  }
}