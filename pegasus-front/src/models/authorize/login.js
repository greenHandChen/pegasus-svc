import {getAccessToken} from '../../services/authorize/authorizeService';
import {getLocalAccessToken, removeAccessToken} from "../../utils/authorize";
import {AUTH_LOGOUT_URL} from '../../../config/config';

export default {
  namespace: 'login',

  state: {},

  effects: {
    * getAccessToken({payload}, {call, put}) {
      return yield call(getAccessToken, payload);
    },

    logout() {
      const accessToken = getLocalAccessToken();
      // 清除本地token
      removeAccessToken();
      // 清除服务器token
      window.open(`${AUTH_LOGOUT_URL}?access_token=${accessToken}`,'_self');
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