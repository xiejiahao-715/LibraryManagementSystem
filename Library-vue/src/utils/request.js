import axios from "axios";
import {getToken} from "@/utils/auth";
import router from "@/router";
import ResultCode from '@/utils/RequestCode'
import {ElMessage} from "element-plus";
import {useUserInfoStore} from "@/store/useUserInfoStore";


const baseURL = 'http://127.0.0.1:8080/api'

const service = axios.create({
  baseURL: baseURL,
  timeout: 10000
})

service.interceptors.request.use(
    config=>{
      let token = getToken()
      if(token){
        config.headers['token'] = token
      }
      return config
    },
    error => {
      console.log(error)
      return Promise.reject(error)
    }

)
service.interceptors.response.use(
    response=>{
      let res = response.data
      if(res){
        if(res.success === true && res.code === ResultCode.SUCCESS){
          return res
        }else if(res.code === ResultCode.USER_RE_LOGIN){
          const UserInfoStore = useUserInfoStore()
          // 清除用户数据
          UserInfoStore.resetUserInfo()
          // 重新登录状态码,重定向到登录页面
          router.push("/login")
        }
      }
      let error = (res && res.message) || '未知错误'
      ElMessage({
        type: 'error',
        message: error,
        duration: 5000,
        showClose: true
      })
      return Promise.reject(error)
    },
    error => {
      ElMessage({
        type: 'error',
        message: error,
        duration: 5000,
        showClose: true
      })
      return Promise.reject(error)
    }
)

export default service
