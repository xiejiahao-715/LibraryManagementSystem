<template>
  <div class="ManageBookReserve">
    <el-table :data="reserveList" border stripe highlight-current-row table-layout="auto" flexible>
      <el-table-column label="序号" fixed="left" align="center">
        <template #default="{$index}">{{$index + 1 + (pagination.current - 1) * pagination.limit}}</template>
      </el-table-column>
      <el-table-column label="订单号" prop="id" align="center"></el-table-column>
      <el-table-column label="预约用户" prop="username" align="center"></el-table-column>
      <el-table-column label="图书名称" prop="bookName" align="center"></el-table-column>
      <el-table-column label="预约时间" prop="reserveTime" align="center"></el-table-column>
      <el-table-column label="通知时间" align="center">
        <template #default="{row}">
          <div v-if="row.notifyTime">{{row.notifyTime}}</div>
          <div v-else>暂未通知</div>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" prop="description" align="center"></el-table-column>
      <el-table-column label="图书库存数量" prop="stockNum" align="center"></el-table-column>
      <el-table-column label="操作" fixed="right" align="center" width="110px">
        <template #default="{row}">
          <div class="operation">
            <el-button v-if="row.status === 0 && row.stockNum === 0" type="primary" @click="openAddBookDialog(row.bookId)">添加库存</el-button>
            <el-button @click="toBookDetailPage(row.bookId)">查看图书</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        background layout="total, prev, pager, next"
        :total="pagination.total"
        :page-size="pagination.limit"
        v-model:current-page="pagination.current"
        @current-change="pageChange"/>

    <el-dialog
        v-model="addBookInfo.dialog" title="添加图书库存"
        :draggable="true" destroy-on-close
        @closed="addBookDialogClosed">
      <template #footer>
        <el-button @click="addBookInfo.dialog = false">取消</el-button>
        <el-button type="primary" @click="addBookStock">确定</el-button>
      </template>
      添加库存的数量：<el-input-number v-model="addBookInfo.addNum" :min="1"></el-input-number>
    </el-dialog>

  </div>
</template>

<script>
import {onBeforeMount, shallowReactive} from "vue";
import {addBookStockApi, getPageReserveBookDetailApi} from "@/api/admin";
import {useRouter} from "vue-router";
import {ElMessage} from "element-plus";

export default {
  name: "ManageBookReserve",
  setup(){
    const pagination = shallowReactive({
      current: 1,
      limit: 5,
      total: 0
    })
    function pageChange(){
      getReserveList()
    }

    const reserveList = shallowReactive([])
    function getReserveList(){
      reserveList.splice(0,reserveList.length)
      getPageReserveBookDetailApi(pagination.current,pagination.limit)
        .then(res=>{
          reserveList.push(...res.data.records)
          pagination.total = res.data.total
        })
    }
    onBeforeMount(()=>{
      getReserveList()
    })

    // 去往图书详情界面
    const router = useRouter()
    function toBookDetailPage(bookId){
      router.push({
        name: 'BookDetail',
        params:{
          bookId: bookId
        }
      })
    }
    // 添加库存
    const addBookInfo = shallowReactive({
      dialog: false,
      bookId: null,
      addNum: null
    })
    function openAddBookDialog(bookId){
      addBookInfo.bookId = bookId
      addBookInfo.addNum = 1
      addBookInfo.dialog = true
    }
    function addBookDialogClosed(){
      addBookInfo.addNum = null
      addBookInfo.dialog = false
      addBookInfo.bookId = null
    }
    // 添加库存
    function addBookStock(){
      addBookStockApi(addBookInfo.bookId,addBookInfo.addNum)
          .then(res=>{
            ElMessage({type: 'success',duration: 5000,showClose: true,message: res.message})
            // 刷新列表
            getReserveList()
            // 关闭对话框
            addBookInfo.dialog = false
          })
    }

    return {
      pagination,pageChange,
      reserveList,
      toBookDetailPage,
      addBookInfo,addBookStock,addBookDialogClosed,openAddBookDialog
    }
  }
}
</script>

<style scoped lang="scss">
.ManageBookReserve{
  padding: 20px 30px 30px 20px;
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
