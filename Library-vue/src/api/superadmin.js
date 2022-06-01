import request from "@/utils/request";

// 分页查询获取管理员信息
export function getPageAdminInfoApi(current,limit){
  return request({
    url: `/superadmin/adminManage/page/${current}/${limit}`,
    method: 'get'
  })
}

// 添加管理员
export function addAdminApi(form){
  return request({
    url: '/superadmin/adminManage/add',
    method: 'post',
    data: form
  })
}

// 统计图书的日借阅数量
export function getDailyBookBorrowNumApi(date,offsetDay){
  return request({
    url: '/superadmin/statistics/dailyBookBorrowNum',
    method: 'get',
    params:{
      date,offsetDay
    }
  })
}

// 统计每一种分类对应的图书数量
export function getBookNumOfTypeApi(){
  return request({
    url: '/superadmin/statistics/bookNumOfType',
    method: 'get'
  })
}
