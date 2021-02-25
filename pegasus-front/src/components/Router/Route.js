import {Route} from 'dva/router';

export default class WrapperRoute extends Route {
  shouldComponentUpdate(nextProps, nextState, nextContext) {
    return !!nextState.match;
  }
}