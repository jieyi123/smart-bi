export default [
  {
    path: '/user',
    layout: false,
    routes: [
      { name: '登录', path: '/user/login', component: './User/Login' },
      { name: '注册', path: '/user/register', component: './User/Register' },
      { name: '忘记密码', path: '/user/retrieve', component: './User/RetrievePassword' },
    ],
  },
  {path: '/',redirect: '/add_chart'},
  {
    path: '/add_chart',
    name: '智能分析',
    icon: 'barChart',
    component: './AddChart'
  },
  {
    path: '/my_chart',
    name: '我的图表',
    icon: 'pieChart',
    component: './MyChart'
  },
  {
    path: '/admin',
    name: '管理页',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      { path: '/admin', redirect: '/admin/user-manager' },
      { path: '/admin/user-manager', icon: 'pieChart',name: '用户管理', component: './Admin/UserManager' },
    ],
  },
  { name: '个人设置', icon: 'user', path: '/user-settings', component: './UserSettings' },
  { path: '/', redirect: '/welcome' },
  { path: '*', layout: false, component: './404' },
];

