import React, { useState} from 'react';
import {Form, List, Modal, Input, message} from 'antd';
import {history, useModel} from "@@/exports";
import {updatePasswordUsingPost, userLogoutUsingPost} from "@/services/smart-bi-backend/userController";
import {stringify} from "querystring";
import {waitTime} from "@/pages/Admin/UserManager";

type Unpacked<T> = T extends (infer U)[] ? U : T;

const passwordStrength = {
  strong: <span className="strong">强</span>,
  medium: <span className="medium">中</span>,
  weak: <span className="weak">弱 Weak</span>,
};

const loginOut = async () => {
  await userLogoutUsingPost();
  const { search, pathname } = window.location;
  const urlParams = new URL(window.location.href).searchParams;
  /** 此方法会跳转到 redirect 参数所在的位置 */
  const redirect = urlParams.get('redirect');
  // Note: There may be security issues, please note
  await waitTime(1000)
  if (window.location.pathname !== '/user/login' && !redirect) {
    history.replace({
      pathname: '/user/login',
      search: stringify({
        redirect: pathname + search,
      }),
    });
  }
};


//修改密码模态框
const ModifyPasswordModal = ({ visible, onCancel, onOk }) => {
  const [form] = Form.useForm();

  const { initialState } = useModel('@@initialState');
  const { loginUser } = initialState || {};

  const handleOk = () => {
    form.validateFields().then((values) =>  {
        // 处理密码修改逻辑
     updatePasswordUsingPost({
        id:loginUser?.id,
        ...values
      }).then((response)=>{
       message.success("密码修改成功")
       onOk();
       loginOut();
      }).catch((error)=>{
        message.error(error.message)
      })

    });
  };

  return (
    <Modal
      title="修改密码"
      visible={visible}
      onCancel={onCancel}
      onOk={handleOk}
    >
      <Form form={form} layout='vertical'>
        <Form.Item
          label="原密码"
          name="oldPassword"
          rules={[{ required: true, message: '请输入原密码' }]}
        >
          <Input.Password />
        </Form.Item>
        <Form.Item
          label="新密码"
          name="newPassword"
          rules={[
            { required: true, message: '请输入新密码' },
            {
              min:8,
              message:'密码长度不能小于8位',
              type:'string'
            }
          ]}
        >
          <Input.Password />
        </Form.Item>
        <Form.Item
          label="确认密码"
          name="confirmPassword"
          dependencies={['newPassword']}
          rules={[
            { required: true, message: '请确认新密码' },
            {
              min:8,
              message:'密码长度不能小于8位',
              type:'string'
            },
            ({ getFieldValue }) => ({
              validator(_, value) {
                if (!value || getFieldValue('newPassword') === value) {
                  return Promise.resolve();
                }
                return Promise.reject('两次密码输入不一致');
              },
            }),
          ]}
        >
          <Input.Password />
        </Form.Item>
      </Form>
    </Modal>
  );
};




const SecurityView: React.FC = () => {
  const { initialState } = useModel('@@initialState');
  const { loginUser } = initialState || {};

  const [modalVisible, setModalVisible] = useState(false);

  const handleModifyClick = () => {
    setModalVisible(true);
  };

  const handleModalCancel = () => {
    setModalVisible(false);
  };

  const handleModalOk = () => {
    // 如果需要的话，执行其他逻辑
    setModalVisible(false);
  };

  const getData = () => [
    {
      title: '账户密码',
      description: (
        <>
          当前密码强度：
          {passwordStrength.strong}
        </>
      ),
      actions: [
        <a key="Modify" onClick={handleModifyClick}>修改</a>
      ],
    },
    {
      title: '绑定邮箱',
      description: `已绑定邮箱：${loginUser?.email}`,
      actions: [
        <a key="Modify">修改</a>
      ],
    },
    {
      title: '密保问题',
      description: '未设置密保问题，密保问题可有效保护账户安全',
      actions: [<a key="Set">设置</a>],
    },

    {
      title: '密保手机',
      description: `已绑定手机：${loginUser?.phone}`,
      actions: [<a key="Modify">修改</a>],
    },
    {
      title: 'MFA 设备',
      description: '未绑定 MFA 设备，绑定后，可以进行二次确认',
      actions: [<a key="bind">绑定</a>],
    },
  ];

  const data = getData();
  return (
    <>
      <List<Unpacked<typeof data>>
        itemLayout="horizontal"
        dataSource={data}
        renderItem={(item) => (
          <List.Item actions={item.actions}>
            <List.Item.Meta title={item.title} description={item.description} />
          </List.Item>
        )}
      />
      <ModifyPasswordModal
        visible={modalVisible}
        onCancel={handleModalCancel}
        onOk={handleModalOk}
      />
    </>
  );
};

export default SecurityView;
