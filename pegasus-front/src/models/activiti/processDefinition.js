import {
  createProcessDefinition,
  deleteProcessDefinition,
  getProcessDefinitionList,
  deployProcessDefinition
} from '../../services/activiti/processDefinitionService';

export default {
  namespace: 'processDefinition',

  state: {
    processDefinitionList: []
  },

  effects: {
    * getProcessDefinitionList({payload}, {call, put}) {
      const res = yield call(getProcessDefinitionList, payload);
      if (res) {
        yield put({
          type: 'updateState',
          payload: {
            processDefinitionList: res
          }
        });
      }
    },
    * createProcessDefinition({payload}, {call, put}) {
      yield call(createProcessDefinition, payload);
      yield put({
        type: 'getProcessDefinitionList'
      });
    },

    * deleteProcessDefinition({payload}, {call, put}) {
      yield call(deleteProcessDefinition, payload);
      yield put({
        type: 'getProcessDefinitionList'
      });
    },

    * deployProcessDefinition({payload}, {call, put}){
      yield call(deployProcessDefinition, payload);
      yield put({
        type: 'getProcessDefinitionList'
      });
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