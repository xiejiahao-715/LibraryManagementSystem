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
