import oldRouterData from '../../config/router';
import dynamic from 'dva/dynamic';

/**
 * @Author: enHui.Chen
 * @Description: 获取平铺的路由数据
 * @Data 2019/9/2
 */
export function getTiledRouterData(app) {
  let newRouterData = {};
  // 进行平铺
  tileRoute2Router(newRouterData, oldRouterData, app);
  return newRouterData;
}

/**
 * @Author: enHui.Chen
 * @Description: 平铺路由数据
 * @Data 2019/9/2
 */
function tileRoute2Router(newRouterData, oldRouterData, app) {
  oldRouterData.forEach(oldRouter => {
    const newRouter = {};
    handleNewRouterData(newRouter, oldRouter, app);
    // 相当于视为必须经过菜单配置的路由才可展示
    newRouterData[oldRouter.path] = newRouter;
    if (oldRouter.components && typeof oldRouter.components !== "undefined" && Array.isArray(oldRouter.components)) {
      tileRoute2Router(newRouterData, oldRouter.components, app);
    }
  });
}

/**
 * @Author: enHui.Chen
 * @Description: 处理新的路由数据(动态注册model和component)
 * @Data 2019/9/2
 */
function handleNewRouterData(newRouter, oldRouter, app) {
  if (oldRouter.component && typeof oldRouter.component !== "undefined") {
    newRouter.component = wrapperDynamic(app, oldRouter);
  }
}

function wrapperDynamic(app, oldRouter) {
  return dynamic({
    app: app,
    models: () => oldRouter.models.filter(m => !modelIsExist(app, m)).map(m => import(`../models/${m}.js`)),
    component: () => import(`../routes/${oldRouter.component}`)
  });
}

/**
 * @Author: enHui.Chen
 * @Description: 判断models是否已存在,true已存在,false不存在
 * @Data 2019/9/3
 */
function modelIsExist(app, model) {
  return app._models.some(({namespace}) => {
    return namespace === model.substring(model.lastIndexOf('/') + 1);
  });
}

/**
 * @Author: enHui.Chen
 * @Description: 生成routerData数据
 * @Data 2019/9/2
 */
export function generateRouterData(routerData, tiledRouterData) {
  routerData.forEach(r => {
    if (!r.isLeaf && r.routerData && typeof r.routerData !== "undefined") {
      return generateRouterData(r.routerData, tiledRouterData);
    }
    if (typeof tiledRouterData[r.path] !== "undefined") {
      r.component = tiledRouterData[r.path].component;
    }
  })
}