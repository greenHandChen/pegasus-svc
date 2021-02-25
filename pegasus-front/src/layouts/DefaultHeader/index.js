import React from 'react';
import {connect} from "dva";

/**
 * @Author: enHui.Chen
 * @Description: 标题栏
 * @Data 2019/8/21
 */
@connect(({user}) => ({
  currentUser: user.currentUser
}))
export default class DefaultHeader extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
  }

  handleClick = (e) => {
    e.preventDefault();
  }

  handleLogout = () => {
    this.props.dispatch({
      type: 'login/logout'
    });
  }

  render() {
    const {
      currentUser
    } = this.props;
    return (
      <header className="main-header" style={{zIndex: 820}}>
        {/*左上角标题*/}
        <a onClick={this.handleClick} className="logo">
          <span className="logo-lg"><b>Pe</b>gasus</span>
        </a>
        {/*Header Navbar: style can be found in header.less*/}
        <nav className="navbar navbar-static-top">
          {/*Sidebar toggle button*/}
          <a onClick={this.handleClick} className="sidebar-toggle" data-toggle="push-menu" role="button">
            <span className="sr-only">Toggle navigation</span>
          </a>

          <div className="navbar-custom-menu">
            <ul className="nav navbar-nav">
              {/*Messages: style can be found in dropdown.less*/}
              <li className="dropdown messages-menu">
                <a onClick={this.handleClick} className="dropdown-toggle" data-toggle="dropdown">
                  <i className="fa fa-envelope-o"></i>
                  <span className="label label-success">4</span>
                </a>
                <ul className="dropdown-menu">
                  <li className="header">You have 4 messages</li>
                  <li>
                    {/* inner menu: contains the actual data */}
                    <ul className="menu">
                      {/*start message*/}
                      <li>
                        <a onClick={this.handleClick}>
                          <div className="pull-left">
                            <img src={require('../../assets/bigP.jpg')}
                                 className="img-circle"
                                 alt="User Image1"/>
                          </div>
                          <h4>
                            Support Team
                            <small><i className="fa fa-clock-o"></i> 5 mins</small>
                          </h4>
                          <p>Why not buy a new awesome theme?</p>
                        </a>
                      </li>
                      {/*end message*/}
                      <li>
                        <a onClick={this.handleClick}>
                          <div className="pull-left">
                            <img src={require('../../assets/bigP.jpg')}
                                 className="img-circle"
                                 alt="User Image2"/>
                          </div>
                          <h4>
                            AdminLTE Design Team
                            <small><i className="fa fa-clock-o"></i> 2 hours</small>
                          </h4>
                          <p>Why not buy a new awesome theme?</p>
                        </a>
                      </li>
                      <li>
                        <a onClick={this.handleClick}>
                          <div className="pull-left">
                            <img src={require('../../assets/bigP.jpg')}
                                 className="img-circle"
                                 alt="User Image3"/>
                          </div>
                          <h4>
                            Developers
                            <small><i className="fa fa-clock-o"></i> Today</small>
                          </h4>
                          <p>Why not buy a new awesome theme?</p>
                        </a>
                      </li>
                      <li>
                        <a onClick={this.handleClick}>
                          <div className="pull-left">
                            <img src={require('../../assets/bigP.jpg')}
                                 className="img-circle"
                                 alt="User Imag4"/>
                          </div>
                          <h4>
                            Sales Department
                            <small><i className="fa fa-clock-o"></i> Yesterday</small>
                          </h4>
                          <p>Why not buy a new awesome theme?</p>
                        </a>
                      </li>
                      <li>
                        <a onClick={this.handleClick}>
                          <div className="pull-left">
                            <img src={require('../../assets/bigP.jpg')}
                                 className="img-circle"
                                 alt="User Image5"/>
                          </div>
                          <h4>
                            Reviewers
                            <small><i className="fa fa-clock-o"></i> 2 days</small>
                          </h4>
                          <p>Why not buy a new awesome theme?</p>
                        </a>
                      </li>
                    </ul>
                  </li>
                  <li className="footer"><a onClick={this.handleClick}>See All Messages</a></li>
                </ul>
              </li>
              {/*Notifications: style can be found in dropdown.less*/}
              <li className="dropdown notifications-menu">
                <a onClick={this.handleClick} className="dropdown-toggle" data-toggle="dropdown">
                  <i className="fa fa-bell-o"></i>
                  <span className="label label-warning">10</span>
                </a>
                <ul className="dropdown-menu">
                  <li className="header">You have 10 notifications</li>
                  <li>
                    {/*inner menu: contains the actual data */}
                    <ul className="menu">
                      <li>
                        <a onClick={this.handleClick}>
                          <i className="fa fa-users text-aqua"></i> 5 new members joined today
                        </a>
                      </li>
                      <li>
                        <a onClick={this.handleClick}>
                          <i className="fa fa-warning text-yellow"></i> Very long description
                          here that
                          may not fit into the
                          page and may cause design problems
                        </a>
                      </li>
                      <li>
                        <a onClick={this.handleClick}>
                          <i className="fa fa-users text-red"></i> 5 new members joined
                        </a>
                      </li>
                      <li>
                        <a onClick={this.handleClick}>
                          <i className="fa fa-shopping-cart text-green"></i> 25 sales made
                        </a>
                      </li>
                      <li>
                        <a onClick={this.handleClick}>
                          <i className="fa fa-user text-red"></i> You changed your username
                        </a>
                      </li>
                    </ul>
                  </li>
                  <li className="footer"><a onClick={this.handleClick}>View all</a></li>
                </ul>
              </li>
              {/*Tasks: style can be found in dropdown.less*/}
              <li className="dropdown tasks-menu">
                <a onClick={this.handleClick} className="dropdown-toggle" data-toggle="dropdown">
                  <i className="fa fa-flag-o"></i>
                  <span className="label label-danger">9</span>
                </a>
                <ul className="dropdown-menu">
                  <li className="header">You have 9 tasks</li>
                  <li>
                    {/*inner menu: contains the actual data*/}
                    <ul className="menu">
                      <li>{/*Task item*/}
                        <a onClick={this.handleClick}>
                          <h3>
                            Design some buttons
                            <small className="pull-right">20%</small>
                          </h3>
                          <div className="progress xs">
                            <div className="progress-bar progress-bar-aqua" style={{width: "20%"}}
                                 role="progressbar"
                                 aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                              <span className="sr-only">20% Complete</span>
                            </div>
                          </div>
                        </a>
                      </li>
                      {/*end task item*/}
                      <li>
                        <a onClick={this.handleClick}>
                          <h3>
                            Create a nice theme
                            <small className="pull-right">40%</small>
                          </h3>
                          <div className="progress xs">
                            <div className="progress-bar progress-bar-green"
                                 style={{width: "40%"}}
                                 role="progressbar"
                                 aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                              <span className="sr-only">40% Complete</span>
                            </div>
                          </div>
                        </a>
                      </li>
                      <li>
                        <a onClick={this.handleClick}>
                          <h3>
                            Some task I need to do
                            <small className="pull-right">60%</small>
                          </h3>
                          <div className="progress xs">
                            <div className="progress-bar progress-bar-red" style={{width: "60%"}}
                                 role="progressbar"
                                 aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                              <span className="sr-only">60% Complete</span>
                            </div>
                          </div>
                        </a>
                      </li>
                      <li>
                        <a onClick={this.handleClick}>
                          <h3>
                            Make beautiful transitions
                            <small className="pull-right">80%</small>
                          </h3>
                          <div className="progress xs">
                            <div className="progress-bar progress-bar-yellow"
                                 style={{width: "80%"}}
                                 role="progressbar"
                                 aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                              <span className="sr-only">80% Complete</span>
                            </div>
                          </div>
                        </a>
                      </li>
                    </ul>
                  </li>
                  <li className="footer">
                    <a onClick={this.handleClick}>View all tasks</a>
                  </li>
                </ul>
              </li>
              {/*User Account: style can be found in dropdown.less*/}
              <li className="dropdown user user-menu">
                <a onClick={this.handleClick} className="dropdown-toggle" data-toggle="dropdown">
                  <img src={require('../../assets/bigP.jpg')} className="user-image"
                       alt="User Image6"/>
                  <span className="hidden-xs">{currentUser.fullName}</span>
                </a>
                <ul className="dropdown-menu">
                  {/*User image*/}
                  <li className="user-header">
                    <img src={require('../../assets/bigP.jpg')} className="img-circle"
                         alt="User Image7"/>
                    <p>
                      {`${currentUser.fullName} - ${currentUser.title}`}
                      <small>Member since Nov. 2012</small>
                    </p>
                  </li>
                  {/*Menu Body*/}
                  <li className="user-body">
                    <div className="row">
                      <div className="col-xs-4 text-center">
                        <a onClick={this.handleClick}>Followers</a>
                      </div>
                      <div className="col-xs-4 text-center">
                        <a onClick={this.handleClick}>Sales</a>
                      </div>
                      <div className="col-xs-4 text-center">
                        <a onClick={this.handleClick}>Friends</a>
                      </div>
                    </div>
                  </li>
                  {/*Menu Footer*/}
                  <li className="user-footer">
                    <div className="pull-left">
                      <a onClick={this.handleClick} className="btn btn-default btn-flat">个人配置</a>
                    </div>
                    <div className="pull-right">
                      <a onClick={this.handleLogout} className="btn btn-default btn-flat">注销</a>
                    </div>
                  </li>
                </ul>
              </li>
              {/*Control Sidebar Toggle Button*/}
              <li>
                <a onClick={this.handleClick} data-toggle="control-sidebar"><i className="fa fa-gears"></i></a>
              </li>
            </ul>
          </div>
        </nav>
      </header>
    );
  }
}