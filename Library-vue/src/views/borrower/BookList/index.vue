<template>
  <div class="bookList">
    <el-form :inline="true" :model="queryForm" label-position="left">
      <el-form-item label="ISBN">
        <el-input v-model="queryForm.isbn" placeholder="按ISBN号筛选" clearable></el-input>
      </el-form-item>
      <el-form-item label="标题">
        <el-input v-model="queryForm.name" placeholder="按标题筛选" clearable ></el-input>
      </el-form-item>
      <el-form-item label="作者">
        <el-input v-model="queryForm.author" placeholder="按作者筛选" clearable ></el-input>
      </el-form-item>
      <el-form-item label="分类">
        <el-select v-model="queryForm.typeId" placeholder="选择图书分类" clearable>
          <el-option
              v-for="type in bookTypeList" :key="type.id"
              :label="type.name" :value="type.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="queryBook">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="bookList" border stripe highlight-current-row ref="tableRef">
      <el-table-column label="序号" fixed="left" align="center" type="index" :index="indexMethod"></el-table-column>
      <el-table-column fixed="left" label="分类" prop="typeName"></el-table-column>
      <el-table-column fixed="left" label="ISBN" prop="isbn"></el-table-column>
      <el-table-column fixed="left" label="封面" align="center">
        <template #default="{row}">
          <el-image :src="row.coverImg">
            <template #error>
                <el-icon><icon-picture /></el-icon>
            </template>
          </el-image>
        </template>
      </el-table-column>
      <el-table-column fixed="left" label="名称" prop="name"></el-table-column>
      <el-table-column label="作者" prop="author"></el-table-column>
      <el-table-column label="出版社" prop="press"></el-table-column>
      <el-table-column label="定价" prop="price"></el-table-column>
      <el-table-column label="出版日期" prop="publishTime"></el-table-column>
      <el-table-column label="数量" prop="stockNum" align="center"></el-table-column>
      <el-table-column label="操作" align="center" fixed="right">
        <template #default="{row}">
          <div class="operation">
            <el-button v-if="row.stockNum !== 0" type="primary" @click="openBorrowBookDialog(row.id)">借阅</el-button>
            <el-button v-else type="success" @click="openReserveBookDialog(row.id)">预定</el-button>
            <el-button type="info" @click="getBookDetail(row.id)">查看详情</el-button>
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

    <el-dialog v-model="borrowBookVisible" title="借阅图书" :draggable="true" destroy-on-close>
      <template #footer>
        <el-button @click="borrowBookVisible = false">取消</el-button>
        <el-button type="primary" @click="borrowBook">确定</el-button>
      </template>
      <div class="borrowDialog">
        <div>是否借阅该图书?</div>
        <div>
          选择借阅的天数：<el-input-number v-model="borrowBookForm.borrowTime" :min="1" :max="30" size="small" ></el-input-number>
        </div>
        <div>已借阅情况：{{`${UserInfoStore.borrowedNum}/${UserInfoStore.borrowMaxNum}`}}</div>
      </div>
    </el-dialog>

    <el-dialog v-model="reserveBookVisible" title="预定图书" :draggable="true" destroy-on-close>
      <template #footer>
        <el-button @click="reserveBookVisible = false">取消</el-button>
        <el-button type="primary" @click="reserveBook">确定</el-button>
      </template>
      <div class="reserveBook">
        <div>是否预定该图书?</div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {useRouter} from 'vue-router'
import {onMounted, shallowReactive, shallowRef, onBeforeMount, markRaw} from 'vue'
import {getPageBookInfoApi,getBookTypeApi,borrowBookApi,reserveBookApi} from "@/api/borrower";
import { Picture as IconPicture } from '@element-plus/icons-vue'
import  {ElLoading,ElMessage} from 'element-plus'
import {useUserInfoStore} from "@/store/useUserInfoStore";

