import React from 'react';

/**
 * @Author: enHui.Chen
 * @Description: 版权
 * @Data 2019/8/21
 */
export default class DefaultFooter extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  handleClick = (e) => {
    e.preventDefault();
  }

  render() {
    return (
      <footer className="main-footer">
        <div className="pull-right hidden-xs">
          <b>Version</b> 0.0.1
        </div>
        <strong>Copyright &copy; 2014-2019 <a onClick={this.handleClick}>Pegasus</a>.</strong> All rights
        reserved.
      </footer>
    );
  }
}