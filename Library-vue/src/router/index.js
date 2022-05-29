import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/layout'

/**
 * 路由表配置每一个route中可以使用的配置项
 * {
 *   path: ''  路由的路径
 *   component: '' 被展示的组件
 *   name: ''  路由的名称，注意，如果希望该组件可以被缓存，那么该name的值必须与展示组件的name的值相同，否则将无法缓存
 *   children: []  子路由配置
 *   hidden: false  是否需要被展示在菜单栏中，默认为true，当设置为hidden: false的时候，该路由及其子路由都不会被渲染在侧边栏中
 *   mate:{
 *     title: ''  路由的标题，在侧边栏，面包屑导航，tag标签栏会被用到
 *     icon: ''  图标的类名称 本项目中使用的iconfont来展示自定义图标
 *     showInTag: true 该路由是否会被展示在tag标签栏中，默认为true，当设置为false时，这不会在标签栏中展示该路由
 *     cached: false  是否需要被缓存，默认为false，当设置为true时，并且该路由被展示在tag标签栏中的时候，被展示的组件将会被缓存
 *                    换句话说，组件缓存是动态的，在tag标签栏中，设置该cached: true的组件才能被缓存，两者必须同时满足
 *     breadcrumb: true 该路由是否需要在面包屑导航中展示，默认为true，当设置为breadcrumb: false,则不会显示该面包屑
 *     roles: [] 配置权限，不配置默认都可以访问，如果没有限制请不要设置为 []
 *   }
 * }
 */

// 静态路由表，不需要任何权限即可访问
export const constantRouter = [
  {
    path: "/login",
    name: "login",
    component: () => import('@/views/Login'),
    hidden: true,
    meta: {
      title: '登录',
      showInTag: false
    },
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children:[
      {
        name: 'Dashboard',
        path: 'dashboard',
        component: () => import('@/views/Dashboard'),
        meta: {
          title: '首页',
          icon: 'el-icon-xjh-home'
        },
      }
    ]
  },
]

export const asyncRouter = [
    // 通用部分
  {
    path: '/bookDetail',
    component: Layout,
    hidden: true,
    children: [
      {
        name: 'BookDetail',
        path: ':bookId',
        component: ()=>import("@/views/BookDetail"),
        meta:{
          title: '图书详情',
          showInTag: false,
        }
      },
    ]
  },
  // 借阅者相关路由
  {
    path: '/messageList',
    component: Layout,
    children:[
      {
        name: 'MessageList',
        path: '',
        component: ()=>import('@/views/MessageList'),
        meta:{
          title: '我的消息',
          icon: 'el-icon-xjh-message',
        }
      }
    ]
  },
    // 借阅者相关路由
  {
    path: '/queryBook',
    component: Layout,
    meta:{
      roles: ['borrower']
    },
    children:[
      {
        name: 'BookList',  // 当组件需要被缓存时，该name字段的值必须要和导入组件中的name的值相同，不然无法被缓存
        path: '',
        component: ()=>import('@/views/borrower/BookList'),
        meta:{
          title: '查找书籍',
          icon: 'el-icon-xjh-query',
          cached: true,
        }
      }
    ]
  },
  {
    path: '/order',
    component: Layout,
    meta:{
      roles: ['borrower'],
      title: '订单管理',
      icon: 'el-icon-xjh-order'
    },
    children: [
      {
        name: 'BookBorrowList',
        path: 'bookBorrow',
        component: ()=>import("@/views/borrower/BookBorrowList"),
        meta:{
          title: '借阅详情',
          icon: 'el-icon-xjh-borrow'
        }
      },
      {
        name: 'BookReserveList',
        path: 'bookReserve',
        component: ()=>import("@/views/borrower/BookReserveList"),
        meta:{
          title: '预约详情',
          icon: 'el-icon-xjh-reserve'
        }
      }
    ]
  },
    // 图书管理员相关部分
  {
    path: '/manageBorrower',
    component: Layout,
    meta:{
      roles: ['admin'],
    },
    children: [
      {
        name: 'ManageBorrower',
        path: '',
        component: ()=>import('@/views/admin/ManageBorrower'),
        meta:{
          title: '借阅者管理',
          icon: 'el-icon-xjh-borrowerManage'
        }
      }
    ]
  },
  {
    path: '/manageBook',
    component: Layout,
    meta:{
      roles: ['admin'],
      title: '图书管理',
      icon: 'el-icon-xjh-bookManage'
    },
    children: [
      {
        name: 'AllBook',
        path: 'manageBook',
        component: ()=>import('@/views/admin/AllBook'),
        meta:{
          title: '全部图书',
          icon: 'el-icon-xjh-query',
          cached: true
        }
      },
      {
        name: 'UpdateBookInfo',
        path: 'update/:bookId',
        component: ()=>import('@/views/admin/UpdateBookInfo'),
        hidden: true,
        meta:{
          title: '修改图书信息',
          showInTag: false,
        }
      },
      {
        name: 'AddBook',
        path: 'addBook',
        component: ()=>import('@/views/admin/AddBook'),
        meta:{
          title: '添加图书',
          icon: 'el-icon-xjh-add'
        }
      },
      {
        name: 'BatchAddBook',
        path: 'batchAdd',
        component: ()=>import('@/views/admin/BatchAddBook'),
        meta:{
          title: '批量导入图书',
          icon: 'el-icon-xjh-batchAdd'
        }
      },
      {
        name: 'BookOutStockList',
        path: 'bookOutStock',
        component: () => import('@/views/admin/BookOutStockList'),
        meta: {
          title: '图书出库记录',
          icon: 'el-icon-xjh-out-stock'
        }
      }
    ]
  },
  {
    path: '/ManageBookBorrow',
    component: Layout,
    meta:{
      roles: ['admin'],
    },
    children: [
      {
        path: '',
        name: 'ManageBookBorrow',
        component: ()=>import('@/views/admin/ManageBookBorrow'),
        meta:{
          title: '图书借阅管理',
          icon: 'el-icon-xjh-borrow'
        }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes: constantRouter,
})

export default router
