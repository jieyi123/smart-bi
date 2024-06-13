import {deleteChartUsingPost, getChartByIdUsingGet} from '@/services/smart-bi-backend/chartController';
import { PageContainer } from '@ant-design/pro-layout';
import ProTable, { ProColumns } from '@ant-design/pro-table';
import {Badge, Button, Card, Descriptions, Divider, message, Modal} from 'antd';
import React, { FC, useEffect, useState } from 'react';
import { useParams } from 'react-router';
import ReactECharts from "echarts-for-react";
import {history} from "@@/core/history";



const PAGE_NAME_UPPER_CAMEL_CASE: FC = () => {
  const params = useParams();
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<API.ChartVo>();

  const renderContent = (value: any, _: any, index: any) => {
    const obj: {
      children: any;
      props: { colSpan?: number };
    } = {
      children: value,
      props: {},
    };
    return obj;
  };


  const loadData = async () => {
    // 检查动态路由参数是否存在
    if (!params.id) {
      message.error('参数不存在');
      return;
    }
    setLoading(true);
    try {
      // 发起请求获取图表信息，接受一个包含 id 参数的对象作为参数
      const res = await getChartByIdUsingGet({
        id: params.id,
      });
      // 将获取到的接口信息设置到 data 状态中
      setData(res.data);
    } catch (error: any) {
      // 请求失败处理
      message.error('请求失败，' + error.message);
    }
    // 请求完成，设置 loading 状态为 false，表示请求结束，可以停止加载状态的显示
    setLoading(false);
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleDeleteChart= async (id)=>{
    const res=await deleteChartUsingPost({
      id
    });
    if (res.code===0){
      message.success('删除成功');
      setTimeout(()=>{
        history.back();
      },1000)
    }else {
      message.error('删除失败');
    }
  }
const click=async (id)=>  {
  Modal.confirm({
    title: '确认删除',
    content: '你确定要删除这个图表吗？',
    okText: '确认',
    cancelText: '取消',
    onOk (){
      // 在这里调用实际的删除逻辑，例如更新状态或调用API删除后端数据
      handleDeleteChart(id);
    },
    onCancel() {
      console.log('取消删除操作');
      // 这里可以处理取消操作，如果需要的话
    },
  });
}
  return (
    <PageContainer>
      <Card>
        {data ? (
          <>
            <Descriptions
              title="图表信息"
              style={{ marginBottom: 32 }}
              extra={
                <Button onClick={() => click(data?.id)} type="primary" danger>
                  删除图表
                </Button>
              }
            >
              <Descriptions.Item label="图表名称">{data.name}</Descriptions.Item> <br />
              <Descriptions.Item label="图表类型">{data.chartType}</Descriptions.Item> <br />
              <Descriptions.Item label="分析目标">{data.goal}</Descriptions.Item> <br />
              <Descriptions.Item label="创建人">{data.userAccount}</Descriptions.Item> <br/>
              <Descriptions.Item label="创建时间">
                {data.updateTime?.substring(0, data.updateTime?.indexOf('T'))}
              </Descriptions.Item>{' '}
            </Descriptions>
            <h3 style={{ fontWeight: 'bold', marginBottom: '15px', marginTop: '-10px' }}>
              可视化图表
            </h3>
            {data?.genChart ? (
              <ReactECharts option={data?.genChart && JSON.parse(data?.genChart)} />
            ) : (
              <p style={{ color: '#fa0000' }}>图表生成错误</p>
            )}

            <Divider style={{ marginBottom: 10, marginTop: -15 }} />
            <Descriptions title="分析结论" style={{ marginBottom: 32 }}>
              {<div style={{ whiteSpace: 'pre-wrap' }}>{data?.genResult}</div>}
            </Descriptions>
            <Divider style={{ marginTop: '-10px' }} />
          </>
        ) : (
          <>图表不存在</>
        )}
      </Card>
    </PageContainer>
  );
};

export default PAGE_NAME_UPPER_CAMEL_CASE;
