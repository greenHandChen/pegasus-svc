import {
  createOrUpdateAccount,
  deleteDispatchRole,
  dispatchRole,
  findAccountAll,
  findAccountByUserId,
  findRoleListByUserId,
  modifyPassword
} from '../../services/platform/accountService';
import {response} from "../../utils/response";

export default {
  namespace: 'account',

  state: {
    accountList: [],
    userRoleList: []
  },

  effects: {
    * findAccountAll({payload}, {call, put}) {
      const accountList = yield call(findAccountAll, payload);
      if (accountList) {
        yield put({
          type: 'updateState',
          payload: {accountList}
        });
      }
      return accountList;
    },
    * createOrUpdateAccount({payload}, {call, put}) {
      response(yield call(createOrUpdateAccount, payload));
      yield put({
        type: 'findAccountAll'
      });
    },
    * findAccountByUserId({payload}, {call, put}) {
      return yield call(findAccountByUserId, payload);
    },
    * findRoleListByUserId({payload}, {call, put}) {
      const userRoleList = yield call(findRoleListByUserId, payload);
      if (userRoleList) {
        yield put({
          type: 'updateState',
          payload: {
            userRoleList: userRoleList
          }
        });
      }
      return userRoleList;
    },
    * dispatchRole({payload}, {call, put}) {
      const userRoleList = response(yield call(dispatchRole, payload));
      if (userRoleList) {
        yield put({
          type: 'findRoleListByUserId',
          payload: {
            userId: payload.userId
          }
        });
      }
      return userRoleList;
    },
    * deleteDispatchRole({payload}, {call, put}) {
      response(yield call(deleteDispatchRole, payload));
      yield put({
        type: 'findRoleListByUserId',
        payload: {
          userId: payload.userId
        }
      });
    },
    * modifyPassword({payload}, {call, put}) {
      const formData = new FormData();
      formData.append('oldPassword', payload.oldPassword);
      formData.append('password', payload.password);
      formData.append('id', payload.id);
      return yield call(modifyPassword, formData);
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