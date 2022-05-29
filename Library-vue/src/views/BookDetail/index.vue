<template>
  <div class="bookDetail">
    <el-card>
      <el-button text  bg type="success" style="margin-bottom: 10px" @click="$router.back()">
        <el-icon><ArrowLeftBold /></el-icon>返回
      </el-button>
      <el-alert v-if="stockInfo.show" :title="stockInfo.message" :type="stockInfo.num > 0 ? 'success' : 'error'" effect="dark" />
      <el-form label-position="left" label-width="85px" size="large">
        <el-form-item label="标题">{{bookInfo.name}}</el-form-item>
        <el-form-item label="封面"><el-image :src="bookInfo.coverImg"></el-image></el-form-item>
        <el-form-item label="作者">{{bookInfo.author}}</el-form-item>
        <el-form-item label="出版社">{{bookInfo.press}}</el-form-item>
        <el-form-item label="出版时间">{{bookInfo.publishTime}}</el-form-item>
        <el-form-item label="简介">{{bookInfo.description}}</el-form-item>
        <el-form-item label="分类">{{bookInfo.typeName}}</el-form-item>
        <el-form-item label="ISBN">{{bookInfo.isbn}}</el-form-item>
        <el-form-item label="定价">{{bookInfo.price}}</el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import {useRoute} from 'vue-router'
import {shallowRef, computed, onBeforeMount, shallowReactive} from "vue";
import {getBookInfoByIdApi} from '@/api/borrower'
import {ArrowLeftBold} from '@element-plus/icons-vue'

export default {
  name: "BookDetail",
  components:{
    ArrowLeftBold
  },
  setup(){
    const route = useRoute()
    const bookInfo = shallowRef({})
    const bookId = computed(()=>{
      return route.params.bookId ? route.params.bookId : null
    })
    function getBookInfo(){
      getBookInfoByIdApi(bookId.value)
        .then(res=>{
          bookInfo.value = res.data.book
          if(Number.isInteger(bookInfo.value.stockNum)){
            stockInfo.show = true
            stockInfo.num = bookInfo.value.stockNum
            stockInfo.message =  bookInfo.value.stockNum > 0 ?
                `该图书还剩余 ${bookInfo.value.stockNum} 本，可去借阅` : `该图书暂无库存，可去预约`
          }else{
            stockInfo.show = false
          }
        })
    }
    onBeforeMount(()=>{
      getBookInfo()
    })

    const stockInfo = shallowReactive({
      message: null,
      num: 0,
      show: false
    })
    return {
      bookInfo,bookId,
      stockInfo
    }
  }
}
</script>

<style scoped lang="scss">
.bookDetail{
  padding: 0 20px 30px 20px;
  .el-form{
    ::v-deep{
      .el-form-item{
        .el-form-item__label{
          font-size: 16px;
          font-weight: bold;
        }
        .el-form-item__content{
          font-size: 15px;
        }
      }
    }
  }
}
</style>
