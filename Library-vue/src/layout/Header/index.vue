<template>
  <div class="header">
    <div class="left">
      <!--折叠按钮-->
      <div class="collapse-btn" @click="collapseChange">
        <i v-if="!collapse" class="el-icon-xjh-collapse-on"></i>
        <i v-else class="el-icon-xjh-collapse-close"></i>
      </div>
      <div class="logo">图书管理系统</div>
    </div>
    <div class="right">
      <div class="user-avatar">
        <img src="~@/assets/img/userDefaultAvatar.jpg" alt="">
      </div>
      <el-button style="margin-left: 10px;" @click="logout">退出</el-button>
    </div>
  </div>
</template>

<script>
import {useAppSettingStore} from "@/store/useAppSettingStore";
import {useUserInfoStore} from "@/store/useUserInfoStore";
import {storeToRefs} from 'pinia'
import {useRouter} from "vue-router";

export default {
  name: "Header",
  setup(){
    const router = useRouter()
    const AppSettingStore = useAppSettingStore()
    const UserInfoStore = useUserInfoStore()

    const {sidebarCollapse : collapse} = storeToRefs(AppSettingStore)

    // 修改侧边栏是否折叠的状态
    function collapseChange(){
      AppSettingStore.collapseChange()
    }

    // 退出
    function logout(){
      UserInfoStore.resetUserInfo()
      router.push("/login")
    }

    return {
      collapse,collapseChange,logout
    }
  }
}
</script>

<style scoped lang="scss">
@import "~@/layout/variable.scss";

.header{
  position: relative;
  box-sizing: border-box;
  width: 100%;
  height: $headerHeight;
  font-size: 22px;
  color: #fff;
  background: #242f42;
  display: flex;
  justify-content: space-between;
  .left{
    display: flex;
    align-items: center;
    justify-content: center;
    height: 100%;
    .collapse-btn{
      padding: 0 21px;
      cursor: pointer;
      margin-top: 3px;
    }
  }
  .right{
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 50px;
    .user-avatar{
      margin-left: 20px;
      img{
        display: block;
        width: 40px;
        height: 40px;
        border-radius: 50%;
      }
    }
  }
}
</style>
