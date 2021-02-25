import {createOrUpdateMenu, findMenuAll, findMenuById} from '../../services/platform/menuService'

export default {
  namespace: 'menu',

  state: {
    menuTree: [],
    menu: {}
  },

  effects: {
    * findMenuAll({payload}, {call, put}) {
      const menuTree = yield call(findMenuAll);
      if (menuTree) {
        yield put({
          type: 'updateState',
          payload: {
            menuTree
          }
        });
      }
      return menuTree;
    },
    * createOrUpdateMenu({payload}, {call, put}) {
      const menu = yield call(createOrUpdateMenu, payload);
      if (menu) {
        yield put({type: 'findMenuAll'});
      }
    },
    * findMenuById({payload}, {call, put}) {
      const menu = yield call(findMenuById, payload);
      if (menu && menu[0]) {
        yield put({
          type: 'updateState',
          payload: {
            menu: menu[0]
          }
        });
      }
      return menu;
    }
  },

  reducers: {
    updateState(state, action) {
      return {
        ...state,
        ...action.payload
      };
    }
  }
}