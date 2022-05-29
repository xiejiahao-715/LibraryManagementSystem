<template>
  <div class="messageList">
    <template v-if="messageList && messageList.length > 0">
      <el-alert
          v-for="message in messageList"
          :key="message.id"
          :title="'时间：'+message.sendDate"
          :description="message.message"
          :closable="false"
          type="info" effect="dark"
          style="margin-bottom: 10px"/>
    </template>
    <template v-else>
      <el-alert title="无任何消息" type="info" effect="dark" />
    </template>
  </div>
</template>

<script>
import {onBeforeMount, shallowReactive} from "vue";
import {getUserMsgListApi} from "@/api/user";

export default {
  name: "MessageList",
  setup(){
    const messageList = shallowReactive([])

    function getMessage(){
      getUserMsgListApi()
        .then(res=>{
          messageList.push(...res.data.msg)
        })
    }
    onBeforeMount(()=>{
      getMessage()
    })
    return {
      messageList
    }
  }
}
</script>

<style scoped lang="scss">
.messageList{
  padding: 20px 30px 30px 20px;
  .el-alert::v-deep{
    .el-alert__description{
      font-size: 13px !important;
    }
  }
}
</style>
