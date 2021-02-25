import React from 'react';
import {connect} from 'dva';
import DefaultHeader from '../layouts/DefaultHeader';
import DefaultLeftMenu from '../layouts/DefaultLeftMenu';
import DefaultContent from '../layouts/DefaultContent';
import DefaultFooter from '../layouts/DefaultFooter';
import DefaultRightMenu from '../layouts/DefaultRightMenu';

@connect(({global, user}) => ({
  menuTree: global.menuTree
}))
export default class IndexPage extends React.Component {
  constructor(props) {
    super(props);
    this.state = {}
  }

  componentDidMount() {
    this.init();
  }

  init = () => {
    const {dispatch} = this.props;
    dispatch({
      type: 'user/getCurrentUser'
    }).then((user) => {
      if (user) {
        dispatch({
          type: 'global/initMenuByRoleId',
          payload: {
            roleId: user.roleId
          }
        })
      }
    });
  }

  onRefDefaultContent = contentChild => {
    this.contentChild = contentChild;
  }

  render() {
    return (
      <div className="wrapper">
        {/* 标题栏 */}
        <DefaultHeader/>

        {/* 左侧菜单栏 */}
        <DefaultLeftMenu/>

        {/* 内容 */}
        <DefaultContent
          onRefDefaultContent={this.onRefDefaultContent}
        />

        {/* 版权 */}
        <DefaultFooter/>

        {/* 右侧菜单栏 */}
        <DefaultRightMenu/>

        <div className="control-sidebar-bg" style={{zIndex: 800}}></div>
      </div>
    );
  }
}

