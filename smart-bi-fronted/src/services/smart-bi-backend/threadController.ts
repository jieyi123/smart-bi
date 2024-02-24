// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** add GET /api/thread/add */
export async function addUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.addUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<any>('/api/thread/add', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** get GET /api/thread/get */
export async function getUsingGet(options?: { [key: string]: any }) {
  return request<string>('/api/thread/get', {
    method: 'GET',
    ...(options || {}),
  });
}
