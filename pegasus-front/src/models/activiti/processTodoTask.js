import {completeTask, findTaskAdminAssignee} from '../../services/activiti/processTodoTaskService';

export default {
  namespace: 'processTodoTask',

  state: {
    todoTaskList: []
  },

  effects: {
    * findTaskAdminAssignee({payload}, {call, put}) {
      const todoTaskList = yield call(findTaskAdminAssignee, payload);
      if (todoTaskList) {
        yield put({
          type: 'updateState',
          payload: {
            todoTaskList
          }
        });
      }
    },
    * completeTask({payload}, {call, put}) {
      yield call(completeTask, payload);
      yield put({type: 'findTaskAdminAssignee'});
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