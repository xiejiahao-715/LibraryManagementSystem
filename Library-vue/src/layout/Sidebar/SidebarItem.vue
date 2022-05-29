<template>
  <template v-if="!item.hidden">
    <template v-if="isShowRouteItem(item.children,item) && showedRouteItem">
      <template v-if="showedRouteItem.meta">
        <router-link :to="resolvePath(showedRouteItem.path)">
          <el-menu-item  :index="resolvePath(showedRouteItem.path)">
            <i :class="[showedRouteItem.meta.icon]"></i>
            <template #title>{{showedRouteItem.meta.title}}</template>
          </el-menu-item>
        </router-link>
      </template>
    </template>
    <template v-else>
      <el-sub-menu :index="resolvePath(item.path)">
        <template #title v-if="item.meta">
          <i :class="item.meta.icon" class="sub-menu-icon"></i>
          <span>{{item.meta.title}}</span>
        </template>
        <sidebar-item
            v-for="child in item.children" :key="child.path"
            :item="child" :base-path="resolvePath(item.path)">
        </sidebar-item>
      </el-sub-menu>
    </template>
  </template>
</template>

<script>
import {shallowRef,toRaw} from 'vue'
import path from 'path'

export default {
  name: "SidebarItem",
  props:{
    item:{
      type: Object,
      required: true
    },
    basePath:{
      type: String,
      required: true
    }
  },
  setup(props){

    // 当该组件可以被渲染成菜单项(el-menu-item组件),停止递归渲染菜单，存储被渲染的数据
    const showedRouteItem = shallowRef(null)
    /**
     * 判断该路由对象是否只有一个子路由或者没有子路由
     * 也就是说只有一个子路由或者没有子路由，则会被渲染为el-menu-item组件，停止递归渲染的行为
     * @param children  当前组件的子路由对象  props.item.children
     * @param route 当前组件的路由对象 props.item
     */
    function isShowRouteItem(children,route){
      // 消除响应式
      children = toRaw(children)
      route = toRaw(route)
      if(Array.isArray(children)){
        let tmp = null
        // 查看子路由中可以被渲染的节点
        const showingChildren = children.filter(item => {
          if(item.hidden){
            return false
          }else{
            // 记录可能可以被渲染的路由
            tmp = item
            return true
          }
        })
        if(showingChildren.length === 1){
          // 如果该路由定义了alwaysShow: true属性，则代表不需要合并唯一的子路由
          if(route.alwaysShow === true){
            return false
          }
          // 代表自由一个子路由对象，此时还需要检查是否存在该唯一的子路由是否还存在子路由,此时tmp记录的就是该唯一的子路由
          if(Array.isArray(tmp.children) && tmp.children.length > 0){
            // 子路由该存在子路由，不能渲染成菜单项
            return false
          }else{
            // 子路由没有子路由了，可以渲染
            // 由于把当前路由和唯一的子路由合并了，所以代表的路径也要合并，其余的按照子路由的配置显示
            tmp.path = path.resolve(route.path,tmp.path)
            showedRouteItem.value = tmp
            return true
          }
        }
        if(showingChildren.length === 0){
          // 代表已经没有可以展示的路由对象了
          // 则渲染该组件作为菜单项
          showedRouteItem.value = route
          return true
        }
        return false
      }else{
        // 没有子路由则将 传入的item 渲染为菜单项
        showedRouteItem.value = route
        return true
      }
    }

    function resolvePath(routePath) {
      return path.resolve(props.basePath,routePath)
    }


    return {
      showedRouteItem,
      isShowRouteItem,
      resolvePath
    }
  }
}
</script>

