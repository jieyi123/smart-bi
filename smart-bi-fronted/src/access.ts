/**
 * @see https://umijs.org/docs/max/access#access
 * 权限
 * */
export default function access(initialState: InitialState | undefined) {
  const { loginUser } = initialState ?? {};
  return {
    //可以自己定义 自定义权限
    canUser: loginUser,
    canAdmin: loginUser && loginUser.userRole === 'admin',
  };
}
