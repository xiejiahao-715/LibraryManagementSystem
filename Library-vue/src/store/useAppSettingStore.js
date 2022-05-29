import {defineStore} from "pinia";


export const useAppSettingStore = defineStore('appSettingStore',{
  state: ()=> ({
    //  侧边栏的折叠状态,false表示未折叠
    sidebarCollapse: false,

    // 是否显示标签栏
    isShowTagList: true,
    // 标签栏最大的标签数量
    maxTagNum: 8,
    // 存储标签的列表
    tagList: [],
  }),
  getters:{
    // 获取需要缓存的标签列表
    getCachedTagList(state){
      if(state.isShowTagList){
        return state.tagList.filter(tag => tag.cached === true).map(tag => tag.name)
      }else{
        return []
      }
    }
  },
  actions: {
    // 切换侧边栏折叠的状态
    collapseChange(){
      this.sidebarCollapse = !this.sidebarCollapse
    },

    /**
     * 经过检查可以被设置为标签的route对象
     * @param routeItem
     */
    addTag(routeItem){
      // 如果标签栏的数量大于最大标签，则从头部开始删除部分标签便于添加新标签
      if(this.tagList.length >= this.maxTagNum){
        this.delTags(0,this.tagList.length - this.maxTagNum + 1)
      }
      // 添加新标签
      this.tagList.push({
        name: routeItem.name,
        title: routeItem.meta.title,
        path: routeItem.path,
        // 标识该标签对应的路由是否需要被缓存
        cached: routeItem.name && routeItem.meta.cached === true
      })
    },
    // 判断是否已经存在某一个路由标签
    isExistTag(routeItem){
      return this.tagList.some(item=>{
        return item.path === routeItem.path
      })
    },
    // 删除标签
    delTags(start,nums){
      this.tagList.splice(start,nums)
    },
  }
})
