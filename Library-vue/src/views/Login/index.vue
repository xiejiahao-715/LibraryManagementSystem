<template>
  <div class="login-wrapper">
    <div class="content">
      <div class="title">图书管理系统</div>
      <el-tabs type="border-card" v-model="currentTabName">
        <el-tab-pane label="登录" lazy name="login">
          <el-form
              ref="loginFormRef"
              :model="loginForm"
              :rules="loginFormRules"
              label-width="auto"
              label-position="left">
            <el-form-item prop="username" label="用户名">
              <el-input v-model="loginForm.username" placeholder="用户名"></el-input>
            </el-form-item>
            <el-form-item prop="password" label="密码">
              <el-input v-model="loginForm.password" placeholder="密码" type="password" show-password></el-input>
            </el-form-item>
            <el-button style="width: 100%" type="primary" @click="login">登录</el-button>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="注册" lazy name="register">
          <el-form
              ref="registerFormRef"
              :model="registerForm"
              :rules="registerFormRules"
              label-width="auto"
              label-position="left">
            <el-form-item prop="username" label="用户名">
              <el-input v-model="registerForm.username" placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item prop="password" label="密码">
              <el-input v-model="registerForm.password" placeholder="请输入密码" type="password" show-password></el-input>
            </el-form-item>
            <el-form-item prop="confirmPassword" label="确认密码">
              <el-input v-model="registerForm.confirmPassword" placeholder="请确认密码" type="password" show-password></el-input>
            </el-form-item>
            <el-form-item prop="email" label="邮箱">
              <el-input v-model="registerForm.email" placeholder="请输入邮箱"></el-input>
            </el-form-item>
          </el-form>
          <el-button style="width: 100%" type="primary" @click="register">注册</el-button>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import {shallowReactive, markRaw, shallowRef} from 'vue'
import {useRouter} from 'vue-router'
import {setToken} from "@/utils/auth";
import {loginApi, registerApi} from "@/api/user";
import {ElMessage} from "element-plus";

export default {
  name: "Login",
  setup(){
    const router = useRouter()
    // 当前选项卡的名字，默认为登录选项卡
    const currentTabName = shallowRef("login")

    // 登录 部分
    const loginFormRef = shallowRef()
    const loginForm = shallowReactive({
      username: '',
      password: ''
    })
    const loginFormRules = {
      username: [{required: true,trigger:'blur',message: '用户名不能为空'}],
      password: [{required: true,trigger:'blur',message: '密码不能为空'}]
    }
    // 具体登录的逻辑
    const login = () =>{
      loginFormRef.value.validate(isValid=>{
        if(isValid){
          loginApi(markRaw(loginForm))
              .then(res=>{
                setToken(res.data.token)
                router.push("/")
              })
        }
      })
    }

    // 注册部分
    const registerFormRef = shallowRef()
    const registerForm = shallowReactive({
      username: '',
      password: '',
      confirmPassword: '',
      email: ''
    })
    const registerFormRules = {
      username: [{required: true,trigger:'blur',message: '用户名不能为空'}],
      password: [{required: true,trigger:'blur',message: '密码不能为空'}],
      confirmPassword: [{required: true,trigger:'blur',message: '密码不能为空'}],
      email: [{required: true,trigger:'blur',message: '邮箱格式不能为空'},{type: 'email',trigger: 'blur',message: '邮箱格式不正确'}]
    }
    function register(){
      registerFormRef.value.validate(isValid=>{
        if(isValid){
          registerApi(markRaw(registerForm))
            .then(res=>{
              ElMessage({type: 'success',duration: 5000,showClose: true,message: res.message})
              // 将选项卡切换到login页面
              currentTabName.value = "login"
            })
        }
      })
    }

    return {
      currentTabName,
      loginForm,loginFormRules, login,loginFormRef,
      registerFormRef,registerForm,registerFormRules,register
    }
  }
}
</script>

<style scoped lang="scss">
.login-wrapper{
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  .content{
    width: 520px;
    margin-bottom: 10%;
    .title{
      font-size: 26px;
      text-align: center;
      margin: 0 auto 40px auto;
      font-weight: bold;
    }
  }
}
</style>
