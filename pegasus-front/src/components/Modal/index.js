import React from "react";
import {Modal} from "antd";

export default class WrapperModal extends React.Component {
  constructor(props) {
    super(props);
    this.props.onRefModal(this);
    this.state = {
      title: null,
      okText: '确定',
      cancelText: '取消',
      keyboard: true,// 支持ESC关闭
      destroyOnClose: true,// 关闭MODAL时销毁子元素
      maskClosable: false,// 点击蒙层不关闭MODAL
      visible: false
    }
  }

  handleOpenModal = (options) => {
    this.setState({
      ...this.state,
      visible: true,
      ...options
    });
  }

  handleCloseModal = () => {
    this.setState({
      ...this.state,
      visible: false
    });
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
      width,
      zIndex,
      modalContent
    } = this.props;

    return (
      <Modal
        {...this.state}
        centered
        title={title ? title : this.state.title}
        zIndex={zIndex}
        width={width}
        onCancel={this.handleCloseCallBack}
        onOk={this.handleOKCallBack}
      >
        {modalContent}
      </Modal>
    )
  }
}