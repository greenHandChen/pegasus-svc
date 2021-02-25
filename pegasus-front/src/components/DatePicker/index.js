import React from "react";
import {DatePicker} from "antd";
import style from "./index.css";

export default class WrapperDatePicker extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }

  render() {
    return (
      <DatePicker
        className={style['wrapped-date-picker']}
        {...this.state}
        {...this.props}
      />
    )
  }
}