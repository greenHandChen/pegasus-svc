import {
  counterSignAddTask,
  counterSignReduceTask,
  deliverTask,
  findCounterSignAddOrReduceTask,
  findDeliverTask,
  findProcessInstanceMonitor,
  findProcessJumpNode,
  finishTask,
  jumpTask,
  suspendOrActiveTask
} from '../../services/activiti/processMonitorService';
import {paginationUtil, response} from "../../utils/response";

export default {
  namespace: 'processMonitor',

  state: {
    deliverTaskList: [],
    processNodeList: [],
    processMonitorList: [],
    processMonitorPagination: {},
    counterSignTaskList: []
  },

  effects: {
    * findProcessInstanceMonitor({payload}, {call, put}) {
      const processMonitorList = yield call(findProcessInstanceMonitor, payload);
      if (processMonitorList) {
        yield put({
          type: 'updateState',
          payload: {
            processMonitorList: processMonitorList.content,
            processMonitorPagination: paginationUtil(processMonitorList)
          }
        })
        ;
      }
      return processMonitorList;
    },
    * findProcessJumpNode({payload}, {call, put}) {
      const processNodeList = yield call(findProcessJumpNode, payload);
      if (processNodeList) {
        yield put({
          type: 'updateState',
          payload: {
            processNodeList
          }
        });
      }
      return processNodeList;
    },
    * findDeliverTask({payload}, {call, put}) {
      const deliverTaskList = yield call(findDeliverTask, payload);
      if (deliverTaskList) {
        yield put({
          type: 'updateState',
          payload: {
            deliverTaskList
          }
        });
      }
      return deliverTaskList;
    },
    * findCounterSignAddOrReduceTask({payload}, {call, put}) {
      const counterSignTaskList = yield call(findCounterSignAddOrReduceTask, payload);
      if (counterSignTaskList) {
        yield put({
          type: 'updateState',
          payload: {
            counterSignTaskList
          }
        });
      }
      return counterSignTaskList;
    },
    * jumpTask({payload}, {call, put}) {
      response(yield call(jumpTask, payload));
      yield put({type: 'findProcessInstanceMonitor'});
    },
    * finishTask({payload}, {call, put}) {
      response(yield call(finishTask, payload));
      yield put({type: 'findProcessInstanceMonitor'});
    },
    * suspendOrActiveTask({payload}, {call, put}) {
      response(yield call(suspendOrActiveTask, payload));
      yield put({type: 'findProcessInstanceMonitor'});
    },
    * deliverTask({payload}, {call, put}) {
      response(yield call(deliverTask, payload));
      yield put({
        type: 'findDeliverTask',
        payload: {
          processInstanceId: payload.processInstanceId
        }
      });
      yield put({type: 'findProcessInstanceMonitor'});
    },
    * counterSignAddTask({payload}, {call, put}) {
      response(yield call(counterSignAddTask, payload));
      yield put({type: 'findProcessInstanceMonitor'});
    },
    * counterSignReduceTask({payload}, {call, put}) {
      response(yield call(counterSignReduceTask, payload));
      yield put({type: 'findProcessInstanceMonitor'});

    },
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