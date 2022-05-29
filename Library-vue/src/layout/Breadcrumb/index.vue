<template>
  <!-- 面包屑导航主键 -->
  <el-breadcrumb separator="/" class="breadcrumb-wrapper">
    <transition-group name="breadcrumb" mode="out-in">
      <el-breadcrumb-item v-for="(item,index) in crumbList" :key="item.path">
        <router-link  v-if="index === 0" :to="{path: item.path}">{{item.meta.title}}</router-link>
        <span v-else>{{item.meta.title}}</span>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script>
import {shallowReactive, watch} from "vue";
import { useRoute} from "vue-router";

export default {
  name: "Breadcrumb",
  setup(){
    const route = useRoute()
    // 添加一个默认的首页,首页的默认path: '/dashboard'
    const crumbList = shallowReactive([{path: '/dashboard',meta: {title: '首页'}}])

    // 获取面包屑列表
    function getBreadcrumb() {
      // 筛选出设置了title值的路由
      let matched = route.matched.filter(item => item.path && item.meta && item.meta.title && item.meta.breadcrumb !== false)
      let crumbIndex = 0
      let matchedIndex = 0
      // 判断匹配的路由中的第一个是否为首页,如果不为首页，则将crumbIndex+1，从下一个开启匹配
      if(!isDashboard(matched[0])){
        crumbIndex++
      }
      // 将当前的路由列表crumbList 和 匹配到的列表matched 进行匹配
      //找到第一个不同的项，然后将matched数组后面不同的路由项加入替换到面包屑导航中 前面已经相同的就不需要改动了
      while (crumbIndex < crumbList.length && matchedIndex < matched.length){
        if(crumbList[crumbIndex].path === matched[matchedIndex].path){
          crumbIndex++
          matchedIndex++
        }else{
          break
        }
      }
      crumbList.splice(crumbIndex,crumbList.length - crumbIndex,...matched.slice(matchedIndex))
    }
    // 判断一个路由对象是否代表的为首页
    function isDashboard(routeItem){
      return routeItem && routeItem.path === '/dashboard'
    }

    watch(()=>route.path,()=>{
      getBreadcrumb()
    },{immediate: true,deep: true})

    return {
      crumbList
    }
  }
}
</script>

<style scoped lang="scss">

.breadcrumb-wrapper{
  margin: 10px;
  &>.el-breadcrumb__item:nth-child(1){
    .el-breadcrumb__inner{
      a{
        font-weight: 700;
        text-decoration: none;
        transition: var(--el-transition-color);
        color: var(--el-text-color-primary);
      }
    }
  }
}

.breadcrumb-enter-active, {
  transition: all .2s;
}
.breadcrumb-enter-from{
  opacity: 0;
  transform: translateX(-10px);
}
.breadcrumb-enter-to{
  opacity: 1;
  transform: translateX(0);
}
</style>
