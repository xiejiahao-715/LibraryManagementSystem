<template>
  <div class="ManageAdmin">
    <el-button type="primary" style="margin-bottom: 10px" @click="openAddAdminDialog">添加管理员</el-button>
    <el-table :data="adminList" border stripe highlight-current-row table-layout="auto">
      <el-table-column label="序号" align="center">
        <template #default="{$index}">{{$index + 1 + (pagination.current - 1) * pagination.limit}}</template>
      </el-table-column>
      <el-table-column label="用户ID" prop="id"></el-table-column>
      <el-table-column label="用户名" prop="username"></el-table-column>
      <el-table-column label="联系方式" prop="email"></el-table-column>
      <el-table-column label="用户状态" prop="description"></el-table-column>
    </el-table>
    <el-pagination
        style="margin-top: 10px"
        background layout="total, prev, pager, next"
        :total="pagination.total"
        :page-size="pagination.limit"
        v-model:current-page="pagination.current"
        @current-change="pageChange"/>

    <el-dialog title="添加管理员" v-model="addAdminDialogVisible" @closed="addAdminDialogClosed">
      <template #footer>
        <el-button @click="addAdminDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addAdmin">确定</el-button>
      </template>
      <el-form
          ref="addAdminFormRef" destroy-on-close label-width="auto"
          :model="addAdminForm" :rules="addAdminFormRules">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="addAdminForm.username" placeholder="请输入管理员用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="addAdminForm.password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item label="请输入邮箱" prop="email">
          <el-input v-model="addAdminForm.email" placeholder="请输入邮箱"></el-input>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import {markRaw, onBeforeMount, shallowReactive, shallowRef} from "vue";
import {getPageAdminInfoApi,addAdminApi} from "@/api/superadmin";
import {ElMessage} from "element-plus";

export default {
  name: "ManageAdmin",
  setup(){
    const pagination = shallowReactive({
      current: 1,
      limit: 10,
      total: 0
    })
    function pageChange(){
      getAdminList()
    }

    const adminList = shallowReactive([])
    function getAdminList(){
      adminList.splice(0,adminList.length)
      getPageAdminInfoApi(pagination.current,pagination.limit)
        .then(res=>{
          adminList.push(...res.data.records)
          pagination.total = res.data.total
        })
    }
    onBeforeMount(()=>{
      getAdminList()
    })

    // 添加管理员
    const addAdminDialogVisible = shallowRef(false)
    function openAddAdminDialog(){
      addAdminDialogVisible.value = true
    }
    function addAdminDialogClosed(){
      // 重置表单的值
      addAdminFormRef.value.resetFields()
    }
    // 添加管理员表单组件
    const addAdminFormRef = shallowRef()
    // 添加管理员的表单
    const addAdminForm = shallowReactive({
      username: '',
      password: '',
      email: ''
    })
    const addAdminFormRules = {
      username: [{required: true,trigger:'blur',message: '用户名不能为空'}],
      password: [{required: true,trigger:'blur',message: '密码不能为空'}],
      email: [{required: true,trigger:'blur',message: '邮箱格式不能为空'},{type: 'email',trigger: 'blur',message: '邮箱格式不正确'}]
    }
    // 添加管理员
    function addAdmin(){
      addAdminFormRef.value.validate(isValid=>{
        if(isValid){
          addAdminApi(markRaw(addAdminForm))
            .then(res=>{
              ElMessage({type: 'success',message: res.message,duration: 5000,showClose: true})
              getAdminList()
              addAdminDialogVisible.value = false
            })

        }
      })
    }

    return {
      pagination,pageChange,
      adminList,
      addAdminDialogVisible,openAddAdminDialog,addAdminDialogClosed,
      addAdminForm,addAdminFormRef,addAdminFormRules,addAdmin
    }
  }
}
</script>

<style scoped lang="scss">
.ManageAdmin{
  padding: 20px 30px 30px 20px;
}
</style>
