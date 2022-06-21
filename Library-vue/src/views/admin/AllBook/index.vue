<template>
  <div class="AllBook">
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
      <el-form-item label="排序">
        <el-select v-model="queryForm.sort" placeholder="排序规则" clearable>
          <el-option label="按照出版日期排序" value="1"></el-option>
          <el-option label="按照更新时间排序" value="2"></el-option>
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
      <el-table-column label="操作" align="center" fixed="right" width="170px">
        <template #default="{row}">
          <div class="operation">
            <el-button @click="getBookDetail(row.id)">查看图书</el-button>
            <el-button @click="updateBook(row.id)">修改图书基本信息</el-button>
            <el-button @click="openAddBookDialog(row.id)">添加图书</el-button>
            <el-button v-if="row.stockNum > 0" @click="openOutBookDialog(row.id,row.stockNum)">图书出库</el-button>
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

    <el-dialog
        v-model="outBookInfo.dialog" title="图书出库"
        :draggable="true" destroy-on-close
        @closed="outBookDialogClosed">
      <template #footer>
        <el-button @click="outBookInfo.dialog = false">取消</el-button>
        <el-button type="primary" @click="outBook">确定</el-button>
      </template>
      图书出库的数量：<el-input-number v-model="outBookInfo.num" :min="1" :max="outBookInfo.maxNum"></el-input-number>
    </el-dialog>

  </div>
</template>

<script>
import {useRouter} from 'vue-router'
import {onMounted, shallowReactive, shallowRef, onBeforeMount} from 'vue'
import {getPageBookInfoApi,getBookTypeApi} from "@/api/borrower";
import {addBookStockApi,bookOutStockApi} from "@/api/admin";
import { Picture as IconPicture } from '@element-plus/icons-vue'
import {ElMessage, ElLoading} from 'element-plus'

export default {
  name: "AllBook",
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


    // 筛选条件的表单
    const queryForm = shallowReactive({
      isbn: '',
      author: '',
      name: '',
      typeId: '',
      sort: ''
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
    // 表格索引的方法
    function indexMethod(index){
      return index + 1 + (pagination.current-1) * pagination.limit
    }

    const router = useRouter()
    // 查看书籍详细信息
    function getBookDetail(bookId){
      router.push({
        name: 'BookDetail',
        params:{
          bookId
        }
      })
    }
    // 修改图书基本信息
    function updateBook(bookId){
      router.push({
        name: 'UpdateBookInfo',
        params:{
          bookId
        }
      })
    }

    // 添加库存的功能
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
          getPageBookInfo()
          // 关闭对话框
          addBookInfo.dialog = false
        })
    }


    // 图书出库功能
    const outBookInfo = shallowReactive({
      dialog: false,
      bookId: null,
      num: null,
      maxNum: null
    })
    function openOutBookDialog(bookId,maxNum){
      outBookInfo.bookId = bookId
      outBookInfo.num = 1
      outBookInfo.maxNum = maxNum
      outBookInfo.dialog = true
    }
    function outBookDialogClosed(){
      outBookInfo.dialog = false
      outBookInfo.bookId = null
      outBookInfo.num = null
      outBookInfo.maxNum = null
    }
    // 图书出库接口
    function outBook(){
      bookOutStockApi(outBookInfo.bookId,outBookInfo.num)
        .then(res=>{
          ElMessage({type: 'success',duration: 5000,showClose: true,message: res.message})
          // 刷新列表
          getPageBookInfo()
          // 关闭对话框
          outBookInfo.dialog = false
        })
    }


    return {
      tableRef, bookList,
      pagination,pageChange,indexMethod,
      queryForm,bookTypeList,queryBook,
      getBookDetail,updateBook,
      addBookInfo,openAddBookDialog,addBookDialogClosed,addBookStock,
      outBookInfo,openOutBookDialog,outBookDialogClosed,outBook
    }
  }
}
</script>

<style scoped lang="scss">
.AllBook{
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
