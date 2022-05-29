<template>
  <div class="batchAddBook">
    <el-alert type="info" show-icon title="通过上传Excel文件来批量导入图书" :closable="false"></el-alert>
    <el-upload
        ref="uploadRef"
        style="width: 500px;margin-top: 10px;"
        drag action="" :auto-upload="false" :limit="1"
        accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel"
        :on-change="fileChange"
        :on-exceed="fileExceed"
        :before-remove="removeFile">
      <el-icon class="el-icon--upload"><upload-filled /></el-icon>
      <div class="el-upload__text">
        拖拽文件到此处 或 <em>点击这里上传</em>
      </div>
      <template #tip>
        <div class="el-upload__tip" style="font-size: 14px">
          只允许接受.xls和.xlsx文件，一次只能上传 1 个文件
        </div>
      </template>
    </el-upload>
    <el-button type="success" @click="uploadFile">批量导入</el-button>
    <el-button type="info" @click="downloadTemplate">模板文件下载</el-button>
  </div>
</template>

<script>
import {UploadFilled} from '@element-plus/icons-vue'
import {shallowRef} from "vue";
import {ElMessage,genFileId} from "element-plus";
import {batchAddBookApi} from "@/api/admin";

export default {
  name: "BatchAddBook",
  components:{
    UploadFilled
  },
  setup(){
    // 下载批量导入的模板文件
    function downloadTemplate(){
      let publicPath = process.env.BASE_URL
      let link = document.createElement('a')
      link.style.display = 'none'
      link.href = `${publicPath}批量导入图书模板.xls`
      console.log(link.href)
      document.body.appendChild(link)
      link.click()
      link.remove()
    }

    // 上传组件
    const uploadRef = shallowRef()
    const currentFile = shallowRef(null)
    // 文件是否合法
    function isFileAllowed(file){
      // 判断文件的类型是否正确
      const allowType = ['xls','xlsx']
      let suffix = file.name.substring(file.name.lastIndexOf('.') + 1).toLowerCase()
      if(allowType.indexOf(suffix) === -1){
        return false
      }
      // 判断大小是否合理 小于2M
      const limitSize = 1024 * 1024 * 2
      return file.size <= limitSize;
    }
    // 当文件状态改变的时候触发，例如添加文件
    function fileChange(file){
      if(!isFileAllowed(file)){
        ElMessage({type: 'warning',message: '文件不符合要求',duration: 5000,showClose: true})
        uploadRef.value.handleRemove(file)
      }else{
        // 记录当前文件
        currentFile.value = file.raw
      }
    }
    // 当文件超出限制的时候
    function fileExceed(files){
      // 取第一个文件
      let file = files[0]
      if(!isFileAllowed(file)){
        ElMessage({type: 'warning',message: '文件不符合要求',duration: 5000,showClose: true})
      }else{
        // 清空列表
        uploadRef.value.clearFiles()
        file.uid = genFileId()
        // 设置第一个文件
        uploadRef.value.handleStart(file)
        // 记录当前文件
        currentFile.value = file
      }
    }
    // 文件移除
    function removeFile(){
      currentFile.value = null
    }
    // 上传文件
    function uploadFile(){
      if(currentFile.value){
        batchAddBookApi(currentFile.value)
          .then(()=>{
            ElMessage({type: 'success',message: '上传文件成功',duration: 5000,showClose: true})
            uploadRef.value.clearFiles()
          })
      }else {
        ElMessage({type: 'error',message: '请先上传文件',duration: 5000,showClose: true})
      }
    }
    return {
      downloadTemplate,
      uploadRef,fileChange,fileExceed,currentFile,removeFile,uploadFile
    }
  }
}
</script>

<style scoped lang="scss">
.batchAddBook{
  padding: 20px 30px 30px 20px;
}
</style>
