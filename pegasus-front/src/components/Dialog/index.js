import Dialog from './Dialog';
import ReactDOM from 'react-dom';
import React from "react";

/**
 * @Author: enHui.Chen
 * @Description: 动态渲染dialog
 * @Data 2019/11/1
 */
const openDialog = (props) => {
  // 生成节点
  const dialogDiv = document.createElement('div');
  document.body.appendChild(dialogDiv);

  ReactDOM.render(
    <Dialog
      {...props}
      node={dialogDiv}
    />,
    dialogDiv
  );
}

export default {
  renderDialog(props) {
    openDialog(props);
  }
}