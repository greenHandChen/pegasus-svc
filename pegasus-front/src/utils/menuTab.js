import {routerRedux} from "dva/router";

/**
 * @Author: enHui.Chen
 * @Description: 打开TabPane
 * @Data 2019/9/2
 */
export function openTabPane(router, activeKey) {
  if (activeKey) {
    switchTabPane(activeKey);
  } else {
    switchMenuTabPane(router);
  }
}

/**
 * @Author: enHui.Chen
 * @Description: 点击tab切换
 * @Data 2019/9/3
 */
function switchTabPane(activeKey) {

  activeTabPane(activeKey);
  // 点击Tab标签修改路由地址
  window.dvaApp._store.dispatch(routerRedux.push({
    pathname: activeKey
  }));
}

/**
 * @Author: enHui.Chen
 * @Description: 点击menu切换
 * @Data 2019/9/3
 */
function switchMenuTabPane(router) {
  if (!router || typeof (router) === 'undefined' ||
    !router.component || typeof (router.component) === 'undefined') {
    return;
  }

  if (activeTabPane(router.path)) {
    return;
  }

  window.dvaApp._store.dispatch({
    type: 'global/addTabPane',
    payload: {
      router
    }
  });
}

function activeTabPane(activeKey) {
  const tabPane = window.dvaApp._store.getState().global.tabPane;
  for (let i in tabPane) {
    if (tabPane[i].path === activeKey) {
      window.dvaApp._store.dispatch({
        type: 'global/updateState',
        payload: {
          activeKey: activeKey
        }
      });
      return true;
    }
  }
}

/**
 * @Author: enHui.Chen
 * @Description: 移除TabPane
 * @Data 2019/9/2
 */
export function removeTabPane() {

}


/**
 * @Author: enHui.Chen
 * @Description: get route by activeKey
 * @Data 2019/9/3
 */
function getRouteByActiveKey(activeKey) {
  const routerData = window.dvaApp._store.getState().global.routerData;
  return startGetRouteByActiveKey(routerData, activeKey);
}

/**
 * @Author: enHui.Chen
 * @Description:
 * @Data 2019/9/3
 */
function startGetRouteByActiveKey(routerData, activeKey) {
  for (let i = 0; i < routerData.length; i++) {
    if (!routerData[i].isLeaf) {
      return getRouteByActiveKey(routerData[i], activeKey);
    }

    if (routerData[i].path === activeKey) {
      return routerData[i];
    }
  }
}