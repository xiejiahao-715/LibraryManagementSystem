<template>
  <div class="addBook">
    <el-card>
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
        <el-form-item label="数量">
          <el-input-number v-model="book.stockNum" placeholder="请输入图书数量" :min="1"></el-input-number>
        </el-form-item>
        <el-button type="primary" @click="addBook">添加</el-button>
        <el-button @click="reset">重置</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import {markRaw, onBeforeMount, shallowReactive} from "vue";
import {getBookTypeApi} from "@/api/borrower";
import {addBookApi} from "@/api/admin";
import {ElMessage} from "element-plus";
import {useRouter} from "vue-router";
import {copyProperties} from "@/utils";

export default {
  name: "AddBook",
  setup(){
    const router = useRouter()
    // 图书表单的默认值
    const defaultBookValue = ()=>({
      typeId: '',
      isbn: '',
      name: '',
      author: '',
      press: '',
      price: '',
      publishTime: '',
      description: '',
      stockNum: 1
    })
    const book = shallowReactive(defaultBookValue())
    function addBook(){
      addBookApi(markRaw(book))
        .then(res=>{
          ElMessage({type: 'success',duration: 5000,message: res.message,showClose: true})
          router.push({name: 'AllBook'})
        })
    }

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

    // 重置表单
    function reset(){
      copyProperties(defaultBookValue(),book)
    }

    return {
      book,bookTypeList,addBook,
      reset
    }
  }
}
</script>

<style scoped lang="scss">
.addBook{
  padding: 20px 30px 30px 20px;
}
</style>
