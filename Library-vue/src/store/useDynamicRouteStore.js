import {defineStore} from "pinia";
import {constantRouter,asyncRouter} from '@/router'
import router from "@/router";

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })
  return res
}

export const useDynamicRoutingStore = defineStore('dynamicRouteStore',{
  state: ()=>({
    // 存储经过权限过滤后展示的路由表
    routes: [],
    // 存储将要被移除的路由对象，vue-router4.x移除路由的正确姿势是保存router.addRoute()方法的返回值(类型为函数),然后调用返回值
    willRemovedRoutes: []
  }),
  actions:{
    // 根据权限生成动态路由
    generateDynamicRoutes(roles){
      // 过滤出允许添加的路由对象
      let accessRoutes = filterAsyncRoutes(asyncRouter,roles)
      // 构建完成的路由表
      this.routes = constantRouter.concat(accessRoutes)
      // 添加动态路由
      // 添加之前先尝试动态路由
      this.resetDynamicRoutes()
      // vue-router4.x动态添加路由的方法
      for(let route of accessRoutes){
        let removeRoute = router.addRoute(route)
        // 保存已添加路由的回调，在删除的时候会用到
        this.willRemovedRoutes.push(removeRoute)
      }
    },
    // 重置动态路由
    resetDynamicRoutes(){
      // 删除动态路由
      for(let removeRoute of this.willRemovedRoutes){
        // 调用方法删除路由
        removeRoute()
      }
    }
  }
})
