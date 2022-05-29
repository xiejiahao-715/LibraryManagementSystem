<template>
  <div class="ManageBookBorrow">
    <!-- 主要赋值催还图书的模块    -->
    <el-table :data="borrowList" border stripe highlight-current-row table-layout="auto">
      <el-table-column label="序号" fixed="left" align="center">
        <template #default="{$index}">{{$index + 1 + (pagination.current - 1) * pagination.limit}}</template>
      </el-table-column>
      <el-table-column label="订单号" prop="id" align="center"></el-table-column>
      <el-table-column label="图书名称" prop="bookName" align="center"></el-table-column>
      <el-table-column label="借阅者名称" prop="username" align="center"></el-table-column>
      <el-table-column label="借阅日期" prop="borrowBookDate" align="center"></el-table-column>
      <el-table-column label="预期还书日期" prop="returnBookDate" align="center"></el-table-column>
      <el-table-column label="借阅时长(天)" prop="borrowTime" align="center"></el-table-column>
      <el-table-column label="订单完成时间" align="center">
        <template #default="{row}">
          <div v-if="row.endDate">{{row.endDate}}</div>
          <div v-else>未完成</div>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" prop="description" align="center"></el-table-column>
      <el-table-column label="操作" fixed="right" align="center" width="120px">
        <template #default="{row}">
          <div class="operation">
            <el-popconfirm title="确认催还图书？" v-if="row.status !== 2" @confirm="sendNotify(row.id)">
              <template #reference>
                <el-button type="primary">催还图书</el-button>
              </template>
            </el-popconfirm>
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
  </div>
</template>

<script>
import {getPageBorrowBookDetailApi, recallBookReturnNotifyApi} from '@/api/admin'
import {onBeforeMount, shallowReactive} from "vue";
import {useRouter} from "vue-router";
import {ElMessage} from "element-plus";

export default {
  name: "ManageBookBorrow",
  setup(){
    const pagination = shallowReactive({
      limit: 10,
      current: 1,
      total: 0
    })
    function pageChange(){
      getBorrowList()
    }

    // 借阅信息列表
    const borrowList = shallowReactive([])
    function getBorrowList(){
      borrowList.splice(0,borrowList.length)
      getPageBorrowBookDetailApi(pagination.current,pagination.limit)
        .then(res=>{
          borrowList.push(...res.data.records)
          pagination.total = res.data.total
        })
    }
    onBeforeMount(()=>{
      getBorrowList()
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

    // 发送催还图书的通知
    function sendNotify(borrowId){
      recallBookReturnNotifyApi(borrowId)
        .then(res=>{
          ElMessage({type: 'success',duration: 5000,showClose: true,message: res.message})
        })
    }

    return {
      pagination,pageChange,
      borrowList,
      toBookDetailPage,sendNotify
    }
  }
}
</script>

<style scoped lang="scss">
.ManageBookBorrow{
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
}
</style>
