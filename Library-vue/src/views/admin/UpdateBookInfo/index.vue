<template>
  <div class="updateBook">
    <el-card>
      <el-button text  bg type="success" style="margin-bottom: 10px" @click="$router.back()">
        <el-icon><ArrowLeftBold /></el-icon>返回
      </el-button>
      <el-form label-position="left" label-width="auto" size="large">
        <el-form-item label="标题">
          <el-input v-model="book.name" placeholder="请输入图书标题" clearable></el-input>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="book.typeId" placeholder="选择图书分类" clearable>
            <el-option
                v-for="type in bookTypeList" :key="type.id"
                :label="type.name" :value="type.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="作者">
          <el-input v-model="book.author" placeholder="请输入图书作者" clearable></el-input>
        </el-form-item>
        <el-form-item label="出版社">
          <el-input v-model="book.press" placeholder="请输入图书出版社" clearable></el-input>
        </el-form-item>
        <el-form-item label="ISBN">
          <el-input v-model="book.isbn" placeholder="请输入图书ISBN号" clearable></el-input>
        </el-form-item>
        <el-form-item label="出版时间">
          <el-date-picker v-model="book.publishTime" type="date" placeholder="请选择出版日期" format="YYYY-MM-DD" value-format="YYYY-MM-DD" clearable />
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="book.description" placeholder="请输入图书简介" clearable></el-input>
        </el-form-item>
        <el-form-item label="定价">
          <el-input v-model="book.price" placeholder="请输入图书定价" clearable></el-input>
        </el-form-item>
        <el-button type="primary" @click="updateBook">修改</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import {computed, markRaw, onBeforeMount, shallowReactive} from "vue";
import {getBookInfoByIdApi, getBookTypeApi} from "@/api/borrower";
import {useRoute,useRouter} from "vue-router";
import {copyProperties} from "@/utils";
import {updateBookApi} from "@/api/admin";
import {ElMessage} from "element-plus";
import {ArrowLeftBold} from '@element-plus/icons-vue'

export default {
  name: "UpdateBookInfo",
  components:{
    ArrowLeftBold
  },
  setup(){
    const book = shallowReactive({
      typeId: '',
      isbn: '',
      name: '',
      author: '',
      press: '',
      price: '',
      publishTime: '',
      description: '',
    })
    const route = useRoute()
    const bookId = computed(()=>{
      return route.params.bookId ? route.params.bookId : null
    })
    function getBookInfo(){
      getBookInfoByIdApi(bookId.value)
          .then(res=> {
            copyProperties(res.data.book,book)
          })
    }
    onBeforeMount(()=>{
      getBookInfo()
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
    onBeforeMount(()=>{
      getBookType()
    })

    const router = useRouter()
    function updateBook(){
      let form = markRaw(book)
      form.id = bookId.value
      updateBookApi(form)
        .then(res=>{
          ElMessage({type: 'success',duration: 5000,message: res.message,showClose: true})
          router.push({name: 'AllBook'})
        })
    }

    return {
      book,bookTypeList,
      updateBook
    }
  }
}
</script>

<style scoped lang="scss">
.updateBook{
  padding: 20px 30px 30px 20px;
}
</style>
