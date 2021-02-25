import {
  findRoleAll,
  roleActive,
  roleCopyOrExtend,
  roleCreateOrEdit,
  roleMenuBatchDispatch,
  findAccountAllocatableRole
} from '../../services/platform/roleService';
import {findMenuAll, findMenuByMenuName, findTiledMenuByRoleId} from "../../services/platform/menuService";

export default {
  namespace: 'role',

  state: {
    roleList: [],
    menuTree: [],
    tiledMenuList: [],
    allocatableRoleList: []
  },

  effects: {
    * findRoleAll({payload}, {call, put}) {
      const roleList = yield call(findRoleAll, payload);
      if (roleList) {
        yield put({
          type: 'updateState',
          payload: {roleList}
        });
      }
      return roleList;
    },
    * findMenuAllForRole({payload}, {call, put}) {
      const menuTree = yield call(findMenuAll, payload);
      if (menuTree) {
        yield put({
          type: 'updateState',
          payload: {menuTree}
        });
      }
      return menuTree;
    },
    * findTiledMenuByRoleId({payload}, {call, put}) {
      const tiledMenuList = yield call(findTiledMenuByRoleId, payload);
      if (tiledMenuList) {
        yield put({
          type: 'updateState',
          payload: {tiledMenuList}
        });
      }
      return tiledMenuList;
    },
    * roleMenuBatchDispatch({payload}, {call, put}) {
      yield call(roleMenuBatchDispatch, payload);
      yield put({
        type: 'findTiledMenuByRoleId',
        payload: {
          roleId: payload.roleId
        }
      });
    },
    * findMenuByMenuName({payload}, {call, put}) {
      const menuTree = yield call(findMenuByMenuName, payload);
      yield put({
        type: 'updateState',
        payload: {
          menuTree
        }
      });
    },
    * roleCreateOrEdit({payload}, {call, put}) {
      yield call(roleCreateOrEdit, payload);
      yield put({
        type: 'findRoleAll'
      });
    }
    ,
    * roleCopyOrExtend({payload}, {call, put}) {
      yield call(roleCopyOrExtend, payload);
      yield put({
        type: 'findRoleAll'
      });
    },
    * roleActive({payload}, {call, put}) {
      yield call(roleActive, payload);
      yield put({
        type: 'findRoleAll'
      });
    },
    * findAccountAllocatableRole({payload}, {call, put}){
      const allocatableRoleList = yield call(findAccountAllocatableRole, payload);
      if (allocatableRoleList) {
        yield put({
          type: 'updateState',
          payload: {allocatableRoleList}
        });
      }
      return allocatableRoleList;
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