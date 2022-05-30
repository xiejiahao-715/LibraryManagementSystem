import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import router from "@/router/index";
import {getToken} from "@/utils/auth";
import {useUserInfoStore} from "@/store/useUserInfoStore";

// 不需要重定向的白名单
const whiteList = ['/login']

router.beforeEach(async (to,from,next)=>{
  NProgress.start()
  const hasToken = getToken()
  if(hasToken){
    if(to.path === '/login'){
      next({path: '/'})
      NProgress.done()
    }else{
      const UserInfoStore = useUserInfoStore()
      if(UserInfoStore.hasGetUserInfo){
        // 已经获取了用户的权限信息，但是路由不存在的情况下跳转到首页
        if(to.matched.length === 0){
          next({path: '/'})
        }else {
          // 正常情况下直接跳转
          next()
        }
      }else{
        try {
          // TODO 获取用户信息和权限,动态生成了路由
          await UserInfoStore.getUserInfo()
          next({ ...to, replace: true })
        }catch (error){
          //TODO 获取用户信息失败则重置用户信息
          UserInfoStore.resetUserInfo()
          next({path: `/login?redirect=${to.path}`})
          NProgress.done()
        }
      }
    }
  }else{
    // 没有token
    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      const UserInfoStore = useUserInfoStore()
      UserInfoStore.resetUserInfo()
      next({path: `/login?redirect=${to.path}`})
      NProgress.done()
    }
  }
})
router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
