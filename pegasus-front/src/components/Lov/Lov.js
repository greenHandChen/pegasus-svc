import React from "react";
import {Button, Col, ConfigProvider, Form, Input, Modal, Row} from "antd";
import PageTable from "../PageTable";
import ReactDOM from "react-dom";
import {paginationUtil} from '../../utils/response';
import zhCN from "antd/es/locale/zh_CN";
import {initPageParams} from "../../utils/request";

@Form.create({name: 'lovCommonForm'})
export default class Lov extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      init: true,
      // table数据
      dataSource: [],
      pagination: {},
      columns: [],
      conditions: [],
      // modal数据
      renderType: 'common/render',// TODO暂定
      queryType: 'common/lovQuery',
      title: 'Lov',
      width: '650px',
      okText: '确定',
      cancelText: '取消',
      keyboard: true,// 支持ESC关闭
      destroyOnClose: true,// 关闭MODAL时销毁子元素
      maskClosable: false,// 点击蒙层不关闭MODAL
      visible: true
    }
  }

  componentDidMount() {
    this.renderLov();

  }

  /**
   * @Author: enHui.Chen
   * @Description: 渲染LOV
   * @Data 2019/11/11
   */
  renderLov = () => {
    this.handleLovQuery({
      page: 0,
      size: 10
    });
  }

  /**
   * @Author: enHui.Chen
   * @Description: LOV查询
   * @Data 2019/11/11
   */
  handleLovQuery = (pageRequest) => {
    const {
      code
    } = this.props;
    const {
      queryType
    } = this.state;
    window.dvaApp._store.dispatch({
      type: queryType,
      payload: {
        ...pageRequest,
        code
      }
    }).then(rst => {
      if (rst) {
        const newState = {
          dataSource: rst.content,
          pagination: paginationUtil(rst)
        }

        if (this.state.init) {
          newState.columns = rst.columns;
          newState.conditions = rst.conditions;
        }

        this.setState({
          ...newState
        });
      }
    });
  }

  /**
   * @Author: enHui.Chen
   * @Description: table的change事件,考虑以后可以增加回调
   * @Data 2019/11/13
   */
  handleTableChange = (pageRequest) => {
    // eslint-disable-next-line react/no-direct-mutation-state
    this.state.init = false;
    this.handleLovQuery({
      init: false,
      ...initPageParams(pageRequest)
    });
  }

  /**
   * @Author: enHui.Chen
   * @Description: table的doubleClick事件,考虑以后可以增加回调
   * @Data 2019/11/14
   */
  handleTableRowDoubleClick = (row, event) => {
    this.props.handleGetLovValue(row);
    this.handleCloseModal();
  }
  /**
   * @Author: enHui.Chen
   * @Description: 计算table的查询条件
   * @Data 2019/11/13
   */
  handleFormItemConditions = (conditions) => {
    // 计算批次
    const length = conditions.length, formItemConditions = [];
    let batch = length % 2 === 0 ? length / 2 : parseInt(length / 2, 10) + 1;
    for (let i = 0; i < batch; i++) {
      if (i === 0) {
        formItemConditions.push(
          <Row key={`${i}-${conditions[i * 2].lovId}`}>
            {this.handleFormItemCols(conditions[i * 2], i * 2 !== length - 1 ? conditions[i * 2 + 1] : null)}
            <Col span={4}>
              <Button style={{marginTop: '4px'}} onClick={() => this.handleDeleteDispatchRole()}>查询</Button>
            </Col>
          </Row>
        );
      } else {
        formItemConditions.push(
          <Row key={`${i}-${conditions[i * 2].lovId}`}>
            {this.handleFormItemCols(conditions[i * 2], i * 2 + 1 !== length - 1 ? conditions[i * 2 + 1] : null)}
          </Row>
        );
      }
    }
    return formItemConditions;
  }

  /**
   * @Author: enHui.Chen
   * @Description: 处理lovTable的查询框
   * @Data 2019/11/13
   */
  handleFormItemCols = (condition, nextCondition) => {
    const {
      form: {getFieldDecorator}
    } = this.props;
    return (
      <React.Fragment>
        <Col span={10}>
          <Form.Item label={condition.conditionFieldName}>
            {getFieldDecorator(`${condition.conditionFieldLovCode}`)(
              <Input
                placeholder={`${condition.conditionFieldName}`}
                style={{width: condition.conditionFieldWidth}}
              />
            )}
          </Form.Item>
        </Col>
        {
          nextCondition ? <Col span={10}>
            <Form.Item label={nextCondition.conditionFieldName}>
              {getFieldDecorator(`${nextCondition.conditionFieldLovCode}`)(
                <Input
                  placeholder={`${nextCondition.conditionFieldName}`}
                  style={{width: nextCondition.conditionFieldWidth}}
                />
              )}
            </Form.Item>
          </Col> : null
        }
      </React.Fragment>
    );
  }


  /**
   * @Author: enHui.Chen
   * @Description: lov关闭且移除节点
   * @Data 2019/11/13
   */
  handleCloseModal = () => {
    this.setState({
      visible: false
    }, () => {
      this.removeNode();
    });
  }

  /**
   * @Author: enHui.Chen
   * @Description: 移除节点
   * @Data 2019/11/1
   */
  removeNode = () => {
    const lov = this.props.node;
    ReactDOM.unmountComponentAtNode(lov);// 移除react虚拟节点
    document.body.removeChild(lov);// 移除dom节点
  }

  /**
   * @Author: enHui.Chen
   * @Description: 带回调的关闭
   * @Data 2019/9/30
   */
  handleCloseCallBack = () => {
    const {onCancel} = this.props;

    if (typeof onCancel === 'function') {
      onCancel();
    }

    this.handleCloseModal();
  }

  /**
   * @Author: enHui.Chen
   * @Description: 带回调的确认,回调返回true时,不关闭弹框
   * @Data 2019/9/30
   */
  handleOKCallBack = () => {
    const {onOk} = this.props;

    if (typeof onOk === 'function') {
      if (onOk()) {
        return;
      }
    }

    this.handleCloseModal();
  }

  render() {
    const {
      title,
      zIndex
    } = this.props;

    const {
      pagination,
      dataSource,
      columns,
      conditions
    } = this.state;

    const formLayout = {
      labelCol: {span: 8},
      wrapperCol: {span: 16}
    }

    const mapColumns = columns.map(column => {
      return {
        title: column.columnFieldName,
        dataIndex: column.columnFieldCode,
        key: column.columnFieldCode,
        width: column.columnFieldWidth

      }
    });

    const formItemConditions = this.handleFormItemConditions(conditions);

    return (
      <ConfigProvider locale={zhCN}>
        <Modal
          {...this.state}
          centered
          title={title ? title : this.state.title}
          zIndex={zIndex}
          onCancel={this.handleCloseCallBack}
          onOk={this.handleOKCallBack}
        >
          <Form className='ant-advanced-search-form' {...formLayout}>
            {formItemConditions}
          </Form>
          <PageTable
            size={'small'}
            pagination={pagination}
            columns={mapColumns}
            dataSource={dataSource}
            onChange={this.handleTableChange}
            scroll={{
              y: 200,
              x: 300
            }}
            onRow={row => {
              return {
                onDoubleClick: event => this.handleTableRowDoubleClick(row, event)
              }
            }}
          />
        </Modal>
      </ConfigProvider>
    )
  }
}