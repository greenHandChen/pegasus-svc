import React from "react";
import {Button, Input} from "antd";
import Lov from "../Lov";
import './index.css'; // 修改antd的样式

export default class InputLov extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      value: null
    }
  }

  /**
   * @Author: enHui.Chen
   * @Description: 渲染lov弹框
   * @Data 2019/11/14
   */
  handleRenderLov = () => {
    Lov.renderLov({
      handleGetLovValue: this.handleGetLovValue,
      code: this.props.code
    });
  }


  /**
   * @Author: enHui.Chen
   * @Description: 获取lov的value
   * @Data 2019/11/14
   */
  handleGetLovValue = (value) => {
    const {
      onChange,
      mapping
    } = this.props;
    this.setState({
      value: mapping.text ? value[mapping.text] : value
    })
    onChange(mapping.value ? value[mapping.value] : value);
  }

  render() {

    return (
      <Input
        readOnly
        value={this.state.value}
        className={"lov-span"}
        style={{width: 150}}
        suffix={
          <Button icon="search" onClick={this.handleRenderLov}/>
        }/>
    )
  }
}