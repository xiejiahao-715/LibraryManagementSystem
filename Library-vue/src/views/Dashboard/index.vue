<template>
  <div class="dashboard">
    <el-card shadow="hover" style="width: 260px;" class="user-info">
      <template #header>
        <div class="user-info-header">用户信息</div>
      </template>
      <el-form label-width="70px">
        <el-form-item label="用户ID">{{UserInfoStore.uid}}</el-form-item>
        <el-form-item label="用户名称">{{UserInfoStore.name}}</el-form-item>
        <el-form-item label="邮箱">{{UserInfoStore.email}}</el-form-item>
        <el-form-item label="角色">{{UserInfoStore.permissions.join(',')}}</el-form-item>
        <el-form-item v-if="UserInfoStore.hasResource" label="借阅情况">{{`${UserInfoStore.borrowedNum}/${UserInfoStore.borrowMaxNum}`}}</el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import {useUserInfoStore} from "@/store/useUserInfoStore";

export default {
  name: "Dashboard",
  setup(){
    const UserInfoStore = useUserInfoStore()
    return {
      UserInfoStore
    }
  }
}
</script>

<style scoped lang="scss">
.dashboard{
  padding: 20px;
  .user-info{
    .user-info-header{
      font-weight: bold;
    }
    .el-form::v-deep{
      .el-form-item{
        .el-form-item__label{
          font-weight: bold;
        }
      }
    }
  }
}
</style>
