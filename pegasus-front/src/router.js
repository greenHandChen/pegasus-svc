import React from 'react';
import {Route, Router, Switch} from 'dva/router';
import IndexPage from './routes/IndexPage';
import Login from './routes/authorize/Login';
import AuthenticatedRoute from './components/AuthenticatedRoute';
import {ConfigProvider} from 'antd';
import zhCN from 'antd/es/locale/zh_CN';

function RouterConfig({history}) {
  return (
    <ConfigProvider locale={zhCN}>
      <Router history={history}>
        <Switch>
          <Route path="/login" exact component={Login}/>
          <AuthenticatedRoute path="/" component={IndexPage}/>
        </Switch>
      </Router>
    </ConfigProvider>
  );
}

export default RouterConfig;
