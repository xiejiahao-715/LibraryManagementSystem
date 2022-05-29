// 借阅者（普通用户）需要调用的接口
import request from "@/utils/request";

// 分页查询图书信息
export function getPageBookInfoApi(current,limit,queryForm){
  return request({
    url: `/user/book/page/${current}/${limit}`,
    method: 'post',
    headers: {
      'Content-Type': 'application/json;charset=UTF-8'
    },
    data: queryForm,
  })
}
// 获取图书的分类
export function getBookTypeApi(){
  return request({
    url: `/user/book/typeList`,
    method: 'get'
  })
}

// 根据图书id获取图书的详细信息
export function getBookInfoByIdApi(bookId){
  return request({
    url: '/user/book/info',
    params:{
      bookId: bookId
    },
    method: 'get'
  })
}
// 获取用户的借阅情况
export function getUserResourceApi(){
  return request({
    url: '/user/userManage/resource',
    method: 'get'
  })
}

// 借阅图书
export function borrowBookApi(borrowBookForm){
  return request({
    url: '/user/book/borrow',
    method: 'post',
    data: borrowBookForm
  })
}

// 预定图书
export function reserveBookApi(bookId){
  return request({
    url: `/user/book/reserve/${bookId}`,
    method: 'post'
  })
}

// 分页查询借阅信息
export function getPageBorrowBookInfoApi(current, limit){
  return request({
    url: `/user/book/borrow/page/${current}/${limit}`,
    method: 'get'
  })
}

// 分页查询预约信息
export function getPageReserveBookInfoApi(current,limit){
  return request({
    url: `/user/book/reserve/page/${current}/${limit}`,
    method: 'get'
  })
}

// 归还图书
export function returnBookApi(borrowId){
  return request({
    url: `/user/book/return/${borrowId}`,
    method: 'post'
  })
}

// 续借图书
export function renewBookApi(form){
  return request({
    url: '/user/book/renew',
    method: 'post',
    data: form
  })
}
