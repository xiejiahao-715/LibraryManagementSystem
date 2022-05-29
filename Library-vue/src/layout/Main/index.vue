<template>
  <div class="main">
    <Tags style="flex-shrink: 0"></Tags>
    <Breadcrumb style="flex-shrink: 0"></Breadcrumb>
    <el-scrollbar style="flex: 1;overflow: hidden;" ref="scrollbarRef">
      <router-view v-slot="{ Component }">
        <transition name="move" mode="out-in">
          <keep-alive :include="AppSettingStore.getCachedTagList">
            <component :is="Component" :key="componentKey"/>
          </keep-alive>
        </transition>
      </router-view>
    </el-scrollbar>
  </div>
</template>

<script>
import Tags from "@/layout/Tags";
import Breadcrumb from "@/layout/Breadcrumb";
import {useAppSettingStore} from '@/store/useAppSettingStore'
import {computed, ref, watch} from "vue";
import {useRoute} from "vue-router";
export default {
  name: "Main",
  components: {
    Breadcrumb,
    Tags
  },
  setup(){
    const scrollbarRef = ref()
    const AppSettingStore = useAppSettingStore()
    const route = useRoute()
    // 路由更新时滚动条回到顶部
    watch(()=>route.path,()=>{
      scrollbarRef.value.setScrollTop(0)
    },{deep: true})

    const componentKey = computed(()=>{
      return route.path || undefined
    })

    return {
      AppSettingStore,scrollbarRef,componentKey
    }
  }
}
</script>

<style scoped lang="scss">
.main{
  display: flex;
  flex-direction: column;
  background-color: #f0f0f0;
}
.move-enter-active,
.move-leave-active {
  transition: opacity .2s ease;
}
.move-enter-from,
.move-leave-to {
  opacity: 0;
}
</style>
