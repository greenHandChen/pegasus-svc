import React from "react";
import {Button, Drawer, Form} from 'antd';

@Form.create({name: 'menuForm'})
export default class WrapperDrawer extends React.Component {
  constructor(props) {
    super(props);
    this.props.onRefDrawer(this);
    this.state = {
      defaultWidth: '500px',
      // defaultZIndex: 1050,
      visible: false,
      defaultClosable: false,
      defaultDestroyOnClose: true// 关闭Drawer时销毁子元素
    }
  }

  onOk = () => {
    const {onOk, onOkNoClose} = this.props;

    if (typeof onOk === 'function') {
      if (onOk()) {
        return;
      }
    }
    if (typeof onOkNoClose === 'function') {
      onOkNoClose();
      return;
    }

    this.handleCloseDrawer();
  }

  handleOpenDrawer = () => {
    this.setState({
      ...this.state,
      visible: true
    });
  }

  handleCloseDrawer = () => {
    this.setState({
      ...this.state,
      visible: false
    });
  }

  render() {
    const {
      title,
      width,
      zIndex,
      content,
    } = this.props;

    const {
      defaultWidth,
      defaultZIndex,
      visible,
      defaultClosable,
      defaultDestroyOnClose
    } = this.state;

    return (
      <div>
        <Drawer
          title={title}
          visible={visible}
          closable={defaultClosable}
          destroyOnClose={defaultDestroyOnClose}
          zIndex={zIndex ? zIndex : defaultZIndex}
          width={width ? width : defaultWidth}
        >
          {content}
          <div
            style={{
              position: 'absolute',
              bottom: 0,
              width: '100%',
              borderTop: '1px solid #e8e8e8',
              padding: '10px 16px',
              textAlign: 'right',
              left: 0,
              background: '#fff',
              borderRadius: '0 0 4px 4px',
            }}
          >
            <Button
              style={{
                marginRight: 8,
              }}
              onClick={this.handleCloseDrawer}
            >
              取消
            </Button>
            <Button onClick={this.onOk} type="primary">
              确定
            </Button>
          </div>
        </Drawer>
      </div>
    )
  }
}