import {
  getProcessTemplateList,
  getProcessTestEmployeeList,
  startProcessTest
} from '../../services/activiti/processTestService';

export default {
  namespace: 'processTest',

  state: {
    processTemplateList: [],
    processTestEmployeeList: []
  },

  effects: {
    * getProcessTestList({payload}, {call, put}) {
      const processTemplateList = yield call(getProcessTemplateList, payload);
      const processTestEmployeeList = yield call(getProcessTestEmployeeList);
      if (processTemplateList) {
        yield put({
          type: 'updateState',
          payload: {
            processTemplateList: processTemplateList.data,
            processTestEmployeeList
          }
        });
      }
    },
    * startProcessTest({payload}, {call, put}) {
      yield call(startProcessTest, payload);
    }
  },

  reducers: {
    updateState(state, action) {
      return {
        ...state,
        ...action.payload
      }
    }
  }

}