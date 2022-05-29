<template>
  <div class="bookReserveList">
    <el-table :data="reserveBookList" border stripe highlight-current-row table-layout="auto" flexible>
      <el-table-column label="序号" fixed="left" align="center">
        <template #default="{$index}">{{$index + 1 + (pagination.current - 1) * pagination.limit}}</template>
      </el-table-column>
      <el-table-column label="订单号" prop="id" align="center"></el-table-column>
      <el-table-column label="图书名称" prop="bookName" align="center"></el-table-column>
      <el-table-column label="预约时间" prop="reserveTime" align="center"></el-table-column>
      <el-table-column label="通知时间" align="center">
        <template #default="{row}">
          <div v-if="row.notifyTime">{{row.notifyTime}}</div>
          <div v-else>暂未通知</div>
        </template>
      </el-table-column>
      <el-table-column label="订单状态" prop="description" align="center"></el-table-column>
      <el-table-column label="操作" fixed="right" align="center" width="110px">
        <template #default="{row}">
          <div class="operation">
            <el-button @click="getBookDetail(row.bookId)">查看图书</el-button>
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
import {onBeforeMount, shallowReactive} from "vue";
import {getPageReserveBookInfoApi} from "@/api/borrower";
import {useRouter} from "vue-router";

export default {
  name: "BookReserveList",
  setup(){
    const pagination = shallowReactive({
      current: 1,
      limit: 10,
      total: 0
    })
    function pageChange(){
      getPageReserveBookInfo()
    }

    // 预约信息列表
    const reserveBookList = shallowReactive([])
    function getPageReserveBookInfo(){
      // 先清除预约信息列表
      reserveBookList.splice(0, reserveBookList.length)
      getPageReserveBookInfoApi(pagination.current,pagination.limit)
        .then(res=>{
          reserveBookList.push(...res.data.records)
          pagination.total = res.data.total
        })
    }
    onBeforeMount(()=>{
      getPageReserveBookInfo()
    })


    // 查看图书详情
    const router = useRouter()
    function getBookDetail(bookId){
      router.push({path:`/bookDetail/${bookId}`})
    }

    return {
      pagination,pageChange,
      reserveBookList,getBookDetail
    }
  }
}
</script>

<style scoped lang="scss">
.bookReserveList{
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
