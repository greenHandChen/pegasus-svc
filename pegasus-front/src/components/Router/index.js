import React from "react";
import Switch from './Switch';
import Route from './Route';

export default class Content extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  render() {
    const {
      pane
    } = this.props;

    return (
      <Switch
        tabKey={pane.path}
        activeKey
      >
        <Route path={pane.path} component={pane.component}/>
      </Switch>
    );
  }
}