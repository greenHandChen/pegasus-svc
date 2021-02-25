import React from "react";
import {Table} from "antd";

export default class PageTable extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      bordered: true,
      rowKey: 'id',
      size: 'middle'
    }
  }

  render() {
    const {
      pagination
    } = this.props;
    return (
      <Table
        {...this.state}
        {...this.props}
        pagination={
          {
            showSizeChanger: true,
            showQuickJumper: true,
            ...pagination
          }
        }
      >
      </Table>
    )
  }
}