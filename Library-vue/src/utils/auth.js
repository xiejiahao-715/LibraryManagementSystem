import {isString} from "@/utils"

const TOKEN_KEY = 'token'

// 获取token
export function getToken(){
  return localStorage.getItem(TOKEN_KEY)
}

// 删除token
export function removeToken(){
  localStorage.removeItem(TOKEN_KEY)
}

// 设置token
export function setToken(value){
  if(isString(value)){
    localStorage.setItem(TOKEN_KEY,value)
  }
}
