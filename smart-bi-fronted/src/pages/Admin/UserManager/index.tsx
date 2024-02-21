
import { PlusOutlined } from '@ant-design/icons';
import {ActionType, ProColumns, ProDescriptionsItemProps,} from '@ant-design/pro-components';
import {
  PageContainer,
  ProDescriptions,
  ProTable,
} from '@ant-design/pro-components';
import '@umijs/max';
import {Button, Drawer, Image, message, Space, Table} from 'antd';
import React, { useRef, useState } from 'react';
import UpdateForm from './components/UpdateForm';
import type {SortOrder} from "antd/lib/table/interface";
import {
  addUserUsingPost,
  deleteUserUsingPost,
  listUserByPageUsingGet,
  updateUserUsingPost
} from "@/services/smart-bi-backend/userController";
import {FormValueType} from "@/pages/Admin/UserManager/components/CreateForm";
import CreateForm from "@/pages/Admin/UserManager/components/CreateForm";

export const waitTimePromise = async (time: number = 100) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(true);
    }, time);
  });
};

export const waitTime = async (time: number = 100) => {
  await waitTimePromise(time);
};

const TableList: React.FC = () => {

  /**
   * @en-US Pop-up window of new window
   * @zh-CN 新建窗口的弹窗
   *  */
  const [createModalOpen, handleModalOpen] = useState<boolean>(false);
  /**
   * @en-US The pop-up window of the distribution update window
   * @zh-CN 分布更新窗口的弹窗
   * */
  const [updateModalOpen, handleUpdateModalOpen] = useState<boolean>(false);
  const [showDetail, setShowDetail] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.UserVO>();

  /**
   * @en-US Add node
   * @zh-CN 添加接口
   * @param fields
   */
  const handleAdd = async (fields: API.UserVO) => {
    const hide = message.loading('正在添加');
    try {
      await addUserUsingPost({
        ...fields,
      });
      hide();
      message.success('添加成功');
      handleModalOpen(false)
      //自动更新表单
      actionRef.current?.reload();
      return true;
    } catch (error) {
      hide();
      message.error('Adding failed, please try again!');
      return false;
    }
  };

  /**
   * @en-US Update interface
   * @zh-CN 更新接口
   * @param fields
   */
  const handleUpdate = async (fields: FormValueType) => {
    //如果没有选中，直接返回
    if(!currentRow){
      return ;
    }
    const hide = message.loading('修改中');
    try {
      await updateUserUsingPost({
        id:currentRow.id,
        ...fields
      });
      hide();
      message.success('修改成功');
      //自动更新表单
      actionRef.current?.reload();
      return true;
    } catch (error) {
      hide();
      message.error('Configuration failed, please try again!');
      return false;
    }
  };

  /**
   *  Delete node
   * @zh-CN 删除接口
   * @param selectedRows
   */
  const handleRemove = async (selectedRows: API.UserVO) => {
    const hide = message.loading('正在删除');
    if (!selectedRows) return true;
    try {
      await deleteUserUsingPost({
        id:selectedRows.id
      });
      hide();
      message.success('删除成功');
      //自动更新表单
      actionRef.current?.reload();
      return true;
    } catch (error) {
      hide();
      message.error('Delete failed, please try again');
      return false;
    }
  };



  const columns: ProColumns<API.UserVO>[] = [
    {
      title: '编号',
      dataIndex: 'id',
      valueType: 'indexBorder',
      search:false,
      width: 48,
    },
    {
      title: '昵称',
      dataIndex: 'userName',
      //copyable: true, //是否可复制
      ellipsis: true, //是否允许缩略
      formItemProps: {
        rules:[{
          required:true,
        }]
      }
    },
    {
      title: '账号',
      dataIndex: 'userAccount',
      formItemProps: {
        rules:[{
          required:true,
        }]
      }
    },
    {
      title: '头像',
      dataIndex: 'avatarUrl',
      render: (_,record)=>(
        <div>
          <Image src={record.userAvatar} width={80} />
        </div>
      ),
      search: false
    },
    {
      title: '性别',
      dataIndex: 'gender',
      valueType: 'select',
      valueEnum: {
        0: {text: '女'},
        1: {text: '男'}
      },
      formItemProps: {
        rules:[{
          required:true,
        }]
      }
    },
    {
      title: '电话',
      dataIndex: 'phone',
      formItemProps: {
        rules:[{
          required:true,

        },
          {
            pattern: /^1\d{10}$/,
            message: '不合法的手机号！',
          }]
      }
    },
    {
      title: '邮件',
      dataIndex: 'email',
      formItemProps: {
        rules:[{
          required:true,
          pattern: /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/,
          message: '邮箱不合法'
        }]
      }

    },
    {
      title: '角色',
      dataIndex: 'userRole',
      valueType: 'select',
      valueEnum: {
        'user': {text: '普通用户',status: 'Default'},
        'admin': {
          text: '管理员',
          status: 'Success'
        }
      },
      formItemProps: {
        rules:[{
          required:true,
        }]
      }
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'dateTime',
      hideInSearch: true,
      hideInForm:true,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      valueType: 'dateRange',
      hideInTable: true,
      hideInForm: true,
      search: {
        transform: (value) => {
          return {
            startTime: value[0],
            endTime: value[1],
          };
        },
      },
    },
    {
      title: '修改时间',
      dataIndex: 'updateTime',
      valueType: 'dateTime',
      editable: false,
      search: false,
      hideInForm:true
    },

    {
      title: '操作',
      valueType: 'option',
      key: 'option',
      render: (text, record, _, action) => [
        <a
          key="config"
          onClick={() => {
            handleUpdateModalOpen(true);
            setCurrentRow(record);
          }}
        >
          修改
        </a>,
        <a key="config" onClick={()=>{
          handleRemove(record);
        }}>
          删除
        </a>,
      ],
    },
  ];
  return (
    <PageContainer>
      <ProTable<API.UserVO, API.PageParams>
        headerTitle={'用户列表'}
        actionRef={actionRef}
        rowKey="key"
        pagination={{
          pageSize: 5,
        }}
        search={{
          labelWidth: 'auto',
        }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              handleModalOpen(true);
            }}
          >
            <PlusOutlined /> 新建
          </Button>,
        ]}
        request={ async (params: U & {
          pageSize?: number;
          current?: number;
          keyword?: string;
        }, sort: Record<string, SortOrder>, filter: Record<string, (string | number)[] | null>)=>{
          const res=await listUserByPageUsingGet({
            ...params
          })
          if (res.data){
            return{
              data: res.data.records || [],
              success: true,
              total: res.data.total,
            }}
        }
        }
        columns={columns}
        //选择框
        rowSelection={{
          // 自定义选择项参考: https://ant.design/components/table-cn/#components-table-demo-row-selection-custom
          // 注释该行则默认不显示下拉选项
          selections: [Table.SELECTION_ALL, Table.SELECTION_INVERT],
        }}
        tableAlertRender={({
                             selectedRowKeys,
                             selectedRows,
                             onCleanSelected,
                           }) => {
          console.log(selectedRowKeys, selectedRows);
          return (
            <Space size={24}>
            <span>
              已选 {selectedRowKeys.length} 项
              <a style={{ marginInlineStart: 8 }} onClick={onCleanSelected}>
                取消选择
              </a>
            </span>
            </Space>
          );
        }}
        tableAlertOptionRender={() => {
          return (
            <Space size={16}>
              <a>批量删除</a>
              <a>导出数据</a>
            </Space>
          );
        }}

      />

      <UpdateForm
        columns={columns}
        onSubmit={async (value) => {
          const success = await handleUpdate(value);
          if (success) {
            handleUpdateModalOpen(false);
            setCurrentRow(undefined);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
        onCancel={() => {
          handleUpdateModalOpen(false);
          if (!showDetail) {
            setCurrentRow(undefined);
          }
        }}
        visible={updateModalOpen}
        values={currentRow || {}}
      />

      {/* 创建一个CreateModal组件，用于在点击新增按钮时弹出 */}
      <CreateForm
        columns={columns}
        // 当取消按钮被点击时,设置更新模态框为false以隐藏模态窗口
        onCancel={() => {
          handleModalOpen(false);
        }}
        // 当用户点击提交按钮之后，调用handleAdd函数处理提交的数据，去请求后端添加数据(这里的报错不用管,可能里面组件的属性和外层的不一致)
        onSubmit={(values) => {
          handleAdd(values);
        }}
        // 根据更新窗口的值决定模态窗口是否显示
        visible={createModalOpen}
      />

      <Drawer
        width={600}
        open={showDetail}
        onClose={() => {
          setCurrentRow(undefined);
          setShowDetail(false);
        }}
        closable={false}
      >
        {currentRow?.id && (
          <ProDescriptions<API.UserVO>
            title={currentRow?.id}
            request={async () => ({
              data: currentRow || {},
            })}
            params={{
              id: currentRow?.id,
            }}
            columns={columns as ProDescriptionsItemProps<API.UserVO>[]}
          />
        )}
      </Drawer>

    </PageContainer>
  );
};
export default TableList;
