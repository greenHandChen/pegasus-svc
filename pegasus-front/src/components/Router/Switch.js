/*eslint-disable*/

import React from "react";
import {Switch, withRouter} from 'dva/router';

@withRouter
export default class WrapperSwitch extends Switch {
  constructor(props) {
    super(props);
  }

  shouldComponentUpdate(nextProps, nextState, nextContext) {
    const {
      activePath,
      path,
      activeKey,
      tabKey
    } = nextProps;
    // tab标签切换时,只渲染对应标签内容
    return activeKey === tabKey /*&& activePath.startsWith(path)*/;
  }

}