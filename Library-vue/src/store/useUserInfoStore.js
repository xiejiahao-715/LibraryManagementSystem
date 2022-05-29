import {defineStore} from "pinia";
import {useDynamicRoutingStore} from "@/store/useDynamicRouteStore";
import {useAppSettingStore} from "@/store/useAppSettingStore";
import {getUserInfoApi} from "@/api/user";
import {removeToken} from "@/utils/auth";
import {getUserResourceApi} from "@/api/borrower";

// 管理全局的用户信息即状态
export const useUserInfoStore = defineStore('userInfoStore',{
  state: ()=> ({
    uid: '',
    name: '',
    email: '',
    permissions: [],
    // 用户的借阅资源
    userBookResource:{
      borrowedNum: null,
      borrowMaxNum: null,
    }
  }),
  getters:{
    // 是否已经获取了用户的信息
    hasGetUserInfo(state){
      return state.uid !== '' && state.name && state.email && Array.isArray(state.permissions)
    },
    // 是否能获取用户资源,只有用户拥有图书的资源信息
    hasResource(state){
      return state.permissions.indexOf('borrower') !== -1
    },
    borrowedNum(state){
      return state.userBookResource.borrowedNum
    },
    borrowMaxNum(state){
      return state.userBookResource.borrowMaxNum
    }
  },
  actions:{
    async getUserInfo(){
      // 获取用户信息
      let res = await getUserInfoApi()
      this.uid = res.data.userInfo.id
      this.name = res.data.userInfo.username
      this.email = res.data.userInfo.email
      this.permissions  = res.data.userInfo.permissions

      const DynamicRoutingStore = useDynamicRoutingStore()
      // 根据权限生成动态路由
      DynamicRoutingStore.generateDynamicRoutes(this.permissions)

      // 初始化用户资源
      this.getUserResource()
    },
    // 重置用户信息
    resetUserInfo(){
      const DynamicRoutingStore = useDynamicRoutingStore()
      // 删除动态添加的路由数据（重置vue-router）
      DynamicRoutingStore.resetDynamicRoutes()
      // 重置动态路由的store的值
      DynamicRoutingStore.$reset()

      const AppSettingStore = useAppSettingStore()
      // 重置APP设置
      AppSettingStore.$reset()

      // 清除token
      removeToken()
      // 重置本store的值
      this.$reset()
    },
    // 获取用户志愿
    async getUserResource(){
      if(this.hasResource){
        let res = await getUserResourceApi()
        if(res.data.resource) {
          this.userBookResource.borrowedNum = res.data.resource.borrowedNum
          this.userBookResource.borrowMaxNum = res.data.resource.borrowMaxNum
        }
      }
    }
  }
})
