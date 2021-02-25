import {getCurrentUser} from '../../services/authorize/userService'

export default {
  namespace: 'user',

  state: {
    currentUser: {}
  },

  effects: {
    * getCurrentUser({payload}, {call, put}) {
      const currentUser = yield call(getCurrentUser);
      if (currentUser) {
        yield put({
          type: 'updateState',
          payload: {
            currentUser
          }
        });
      }
      return currentUser;
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