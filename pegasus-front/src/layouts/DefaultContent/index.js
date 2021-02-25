import React from 'react';
import {Tabs} from 'antd';
import Content from "../../components/Router";
import {connect} from 'dva';
import {routerRedux} from 'dva/router';
import {openTabPane} from '../../utils/menuTab';

const {TabPane} = Tabs;

/**
 * @Author: enHui.Chen
 * @Description: 内容
 * @Data 2019/8/21
 */
@connect(({global}) => ({
  activeKey: global.activeKey,
  tabPane: global.tabPane
}))
export default class DefaultContent extends React.Component {
  constructor(props) {
    super(props);
    this.props.onRefDefaultContent(this);
    this.state = {}
  }

  onClickTab = activeKey => {
    openTabPane({}, activeKey);
  };

  onEdit = (targetKey, action) => {
    this[action](targetKey);
  };

  remove = targetKey => {
    let {dispatch, tabPane, activeKey} = this.props;
    let lastIndex;
    tabPane.forEach((menu, i) => {
      if (menu.path === targetKey) {
        lastIndex = i - 1;
      }
    });
    const newTabPane = tabPane.filter(menu => menu.path !== targetKey);
    if (newTabPane.length && activeKey === targetKey) {
      activeKey = tabPane[lastIndex >= 0 ? lastIndex : 0].path;
    }
    dispatch({
      type: 'global/updateState',
      payload: {
        activeKey,
        tabPane: newTabPane
      }
    });
    dispatch(routerRedux.push({
      pathname: activeKey
    }));
  };

  render() {
    const {
      activeKey,
      tabPane
    } = this.props;

    return (
      <div className="content-wrapper">
        <section className="content">
          <div style={{backgroundColor: '#ffff', padding: '15px', borderRadius: '3px', height: '100%'}}>
            <Tabs
              hideAdd
              onChange={this.onClickTab}
              activeKey={activeKey}
              type="editable-card"
              onEdit={this.onEdit}
            >
              {
                tabPane.map(pane => (
                  <TabPane tab={pane.name} key={pane.path}>
                    <Content
                      pane={pane}
                      activeKey
                    />
                  </TabPane>
                ))
              }
            </Tabs>
          </div>

        </section>
      </div>
    );
  }
}