import {getChartByAiAnswerGet} from "@/services/smart-bi-backend/chartController";

declare namespace API {
  type addUsingGETParams = {
    /** name */
    name?: string;
  };

  type BaseResponse = {
    code?: number;
    data?: Record<string, any>;
    message?: string;
  };

  type BaseResponseBiResponse = {
    code?: number;
    data?: BiResponse;
    message?: string;
  };

  type BaseResponseboolean = {
    code?: number;
    data?: boolean;
    message?: string;
  };

  type BaseResponseChart = {
    code?: number;
    data?: ChartVo;
    message?: string;
  };

  type BaseResponseListChart = {
    code?: number;
    data?: Chart[];
    message?: string;
  };

  type BaseResponseListUserVO = {
    code?: number;
    data?: UserVO[];
    message?: string;
  };

  type BaseResponselong = {
    code?: number;
    data?: number;
    message?: string;
  };

  type BaseResponseString = {
    code?: number;
    data?: string;
    message?: string;
  };

  type BaseResponsePageChart = {
    code?: number;
    data?: PageChart;
    message?: string;
  };

  type BaseResponsePageUserVO = {
    code?: number;
    data?: PageUserVO;
    message?: string;
  };

  type BaseResponsestring = {
    code?: number;
    data?: string;
    message?: string;
  };

  type BaseResponseUser = {
    code?: number;
    data?: User;
    message?: string;
  };

  type BaseResponseUserVO = {
    code?: number;
    data?: UserVO;
    message?: string;
  };

  type BiResponse = {
    chartId?: number;
    genChart?: string;
    genResult?: string;
  };

  type Chart = {
    chartData?: string;
    chartType?: string;
    createTime?: string;
    execMessage?: string;
    genChart?: string;
    genResult?: string;
    goal?: string;
    id?: string;
    isDelete?: number;
    name?: string;
    status?: string;
    updateTime?: string;
    userId?: number;
  };

  type ChartVo = {
    chartData?: string;
    chartType?: string;
    createTime?: string;
    execMessage?: string;
    genChart?: string;
    genResult?: string;
    goal?: string;
    id?: string;
    isDelete?: number;
    name?: string;
    status?: string;
    updateTime?: string;
    userId?: number;
    userAccount?: string;
  };

  type ChartAddRequest = {
    chartData?: string;
    chartType?: string;
    goal?: string;
    name?: string;
  };

  type ChartQueryRequest = {
    chartData?: string;
    chartType?: string;
    current?: number;
    genChart?: string;
    genResult?: string;
    goal?: string;
    id?: number;
    name?: string;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    userId?: number;
  };

  type ChartUpdateRequest = {
    chartData?: string;
    chartType?: string;
    genChart?: string;
    genResult?: string;
    goal?: string;
    id?: number;
    name?: string;
  };

  type DeleteRequest = {
    id?: number;
  };

  type getCaptchaUsingGETParams = {
    /** phone */
    phone: string;
  };

  type getChartAsyncMqUsingPOSTParams = {
    chartType?: string;
    goal?: string;
    name?: string;
  };

  type getChartAsyncUsingPOSTParams = {
    chartType?: string;
    goal?: string;
    name?: string;
  };

  type getChartByIdUsingGETParams = {
    /** id */
    id?: number;
  };
  type getChartByAiAnswerGetParams = {
    /** answer */
    question?: string;
  };

  type getChartUsingPOSTParams = {
    chartType?: string;
    goal?: string;
    name?: string;
  };

  type getUserByIdUsingGETParams = {
    /** id */
    id?: number;
  };

  type listUserByPageUsingGETParams = {
    current?: number;
    email?: string;
    endTime?: string;
    gender?: number;
    id?: number;
    pageSize?: number;
    phone?: string;
    sortField?: string;
    sortOrder?: string;
    startTime?: string;
    updateTime?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userRole?: string;
  };

  type listUserUsingGETParams = {
    current?: number;
    email?: string;
    endTime?: string;
    gender?: number;
    id?: number;
    pageSize?: number;
    phone?: string;
    sortField?: string;
    sortOrder?: string;
    startTime?: string;
    updateTime?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userRole?: string;
  };

