import request from "@/utils/request";

export function loginApi(loginForm){
  return request({
    url: '/login',
    method: 'post',
    data:{
      username: loginForm.username,
      password: loginForm.password
    }
  })
}

export function getUserInfoApi(){
  return request({
    url: '/getUserInfo',
    method: 'get'
  })
}

export function registerApi(form){
  return request({
    url: '/register',
    method: 'post',
    data: form
  })
}

// 获取用户的消息列表
export function getUserMsgListApi(){
  return request({
    url: '/user/userManage/msg',
    method: 'get'
  })
}
