<template>
  <div class="BookOutStockList">
    <el-table :data="bookOutStockList" border stripe highlight-current-row table-layout="auto">
      <el-table-column label="序号" fixed="left" align="center">
        <template #default="{$index}">{{$index + 1 + (pagination.current - 1) * pagination.limit}}</template>
      </el-table-column>
      <el-table-column label="出库订单号" prop="id" align="center"></el-table-column>
      <el-table-column label="图书名称" prop="bookName" align="center"></el-table-column>
      <el-table-column label="操作员" prop="username" align="center"></el-table-column>
      <el-table-column label="出库日期" prop="outStockTime" align="center"></el-table-column>
      <el-table-column label="出库数量" prop="num" align="center"></el-table-column>
      <el-table-column label="操作" align="center">
        <template #default="{row}">
          <div class="operation">
            <el-button @click="toBookDetail(row.bookId)">查看图书</el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
        style="margin-top: 10px"
        background layout="total, prev, pager, next"
        :total="pagination.total"
        :page-size="pagination.limit"
        v-model:current-page="pagination.current"
        @current-change="pageChange"/>
  </div>
</template>

<script>
import {onBeforeMount, shallowReactive} from "vue";
import {getPageBookOutStockInfoApi} from "@/api/admin";
import {useRouter} from "vue-router";

export default {
  name: "BookOutStockList",
  setup(){

    // 分页信息
    const pagination = shallowReactive({
      current: 1,
      limit: 10,
      total: 0
    })
    function pageChange(){
      getBookOutStockList()
    }

    // 图书出库信息列表
    const bookOutStockList = shallowReactive([])
    function getBookOutStockList(){
      bookOutStockList.splice(0,bookOutStockList.length)
      getPageBookOutStockInfoApi(pagination.current,pagination.limit)
        .then(res=>{
          bookOutStockList.push(...res.data.records)
          pagination.total = res.data.total
        })
    }
    onBeforeMount(()=>{
      getBookOutStockList()
    })

    const router = useRouter()
    function toBookDetail(bookId){
      router.push({
        name: 'BookDetail',
        params:{
          bookId: bookId
        }
      })
    }

    return {
      pagination,pageChange,
      bookOutStockList,
      toBookDetail
    }
  }
}
</script>

<style scoped lang="scss">
.BookOutStockList{
  padding: 20px 30px 30px 20px;
  .operation{
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }
}
</style>
