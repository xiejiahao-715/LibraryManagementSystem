<template>
  <div class="bookBorrowList">
    <el-table :data="borrowBookList" border stripe highlight-current-row table-layout="auto">
      <el-table-column label="序号" fixed="left" align="center">
        <template #default="{$index}">{{$index + 1 + (pagination.current - 1) * pagination.limit}}</template>
      </el-table-column>
      <el-table-column label="订单号" prop="id" align="center"></el-table-column>
      <el-table-column label="图书名称" prop="bookName" align="center"></el-table-column>
      <el-table-column label="借阅时间" prop="borrowBookDate" align="center"></el-table-column>
      <el-table-column label="预期还书时间" prop="returnBookDate" align="center"></el-table-column>
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
            <el-popconfirm v-if="row.status !== 2" title="确认归还图书?" @confirm="returnBook(row.id)">
              <template #reference><el-button>归还图书</el-button></template>
            </el-popconfirm>
            <el-button v-if="row.status === 0" @click="openRenewBookDialog(row.id)">续借</el-button>
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

    <el-dialog v-model="renewBookVisible" title="续借图书" :draggable="true" destroy-on-close>
      <template #footer>
        <el-button @click="renewBookVisible = false">取消</el-button>
        <el-button type="primary" @click="renewBook">确定</el-button>
      </template>
      <div class="renewDialog">
        <div>是否续借该图书?</div>
        <div>
          选择续借的天数：<el-input-number v-model="renewBookForm.renewTime" :min="1" :max="30" size="small" ></el-input-number>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {markRaw, onBeforeMount, shallowReactive, shallowRef} from "vue";
import {getPageBorrowBookInfoApi,returnBookApi,renewBookApi} from "@/api/borrower";
import {useRouter} from "vue-router";
import {ElMessage} from "element-plus";
import {useUserInfoStore} from "@/store/useUserInfoStore";

export default {
  name: "BookBorrowList",
  setup(){
    // 分页信息
    const pagination = shallowReactive({
      current: 1,
      limit: 10,
      total: 0
    })
    // 页号改变
    function pageChange(){
      getPageBorrowBookInfo()
    }

    // 借阅信息
    const borrowBookList = shallowReactive([])
    function getPageBorrowBookInfo(){
      // 先清空数据
      borrowBookList.splice(0,borrowBookList.length)
      getPageBorrowBookInfoApi(pagination.current,pagination.limit)
        .then(res=>{
          borrowBookList.push(...res.data.records)
          pagination.total = res.data.total
        })
    }
    onBeforeMount(()=>{
      getPageBorrowBookInfo()
    })

    // 查看图书详情
    const router = useRouter()
    function getBookDetail(bookId){
      router.push({path:`/bookDetail/${bookId}`})
    }

    // 归还图书
    const UserInfoStore = useUserInfoStore()
    function returnBook(borrowId){
      returnBookApi(borrowId)
        .then(res=>{
          ElMessage({type: 'success',message: res.message,duration: 5000,showClose:true})
          getPageBorrowBookInfo()
          // 刷新用户资源
          UserInfoStore.getUserResource()
        })
    }


    // 续借图书
    const renewBookVisible = shallowRef(false)
    const renewBookForm = shallowReactive({
      borrowId: null,
      renewTime: null
    })
    function renewBook(){
      renewBookApi(markRaw(renewBookForm))
        .then(res=>{
          ElMessage({type: 'success',message: res.message,duration: 5000,showClose:true})
          getPageBorrowBookInfo()
          renewBookVisible.value = false
        })
    }
    function openRenewBookDialog(borrowId){
      renewBookForm.borrowId = borrowId
      renewBookForm.renewTime = 30
      renewBookVisible.value = true
    }

    return {
      pagination,pageChange,
      borrowBookList,getBookDetail,returnBook,
      renewBookVisible,renewBookForm,renewBook,openRenewBookDialog
    }
  }
}
</script>

<style scoped lang="scss">
.bookBorrowList{
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
  .renewDialog{
    &>div{
      font-weight: bold;
    }
    &>div:not(:last-child){
      margin-bottom: 10px;
    }
  }
}
</style>
