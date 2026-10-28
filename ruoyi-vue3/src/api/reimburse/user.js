import request from '@/utils/request'

export function getUserInfo(query) {
  return request({
    url: 'openApi/user',
    method: 'get',
    params: query
  })
}

export function getUserTravelBill(query) {
  return request({
    url: '/openApi/user/travel/bill',
    method: 'get',
    params: query
  })
}