export default {
  name: "BookList",
  components:{
    IconPicture
  },
  setup(){
    // 存储图书信息的数组
    const bookList = shallowReactive([])
    // 表单对象，用于显示加载动画
    const tableRef = shallowRef()
    // 获取图书信息
    function getPageBookInfo(){
      // 先清空数据
      bookList.splice(0,bookList.length)
      const loadingInstance = ElLoading.service({
        target: tableRef.value.$el,
      })
      getPageBookInfoApi(pagination.current,pagination.limit,queryForm)
          .then(res=>{
            if(Array.isArray(res.data.records)){
              bookList.push(...res.data.records)
              pagination.total = res.data.total
            }
          })
          .finally(()=>{
            loadingInstance.close()
          })
    }
    onMounted(()=>{
      getPageBookInfo()
    })

    // 分页数据
    const pagination = shallowReactive({
      current: 1,
      limit: 15,
      total: 0
    })
    // 页码改变的事件
    function pageChange(){
      getPageBookInfo()
    }
    function indexMethod(index){
      return index + 1 + (pagination.current-1) * pagination.limit
    }


    // 筛选条件的表单
    const queryForm = shallowReactive({
      isbn: '',
      author: '',
      name: '',
      typeId: ''
    })
    // 存储图书分类的数组
    const bookTypeList = shallowReactive([])
    function getBookType(){
      getBookTypeApi()
        .then(res=>{
          if(Array.isArray(res.data.types)){
            bookTypeList.splice(0,bookTypeList.length,...res.data.types)
          }
        })
    }
    // 提交表单查询图书信息
    function queryBook(){
      if(pagination.current !== 1) {
        pagination.current = 1
      }
      getPageBookInfo()
    }
    onBeforeMount(()=>{
      getBookType()
    })

    const router = useRouter()
    // 查看书籍详细信息
    function getBookDetail(id){
      router.push({path:`/bookDetail/${id}`})
    }

    const UserInfoStore = useUserInfoStore();


    // 借阅图书
    const borrowBookVisible = shallowRef(false)
    const borrowBookForm = shallowReactive({
      // 借阅图书的id
      bookId: null,
      // 借阅的时长(天)
      borrowTime: null,
    })
    function openBorrowBookDialog(bookId){
      borrowBookVisible.value = true
      borrowBookForm.bookId = bookId
      borrowBookForm.borrowTime = 30
    }
    function borrowBook(){
      borrowBookApi(markRaw(borrowBookForm))
          .then(res=>{
            // 借阅成功
            ElMessage({type: 'success', message: res.message, showClose: true, duration: 5000})
            // 刷新列表
            getPageBookInfo()
            // 刷新用户资源
            UserInfoStore.getUserResource()
            // 关闭对话框
            borrowBookVisible.value = false
          })
    }


    // 预约图书
    const reserveBookVisible = shallowRef(false)
    const reserveBookId = shallowRef(null)
    function openReserveBookDialog(bookId){
      reserveBookVisible.value = true
      reserveBookId.value = bookId
    }
    function reserveBook(){
      reserveBookApi(reserveBookId.value)
        .then(res=>{
          ElMessage({type: 'success', message: res.message, showClose: true, duration: 5000})
          // 刷新列表
          getPageBookInfo()
          // 关闭对话框
          reserveBookVisible.value = false
        })
    }

    return {
      tableRef, bookList,
      pagination,pageChange,indexMethod,
      queryForm,bookTypeList,queryBook,
      getBookDetail,
      UserInfoStore,
      borrowBookVisible,openBorrowBookDialog,borrowBookForm,borrowBook,
      reserveBookVisible, openReserveBookDialog,reserveBook
    }
  }
}
</script>

<style scoped lang="scss">
.bookList{
  padding: 0 20px 30px 20px;
  height: 100%;
  .el-form{
    .el-form-item{
      .el-input{
        width: 200px;
      }
    }
  }
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
    margin-top: 10px;
    ::v-deep{
      .el-pager{
        &>li:not(.is-active){
          background-color: white;
        }
      }
      &>.btn-prev,&>.btn-next{
        &:not([disabled]){
          background-color: white;
        }
      }
    }
  }
  .borrowDialog::v-deep{
    &>div{
      font-weight: bold;
    }
    &>div:not(:last-child){
      margin-bottom: 10px;
    }
  }
}
</style>
