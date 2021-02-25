import React from "react";
import {Select} from "antd";

export default class FetchSelect extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      defaultProps: {
        showSearch: true,// 是否开启搜索
        notFoundContent: null,// 无内容
        defaultActiveFirstOption: true,// 是否激活第一个结果
        showArrow: true,// 是否展示箭头
        filterOption: false,// 根据输入过滤
        style: {
          width: 150
        }
      },
      value: undefined,
      data: []
    }
  }

  handleChange = (value) => {
    this.setState({value})
    this.props.onChange(value);
  }

  handleSearch = (value) => {
    const {
      mapping,
      actionType
    } = this.props;
    if (value) {
      window.dvaApp._store.dispatch({
        type: actionType,
        payload: {value}
      }).then(rst => {
        const data = [];
        if (rst) {
          rst.forEach(row =>
            data.push({
              text: row[mapping.text],
              value: row[mapping.value]
            })
          )
        }
        this.setState({data});
      });
    } else {
      this.setState({data: []});
    }
  }

  render() {
    const {
      data,
      value,
      defaultProps
    } = this.state;
    return (
      <Select
        {...defaultProps}
        {...this.props}
        value={value}
        onSearch={this.handleSearch}
        onChange={this.handleChange}
      >
        {data.map(row => {
          return (
            <Select.Option key={row.value} value={row.value}>{row.text}</Select.Option>
          )
        })}
      </Select>
    )
  }
}