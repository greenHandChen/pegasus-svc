import React from "react";
import {connect} from 'dva';
import {Button, Form, Input, Select} from 'antd';

@Form.create({name: 'processTest'})
@connect(({processTest}) => ({
  processTemplateList: processTest.processTemplateList,
  processTestEmployeeList: processTest.processTestEmployeeList
}))
export default class ProcessTest extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }

  componentDidMount() {
    this.init();
  }

  init = () => {
    this.props.dispatch({
      type: 'processTest/getProcessTestList',
      payload: {
        latest: true,
        size: 99999,
        sort: 'deploymentId',
        order: 'desc',
        $inlinecount: 'allpages'
      }
    });
  }

  handleBtnSubmit = () => {
    const {
      dispatch,
      form
    } = this.props;

    form.validateFieldsAndScroll(err => {
      if (!err) {
        dispatch({
          type: 'processTest/startProcessTest',
          payload: form.getFieldsValue()
        });
      }
    })

  }

  render() {
    const {
      processTemplateList,
      processTestEmployeeList,
      form: {getFieldDecorator}
    } = this.props;

    const processSelectOpt = {
      allowClear: true,
      showSearch: true,
      style: {width: 300}
    }

    const processTemplate =
      <Select
        {...processSelectOpt}
        placeholder={'请选择流程模版'}
      >
        {
          processTemplateList.map(t =>
            <Select.Option
              value={t.id}
              key={t.id}
            >
              {t.name}
            </Select.Option>)
        }
      </Select>;

    const processTestEmployee =
      <Select
        {...processSelectOpt}
        placeholder={'请选择流程测试发起人'}
      >
        {
          processTestEmployeeList.map(e =>
            <Select.Option
              value={e.userId}
              key={e.userId}
            >
              {`${e.fullName}(${e.employeeCode})`}
            </Select.Option>)
        }
      </Select>;

    return (
      <React.Fragment>
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
          <Form.Item label={'流程模版'}>
            {getFieldDecorator('processDefinitionId', {
              rules: [{
                required: true,
                message: '请选择流程模版'
              }]
            })(processTemplate)}
          </Form.Item>
          <Form.Item label={'流程测试发起人'}>
            {getFieldDecorator('userId', {
              rules: [{
                required: true,
                message: '请选择流程测试发起人'
              }]
            })(processTestEmployee)}
          </Form.Item>
          <Form.Item label={'金额'}>
            {getFieldDecorator('amount', {
              rules: [{
                required: true,
                message: '请填写金额'
              }]
            })(<Input style={{width: '300px'}}/>)}
          </Form.Item>
          <Form.Item
            wrapperCol={{
              xs: {span: 24},
              sm: {span: 1, offset: 7}
            }}
          >
            <Button type="primary" onClick={this.handleBtnSubmit}>
              发起测试流程
            </Button>
          </Form.Item>
        </Form>
      </React.Fragment>
    )
  }
}