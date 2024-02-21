// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** upload POST /api/upload */
export async function uploadUsingPost(file: File, options?: { [key: string]: any }) {
  const formData = new FormData();
  formData.append('file', file);
  return request<API.BaseResponsestring>('/api/upload', {
    method: 'POST',
    headers: {

    },
    data: formData,
    ...(options || {}),
  });
}
