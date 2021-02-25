import React from "react";
import {Switch} from 'antd';

export default class WrapperSwitch extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      checked: null
    }
  }

  UNSAFE_componentWillReceiveProps(newProps, newState) {
    this.setState({
      checked: newProps.defaultChecked
    })
  }

  handleSwitchChange = () => {
    let checked = !this.state.checked;
    console.log('checked', checked)
    this.setState({
      checked
    })

    this.props.getSwitchValue(checked);

  }

  render() {
    return (
      <Switch
        value={this.state.checked}
        checked={this.state.checked}
        onChange={this.handleSwitchChange}
      />
    )
  }

}