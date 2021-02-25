import Lov from './Lov';
import ReactDOM from 'react-dom';
import React from "react";

/**
 * @Author: enHui.Chen
 * @Description: 动态渲染Lov
 * @Data 2019/11/1
 */
const renderLov = (props) => {
  // 生成dom节点
  const lov = document.createElement('div');
  document.body.appendChild(lov);

  // 生成react虚拟节点
  ReactDOM.render(
    <Lov
      {...props}
      node={lov}
    />,
    lov
  );
}

export default {
  renderLov(props) {
    renderLov(props);
  }
}