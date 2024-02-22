// @ts-ignore
/* eslint-disable */
import { request } from '@umijs/max';

/** addUser POST /api/user/add */
export async function addUserUsingPost(body: API.UserAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponselong>('/api/user/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getCaptcha GET /api/user/captcha */
export async function getCaptchaUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getCaptchaUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponse>('/api/user/captcha', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** deleteUser POST /api/user/delete */
export async function deleteUserUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseboolean>('/api/user/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** getUserById GET /api/user/get */
export async function getUserByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getUserByIdUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseUserVO>('/api/user/get', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** getLoginUser GET /api/user/get/login */
export async function getLoginUserUsingGet(options?: { [key: string]: any }) {
  return request<API.BaseResponseUserVO>('/api/user/get/login', {
    method: 'GET',
    ...(options || {}),
  });
}

/** listUser GET /api/user/list */
export async function listUserUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listUserUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListUserVO>('/api/user/list', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** listUserByPage GET /api/user/list/page */
export async function listUserByPageUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listUserByPageUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageUserVO>('/api/user/list/page', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 用户登录 POST /api/user/login */
export async function userLoginUsingPost(
  body: API.UserLoginRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseUser>('/api/user/login', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 用户注销 POST /api/user/logout */
export async function userLogoutUsingPost(options?: { [key: string]: any }) {
  return request<API.BaseResponseboolean>('/api/user/logout', {
    method: 'POST',
    ...(options || {}),
  });
}

/** 用户注册 POST /api/user/register */
export async function userRegisterUsingPost(
  body: API.UserRegisterRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponselong>('/api/user/register', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** retrievePassword POST /api/user/retrievePassword */
export async function retrievePasswordUsingPost(
  body: API.RetrievePasswordRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponse>('/api/user/retrievePassword', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updateUser POST /api/user/update */
export async function updateUserUsingPost(
  body: API.UserUpdateRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseboolean>('/api/user/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** updatePassword POST /api/user/updatePassword */
export async function updatePasswordUsingPost(
  body: API.UserPasswordRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponse>('/api/user/updatePassword', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** verifyCapture GET /api/user/verifyCapture */
export async function verifyCaptureUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.verifyCaptureUsingGETParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponse>('/api/user/verifyCapture', {
    method: 'GET',
    params: {
      ...params,
      getParams: undefined,
      ...params['getParams'],
    },
    ...(options || {}),
  });
}
