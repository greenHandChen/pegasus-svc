import React from "react";
import {Tag} from "antd";
import style from "./index.css";

export default class BreathTag extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }

  render() {
    const {
      color,
      content
    } = this.props;
    return (
      <Tag
        className={style['breath-light']}
        color={color}
      >
        {content}
      </Tag>
    )
  }
}