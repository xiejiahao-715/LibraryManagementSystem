import request from "@/utils/request";

// 分页查询获取借阅者信息
export function getPageBorrowerInfoApi(current,limit){
  return request({
    url: `/admin/borrowerManage/page/${current}/${limit}`,
    method: 'get'
  })
}

// 修改借阅的最大借阅资源数量
export function updateBorrowMaxNumApi(borrowerId,newValue){
  return request({
    url: `/admin/borrowerManage/maxBorrowNum/${borrowerId}`,
    method: 'put',
    params: {
      newValue: newValue
    }
  })
}
// 添加图书
export function addBookApi(form){
  return request({
    url: '/admin/bookManage/addBook',
    method: 'post',
    data: form
  })
}

// 修改图书信息
export function updateBookApi(form){
  return request({
    url:'/admin/bookManage/updateBook',
    method: 'put',
    data: form
  })
}

// 通过excel批量导入图书
export function batchAddBookApi(excelFile){
  const formDate = new FormData()
  formDate.append("file",excelFile)
  return request({
    url: '/admin/bookManage/batchAddBook',
    header:{
      'Content-Type': 'multipart/form-data'
    },
    data: formDate,
    method: 'post'
  })
}

// 添加图书库存
export function addBookStockApi(bookId,addNum){
  return request({
    url: '/admin/bookManage/addBookStock',
    params:{
      bookId,addNum
    },
    method: 'put'
  })
}

// 图书出库
export function bookOutStockApi(bookId,num){
  return request({
    url: '/admin/bookManage/bookOutStock',
    params:{
      bookId,num
    },
    method: 'post'
  })
}

// 获取图书出库数据
export function getPageBookOutStockInfoApi(current,limit){
  return request({
    url: `/admin/bookManage/bookOutStock/${current}/${limit}`,
    method: 'get'
  })
}

// 分页查询所有借阅信息
export function getPageBorrowBookDetailApi(current,limit){
  return request({
    url: `/admin/bookManage/bookBorrowDetail/${current}/${limit}`,
    method: 'get'
  })
}

// 分页查询所有的预约信息
export function getPageReserveBookDetailApi(current,limit){
  return request({
    url: `/admin/bookManage/bookReserveDetail/${current}/${limit}`,
    method: 'get'
  })
}

// 催还图书通知
export function recallBookReturnNotifyApi(borrowId){
  return request({
    url: `/admin/bookManage/callReturnBook/${borrowId}`,
    method: 'put'
  })
}

