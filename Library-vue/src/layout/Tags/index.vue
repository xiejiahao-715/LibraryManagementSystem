<template>
  <div class="tags-wrapper" v-if="AppSettingStore.isShowTagList && tagList.length > 0">
    <transition-group tag="div" name="tag-fade" class="tags-list" mode="out-in">
      <div class="tag-item" v-for="(tag,index) in tagList" :key="tag.path" :class="{'active-tag':isActive(tag)}">
        <router-link :to="tag.path">{{tag.title}}</router-link>
        <i class="el-icon-xjh-close tag-delete-icon" @click="closeTag(index)"></i>
      </div>
    </transition-group>
  </div>
</template>

<script>
import {useAppSettingStore} from '@/store/useAppSettingStore'
import {useRouter,useRoute} from "vue-router";
import {watch} from 'vue'
import {storeToRefs} from "pinia";

export default {
  name: "Tags",
  setup(){
    const route = useRoute()
    const router = useRouter()
    const AppSettingStore = useAppSettingStore()

    // 获取store中的标签列表
    const {tagList} = storeToRefs(AppSettingStore)

    // 判断标签是否为当前页面
    const isActive = (tag)=>{
      return tag.path === route.path
    }

    // 根据当前的路由项设置标签
    function setTag(routeItem){
      // 判断路由对象是否合理，有条件变为一个tag
      if(routeItem.meta && routeItem.path && routeItem.meta.title){
        if(routeItem.meta.showInTag === false){
          // 配置不显示该路由tag
          return;
        }
        // 判断跳转的路由是否已经在导标签中存在
        const isExist = AppSettingStore.isExistTag(routeItem)
        if(!isExist){
          // 不存在重复的标签则添加
          AppSettingStore.addTag(routeItem)
        }
      }
    }
    watch(()=>route.path,()=>{
      // 每当路由更新后调用设置路由的方法
      setTag(route)
    },{immediate: true,deep: true})

    // 关闭某一个标签
    const closeTag = (index) =>{
      const delItem = tagList.value[index]
      // 删除该标签
      AppSettingStore.delTags(index,1)
      // 标签被删除后下一个展示的标签
      const nextShowItem = tagList.value[index] ? tagList.value[index] : tagList.value[index - 1]
      if(nextShowItem){
        // 当前删除的路由和标签一致，则跳转到下一个标签
        delItem.path === route.path && router.push(nextShowItem.path)
      }else{
        router.push("/")
      }
    }

    return {
      AppSettingStore,isActive,closeTag,tagList
    }
  }
}
</script>

<style scoped lang="scss">
@import "~@/layout/variable.scss";
.tags-wrapper{
  position: relative;
  height: $tagDivHeight;
  overflow: hidden;
  background: #ffffff;
  box-shadow: 0 5px 5px #ddd;
  .tags-list{
    box-sizing: border-box;
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    // 标签消息的动画
    .tag-item{
      width: auto;
      flex-grow: 0;
      margin: 3px 5px 2px 3px;
      border-radius: 3px;
      font-size: 12px;
      overflow: hidden;
      cursor: pointer;
      line-height: $tagHeight;
      border: 1px solid #e9eaec;
      background: #fff;
      padding: 0 5px 0 12px;
      vertical-align: middle;
      color: #666;
      a{
        text-decoration: none;
        color: inherit;
      }
      .tag-delete-icon{
        font-size: 14px;
        line-height: $tagHeight;
      }
      &.active-tag{
        color: #fff;
        border: 1px solid #409EFF;
        background-color: #409EFF;
      }
      &:not(.active-tag):hover{
        background: #f8f8f8;
      }
    }
  }
}
// tag的标签添加和删除的动画
.tag-fade-leave-active,.tag-fade-enter-active{
  transition: all .3s ease-in-out;
}
.tag-fade-leave-from{
  transform: scale(1);
}
.tag-fade-leave-to{
  transform: scale(0);
}
.tag-fade-enter-from{
  opacity: 0;
}
.tag-fade-enter-to{
  opacity: 1;
}
</style>
