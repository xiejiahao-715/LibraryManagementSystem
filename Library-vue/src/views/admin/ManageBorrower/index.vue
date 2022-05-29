<template>
  <div class="borrowerList">
    <el-table :data="borrowerList" border stripe highlight-current-row table-layout="auto">
      <el-table-column label="序号" align="center">
        <template #default="{$index}">{{$index + 1 + (pagination.current - 1) * pagination.limit}}</template>
      </el-table-column>
      <el-table-column label="用户ID" prop="id"></el-table-column>
      <el-table-column label="用户名" prop="username"></el-table-column>
      <el-table-column label="联系方式" prop="email"></el-table-column>
      <el-table-column label="已借阅数量" prop="borrowedNum"></el-table-column>
      <el-table-column label="最大借阅数量" prop="borrowMaxNum"></el-table-column>
      <el-table-column label="用户状态" prop="description"></el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="{row}">
          <el-button @click="openDialog(row)">编辑最大借阅数量</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        background layout="total, prev, pager, next"
        :total="pagination.total"
        :page-size="pagination.limit"
        v-model:current-page="pagination.current"
        @current-change="pageChange"/>

    <el-dialog title="编辑最大借阅数量" v-model="dialogVisible">
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="updateBorrowMaxNum">确定</el-button>
      </template>
      <div style="font-weight: bold">
        <div style="margin-bottom: 10px;">当前用户已借阅的数量：{{borrowerResource.borrowedNum}}</div>
        <div>
          <span>最大借阅数量：</span>
          <el-input-number :min="borrowerResource.borrowedNum" v-model="borrowerResource.borrowMaxNum" size="small"></el-input-number>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {onBeforeMount, shallowReactive, shallowRef} from "vue";
import {getPageBorrowerInfoApi,updateBorrowMaxNumApi} from "@/api/admin";
import {ElMessage} from "element-plus";

export default {
  name: "ManageBorrower",
  setup(){
    // 分页信息
    const pagination = shallowReactive({
      current: 1,
      limit: 10,
      total: 0
    })
    function pageChange(){
      getBorrowerList()
    }

    // 借阅者信息
    const borrowerList = shallowReactive([])
    function getBorrowerList(){
      borrowerList.splice(0,borrowerList.length)
      getPageBorrowerInfoApi(pagination.current,pagination.limit)
        .then(res=>{
          borrowerList.push(...res.data.records)
          pagination.total = res.data.total
        })
    }
    onBeforeMount(()=>{
      getBorrowerList()
    })

    // 修改借阅者的最大借阅数量
    const dialogVisible = shallowRef(false)
    const borrowerResource = shallowReactive({
      borrowMaxNum: 0,
      borrowedNum: 0,
      borrowerId: ''
    })
    function openDialog(borrowerInfo){
      borrowerResource.borrowerId = borrowerInfo.id
      borrowerResource.borrowedNum = borrowerInfo.borrowedNum
      borrowerResource.borrowMaxNum = borrowerInfo.borrowMaxNum

      dialogVisible.value = true
    }
    function updateBorrowMaxNum(){
      updateBorrowMaxNumApi(borrowerResource.borrowerId,borrowerResource.borrowMaxNum)
        .then(res=>{
          ElMessage({type: 'success',message: res.message,duration: 5000,showClose:true})
          getBorrowerList()
          dialogVisible.value = false
        })
    }

    return {
      pagination,pageChange,
      borrowerList,
      dialogVisible,borrowerResource,openDialog,updateBorrowMaxNum
    }
  }
}
</script>

<style scoped lang="scss">
.borrowerList{
  padding: 20px 30px 20px 30px;
  .el-table{
    .operation{
      width: 100%;
      height: 100%;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      >*{
        margin: 0 2px 10px 2px;
      }
    }
  }
  .el-pagination{
    margin-top: 20px;
  }
}
</style>
