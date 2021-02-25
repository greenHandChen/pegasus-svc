import {findEmployeeByName} from '../../services/platform/employeeService';

export default {
  namespace: 'employee',

  state: {
    employeeList: []
  },

  effects: {
    * findEmployeeByName({payload}, {call, put}) {
      const employeeList = yield call(findEmployeeByName, payload);
      if (employeeList) {
        yield put({
          type: 'updateState',
          payload: {employeeList}
        });
      }
      return employeeList;
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