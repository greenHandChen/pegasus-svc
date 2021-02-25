import {lovQuery} from '../../services/platform/commonService';

export default {
  namespace: 'common',

  state: {
    lovList: []
  },

  effects: {
    * lovQuery({payload}, {call, put}) {
      const lovList = yield call(lovQuery, payload);
      return lovList;
    },
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