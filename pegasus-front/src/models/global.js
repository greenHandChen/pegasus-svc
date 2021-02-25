import {initMenuByRoleId} from "../services/platform/menuService";
import {generateRouterData, getTiledRouterData} from '../utils/router'
import {deepCopy} from '../utils/util'

function initGlobalModelConfig({getTiledRouterData = e => e}) {
  return {
    namespace: 'global',

    state: {
      menu: [],// 后端返回的菜单数据
      tabPane: [],// 已有的tab页
      activeKey: null,// 当前激活的tab页的key
      routerData: []// 渲染菜单的数据
    },

    effects: {
      * initMenuByRoleId({payload}, {call, put}) {
        // 获取平铺后的路由数据,key为path,value为route
        const menu = yield call(initMenuByRoleId);
        if (menu) {
          const routerData = deepCopy(menu);
          // 获取初步的路由数据
          const tiledRouterData = getTiledRouterData(window.dvaApp);
          // 加工后最终的路由数据
          generateRouterData(routerData, tiledRouterData);
          yield put({
            type: 'updateState',
            payload: {
              menu: menu,
              routerData
            }
          });
        }
      }


    },

    reducers: {
      updateState(state, action) {
        return {
          ...state,
          ...action.payload
        };
      },
      addTabPane(state, {payload}) {
        const {router} = payload;
        const {tabPane} = state;
        return {
          ...state,
          tabPane: [...tabPane, router],
          activeKey: router.path
        }
      }
    }
  }
}

export default initGlobalModelConfig({getTiledRouterData: getTiledRouterData})