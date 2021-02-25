import dva from 'dva';
import createLoading from 'dva-loading';

// 1. 初始化 state 的数据
const app = dva();
// 绑定到全局对象
window.dvaApp = app;

// 2. 增加插件
app.use(createLoading());

// 3. 载入model
app.model(require('./models/global').default);
app.model(require('./models/platform/common').default);
app.model(require('./models/authorize/login').default);
app.model(require('./models/authorize/user').default);

// 4. 配置路由
app.router(require('./router').default);

// 5. 挂载dom
app.start('#root');

