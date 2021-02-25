import React from "react";
import {Table} from "antd";

export default class WrapperTable extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      bordered: true,
      rowKey: 'id',
      size: 'middle'
    }
  }

  render() {
    return (
      <Table
        {...this.state}
        {...this.props}
      >
      </Table>
    )
  }
}