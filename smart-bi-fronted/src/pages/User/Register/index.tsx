import { Footer } from '@/components';
import { getFakeCaptcha } from '@/services/ant-design-pro/login';
import '@/ct4.js';
import {
  LockOutlined,
  MobileOutlined,
  UserOutlined,
} from '@ant-design/icons';
import {
  LoginForm,
  ProFormCaptcha,
  ProFormCheckbox,
  ProFormText,
} from '@ant-design/pro-components';
import {Helmet, history, Link} from '@umijs/max';
import {message, Space, Divider} from 'antd';
import { createStyles } from 'antd-style';
import Settings from '../../../../config/defaultSettings';
import {userRegisterUsingPost} from "@/services/yiapi-backend/userController";
import {FormattedMessage} from "react-intl";
import {getVerifyCode} from "@/services/ant-design-pro/api";
import React from "react";
const useStyles = createStyles(({ token }) => {
  return {
    action: {
      marginLeft: '8px',
      color: 'rgba(0, 0, 0, 0.2)',
      fontSize: '24px',
      verticalAlign: 'middle',
      cursor: 'pointer',
      transition: 'color 0.3s',
      '&:hover': {
        color: token.colorPrimaryActive,
      },
    },
    lang: {
      width: 42,
      height: 42,
      lineHeight: '42px',
      position: 'fixed',
      right: 16,
      borderRadius: token.borderRadius,
      ':hover': {
        backgroundColor: token.colorBgTextHover,
      },
    },
    container: {
      display: 'flex',
      flexDirection: 'column',
      height: '100vh',
      overflow: 'auto',
      backgroundImage:
        "url('https://mdn.alipayobjects.com/yuyan_qk0oxh/afts/img/V-_oS6r-i7wAAAAAAAAAAAAAFl94AQBr')",
      backgroundSize: '100% 100%',
    },
  };
});
const Login: React.FC = () => {
  const { styles } = useStyles();
  const handleSubmit = async (values: API.UserRegisterRequest) => {
    //校验
    const {userPassword,checkPassword}=values;
    if (userPassword!==checkPassword){
      message.error('两次密码输入不一致');
      return;
    }
    try {
      // 注册
      const res = await userRegisterUsingPost({
        ...values,
      });
      if (res.code===0) {
        const defaultLoginSuccessMessage = '注册成功！';
        message.success(defaultLoginSuccessMessage);
        /** 此方法会跳转到 redirect 参数所在的位置  用来重定向到之前未登录时访问的页面*/
        if (!history) return;
        history.push({
          pathname: '/user/login',
        });
        return;
      }else {
        message.error(res.message);
      }
    } catch (error) {
      const defaultLoginFailureMessage = '注册失败，请重试！';
      console.log(error);
      message.error(defaultLoginFailureMessage);
    }
  };
  return (
    <div className={styles.container}>
      <Helmet>
        <title>
          {'注册'}- {Settings.title}
        </title>
      </Helmet>
      <div
        style={{
          flex: '1',
          padding: '32px 0',
        }}
      >
        <LoginForm
          submitter={{
            searchConfig:{
              submitText: '注册'
            }}
          }
          contentStyle={{
            minWidth: 280,
            maxWidth: '75vw',
          }}
          logo={<img alt="logo" src="/ArtStation.ico" />}
          title="智能BI平台"
          subTitle={'一站式管理接口'}
          initialValues={{
            autoLogin: true,
          }}
          onFinish={async (values) => {
            await handleSubmit(values as API.UserLoginRequest);
          }}
        >
            <>
              <ProFormText
                name="userAccount"
                fieldProps={{
                  size: 'large',
                  prefix: <UserOutlined />,
                }}
                placeholder={'用户名不少于4位'}
                rules={[
                  {
                    required: true,
                    message: '用户名是必填项！',
                  },
                  {
                    min:4,
                    message:'账号长度不能小于4位',
                    type:'string'
                  }
                ]}
              />
              <ProFormText.Password
                name="userPassword"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined />,
                }}
                placeholder={'密码最低8位'}
                rules={[
                  {
                    required: true,
                    message: '密码是必填项！',
                  },
                  {
                    min:8,
                    message:'密码长度不能小于8位',
                    type:'string'
                  }
                ]}
              />
              <ProFormText.Password
                name="checkPassword"
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined />,
                }}
                placeholder={'确认密码'}
                rules={[
                  {
                    required: true,
                    message: '确认密码是必填项！',
                  },
                  {
                    min:8,
                    message:'密码长度不能小于8位',
                    type:'string'
                  }
                ]}
              />
            </>
            <>
              <ProFormText
                fieldProps={{
                  size: 'large',
                  prefix: <MobileOutlined />,
                }}
                name="phone"
                placeholder={'请输入手机号'}
                rules={[
                  {
                    required: true,
                    message: '手机号是必填项！',
                  },
                  {
                    pattern: /^1\d{10}$/,
                    message: '不合法的手机号！',
                  },
                ]}
              />
              <ProFormCaptcha
                fieldProps={{
                  size: 'large',
                  prefix: <LockOutlined />,
                }}
                captchaProps={{
                  size: 'large',
                }}
                phoneName="phone"
                placeholder={'请输入验证码'}
                captchaTextRender={(timing, count) => {
                  if (timing) {
                    return `${count} ${'秒后重新获取'}`;
                  }
                  return '获取验证码';
                }}
                name="verifyCode"
                rules={[
                  {
                    required: true,
                    message: '验证码是必填项！',
                  },
                ]}
                onGetCaptcha={async (phone) => {
                  //校验验证码
                  // @ts-ignore
                  initAlicom4({
                    captchaId: "cf6ae8259eb05cfae171b2bd6d18bd62",
                    product: 'bind',
                    //protocol: 'http://' //部署在对应服务上可删除此配置
                  }, function (captcha:any) {
                    // captcha为验证码实例
                    captcha.onReady(function () {
                    }).onSuccess(async function () {
                      const result = captcha.getValidate();
                      //your code
                      const  res=await getVerifyCode(result);
                      try {
                        if (res.data?.result==='success') {
                          //验证成功
                          const result = await getFakeCaptcha({
                            phone,
                          });
                          if (!result) {
                            return;
                          }
                          message.success('获取验证码成功！');
                        }else {
                          message.error('验证失败');
                        }
                      } catch (error) {
                      }
                    }).onError(function () {
                      //your code
                    })
                    captcha.showCaptcha();
                  });
                }}
              />
            </>
          <div
            style={{
              marginBottom: 24,
            }}
          >
            <Space split={<Divider type="vertical"/>} size="middle">
              <ProFormCheckbox noStyle name="autoLogin">
                <FormattedMessage id="pages.login.rememberMe" defaultMessage="自动登录"/>
              </ProFormCheckbox>
              <Link to="/user/login">用户登录</Link>
              <Link to="/user/retrieve">忘记密码</Link>
            </Space>
          </div>
        </LoginForm>
      </div>
      <Footer />
    </div>
  );
};
export default Login;