  type ModelAndView = {
    empty?: boolean;
    model?: Record<string, any>;
    modelMap?: Record<string, any>;
    reference?: boolean;
    status?:
      | 'ACCEPTED'
      | 'ALREADY_REPORTED'
      | 'BAD_GATEWAY'
      | 'BAD_REQUEST'
      | 'BANDWIDTH_LIMIT_EXCEEDED'
      | 'CHECKPOINT'
      | 'CONFLICT'
      | 'CONTINUE'
      | 'CREATED'
      | 'DESTINATION_LOCKED'
      | 'EXPECTATION_FAILED'
      | 'FAILED_DEPENDENCY'
      | 'FORBIDDEN'
      | 'FOUND'
      | 'GATEWAY_TIMEOUT'
      | 'GONE'
      | 'HTTP_VERSION_NOT_SUPPORTED'
      | 'IM_USED'
      | 'INSUFFICIENT_SPACE_ON_RESOURCE'
      | 'INSUFFICIENT_STORAGE'
      | 'INTERNAL_SERVER_ERROR'
      | 'I_AM_A_TEAPOT'
      | 'LENGTH_REQUIRED'
      | 'LOCKED'
      | 'LOOP_DETECTED'
      | 'METHOD_FAILURE'
      | 'METHOD_NOT_ALLOWED'
      | 'MOVED_PERMANENTLY'
      | 'MOVED_TEMPORARILY'
      | 'MULTIPLE_CHOICES'
      | 'MULTI_STATUS'
      | 'NETWORK_AUTHENTICATION_REQUIRED'
      | 'NON_AUTHORITATIVE_INFORMATION'
      | 'NOT_ACCEPTABLE'
      | 'NOT_EXTENDED'
      | 'NOT_FOUND'
      | 'NOT_IMPLEMENTED'
      | 'NOT_MODIFIED'
      | 'NO_CONTENT'
      | 'OK'
      | 'PARTIAL_CONTENT'
      | 'PAYLOAD_TOO_LARGE'
      | 'PAYMENT_REQUIRED'
      | 'PERMANENT_REDIRECT'
      | 'PRECONDITION_FAILED'
      | 'PRECONDITION_REQUIRED'
      | 'PROCESSING'
      | 'PROXY_AUTHENTICATION_REQUIRED'
      | 'REQUESTED_RANGE_NOT_SATISFIABLE'
      | 'REQUEST_ENTITY_TOO_LARGE'
      | 'REQUEST_HEADER_FIELDS_TOO_LARGE'
      | 'REQUEST_TIMEOUT'
      | 'REQUEST_URI_TOO_LONG'
      | 'RESET_CONTENT'
      | 'SEE_OTHER'
      | 'SERVICE_UNAVAILABLE'
      | 'SWITCHING_PROTOCOLS'
      | 'TEMPORARY_REDIRECT'
      | 'TOO_EARLY'
      | 'TOO_MANY_REQUESTS'
      | 'UNAUTHORIZED'
      | 'UNAVAILABLE_FOR_LEGAL_REASONS'
      | 'UNPROCESSABLE_ENTITY'
      | 'UNSUPPORTED_MEDIA_TYPE'
      | 'UPGRADE_REQUIRED'
      | 'URI_TOO_LONG'
      | 'USE_PROXY'
      | 'VARIANT_ALSO_NEGOTIATES';
    view?: View;
    viewName?: string;
  };

  type OrderItem = {
    asc?: boolean;
    column?: string;
  };

  type PageChart = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: Chart[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type PageUserVO = {
    countId?: string;
    current?: number;
    maxLimit?: number;
    optimizeCountSql?: boolean;
    orders?: OrderItem[];
    pages?: number;
    records?: UserVO[];
    searchCount?: boolean;
    size?: number;
    total?: number;
  };

  type RetrievePasswordRequest = {
    /** 确认密码 */
    checkPassword?: string;
    /** 手机号 */
    phone?: string;
    /** 用户密码 */
    userPassword?: string;
    /** 验证码 */
    verifyCode?: string;
  };

  type uploadFileUsingPOSTParams = {
    biz?: string;
  };

  type User = {
    createTime?: string;
    email?: string;
    gender?: number;
    id?: number;
    isDelete?: number;
    phone?: string;
    updateTime?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userPassword?: string;
    userRole?: string;
  };

  type UserAddRequest = {
    email?: string;
    gender?: number;
    phone?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userPassword?: string;
    userRole?: string;
  };

  type UserLoginRequest = {
    captcha?: string;
    phone?: string;
    type?: string;
    userAccount?: string;
    userPassword?: string;
  };

  type UserPasswordRequest = {
    confirmPassword?: string;
    id?: number;
    newPassword?: string;
    oldPassword?: string;
  };

  type UserRegisterRequest = {
    /** 确认密码 */
    checkPassword?: string;
    /** 手机号 */
    phone?: string;
    /** 用户账户 */
    userAccount?: string;
    /** 用户密码 */
    userPassword?: string;
    /** 验证码 */
    verifyCode?: string;
  };

  type UserUpdateRequest = {
    email?: string;
    gender?: number;
    id?: number;
    phone?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userPassword?: string;
    userRole?: string;
  };

  type UserVO = {
    createTime?: string;
    email?: string;
    gender?: number;
    id?: number;
    phone?: string;
    updateTime?: string;
    userAccount?: string;
    userAvatar?: string;
    userName?: string;
    userRole?: string;
  };

  type verifyCaptureUsingGETParams = {
    /** getParams */
    getParams: Record<string, any>;
  };

  type View = {
    contentType?: string;
  };
}
