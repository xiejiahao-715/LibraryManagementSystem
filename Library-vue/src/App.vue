<template>
  <el-config-provider :locale="zhCn">
    <router-view></router-view>
  </el-config-provider>

</template>
<script>
import zhCn from 'element-plus/es/locale/lang/zh-cn'
export default {
  name: 'App',
  setup(){
    return {
      zhCn
    }
  }
}
</script>

<style lang="scss">

</style>
