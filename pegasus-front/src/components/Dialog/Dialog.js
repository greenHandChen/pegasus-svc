import React from "react";
import {Modal} from "antd";
import ReactDOM from "react-dom";

export default class Dialog extends React.Component {
  constructor(props) {
    super(props);
    // this.props.onRefModal(this);
    this.state = {
      width: '300px',
      title: '提示',
      okText: '确定',
      cancelText: '取消',
      keyboard: true,// 支持ESC关闭
      destroyOnClose: true,// 关闭MODAL时销毁子元素
      maskClosable: false,// 点击蒙层不关闭MODAL
      visible: true
    }
  }


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
    const dialogDiv = this.props.node;
    ReactDOM.unmountComponentAtNode(dialogDiv);
    document.body.removeChild(dialogDiv);
  }

  /**
   * @Author: enHui.Chen
   * @Description: 带回调的关闭
   * @Data 2019/9/30
   */
  handleCloseCallBack = () => {
    const {onCancel} = this.props;
    if (onCancel && typeof onCancel === 'function') {
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
    if (onOk && typeof onOk === 'function') {
      if (onOk()) {
        return;
      }
    }
    this.handleCloseModal();
  }

  render() {
    return (
      <Modal
        {...this.state}
        {...this.props}
        centered
        onCancel={this.handleCloseCallBack}
        onOk={this.handleOKCallBack}
      >
        {this.props.content}
      </Modal>
    )
  }
}