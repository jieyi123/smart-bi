import React from 'react';
import {UploadOutlined} from '@ant-design/icons';
import {Button, Upload, message,} from 'antd';
import ProForm, {
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-form';
import { useRequest } from '@umijs/max';

import styles from './BaseView.less';
import {getLoginUserUsingGet, getUserByIdUsingGet, updateUserUsingPost} from "@/services/smart-bi-backend/userController";
import {useModel} from "@@/exports";
import {uploadUsingPost} from "@/services/smart-bi-backend/fileUploadController";


// 头像组件 方便以后独立，增加裁剪之类的功能



const BaseView: React.FC = () => {
  const {  setInitialState } = useModel('@@initialState');
  const { data: currentUser, loading } = useRequest(
    async () => {
    return await  getLoginUserUsingGet();
  });

  const getAvatarURL = () => {
    if (currentUser) {
      if (currentUser.userAvatar) {
        return currentUser.userAvatar;
      }
      const url = 'https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png';
      return url;
    }
    return '';
  };

  // @ts-ignore
  const handleFileUpload = async ({ file }) => {
    try {
      // 创建 FormData 对象
      const formData = new FormData();
      formData.append('file', file);
      // 发送文件到后端
      const response = await uploadUsingPost(file);

      // 处理上传成功的逻辑
      if (response.data) {
        message.success('更新头像成功');
        await  updateUserUsingPost({
          userAvatar:response.data,
          id:currentUser?.id
        })

        const user=await getUserByIdUsingGet({
          id:currentUser?.id
        })
        setInitialState({
          loginUser:user.data
        });

        // 其他成功逻辑
      } else {
        message.error('更新头像失败');
        // 其他失败逻辑
      }
    } catch (error) {
      message.error('更新头像发生错误');
      // 其他错误处理逻辑
    }
  };

  const AvatarView = ({ avatar }: { avatar: string }) => (
    <>
      <div className={styles.avatar_title}>头像</div>
      <div className={styles.avatar}>
        <img src={avatar} alt="avatar" />
      </div>
      <Upload customRequest={({ file }) => handleFileUpload({ file })}
      >
        <div className={styles.button_view}>
          <Button type="primary" >
            <UploadOutlined />
            更换头像
          </Button>
        </div>
      </Upload>
    </>
  );

  return (
    <div className={styles.baseView}>
      {loading ? null : (
        <>
          <div className={styles.left}>
            <ProForm
              layout="vertical"
              onFinish={async (values) => {
                   const res=await updateUserUsingPost(values as API.UserUpdateRequest);
                   if (res.data) {
                     message.success('更新基本信息成功');
                     const userVo=await getUserByIdUsingGet({
                       ...currentUser
                     })
                     setInitialState({
                       loginUser:userVo.data
                     });
                   }
              }}
              submitter={{
                resetButtonProps: {
                  style: {
                    display: 'none',
                  },
                },
                submitButtonProps: {
                  children: '更新基本信息',
                },
              }}
              initialValues={{
                ...currentUser,
              }}
            >

              <ProFormText
                name="id"
                label="id"
                hidden={true}
              />
              <ProFormText
                width="md"
                name="userName"
                label="昵称"
                rules={[
                  {
                    required: true,
                    message: '请输入您的昵称!',
                  },
                ]}
              />
              <ProFormText
                width="md"
                name="userAccount"
                label="账号"
                rules={[
                  {
                    required: true,
                    message: '请输入您的账号!',
                  },
                ]}
              />
              <ProFormText
                width="md"
                name="email"
                label="邮箱"
                rules={[
                  {
                    required: true,
                    message: '请输入邮箱',
                  },
                  {
                    pattern: /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/ ,
                    message: '邮箱不合法！'
                  }
                ]}
              />
              <ProFormSelect
               name="gender"
               label="性别"
               valueEnum={{
                 0: {text: '女'},
                 1: {text: '男'}
               }}
               rules={[{ required: true, message: '请选择您的性别' }]}
              />

              <ProFormText
                name="address"
                label="个人地址"
                placeholder="个人地址"
              />
              <ProFormTextArea
                name="profile"
                label="个人简介"
                placeholder="个人简介"
              />

            </ProForm>
          </div>
          <div className={styles.right}>
            <AvatarView avatar={getAvatarURL()} />
          </div>
        </>
      )}
    </div>
  );
};

export default BaseView;
