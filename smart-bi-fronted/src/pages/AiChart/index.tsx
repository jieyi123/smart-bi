import React, { useState, useRef, useEffect } from 'react';
import {Layout, Input, Button, List, Avatar, Spin, Card, Space} from 'antd';
import ReactMarkdown from "react-markdown";
import {requestConfig} from "@/requestConfig";
import './index.css'


const { TextArea } = Input;
const { Content } = Layout;
const baseURL=requestConfig.baseURL;

const ChatBox = () => {
  const [messages, setMessages] = useState([]);
  const [currentMessage, setCurrentMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const messagesEndRef = useRef(null);

  const [text, setText] = useState('');
  const [answerText, setAnswerText] = useState('');
  const charIndexRef = useRef(0);
  const showCursorRef = useRef(true);

  const prevItemRef = useRef(null);
  const url="/public/ArtStation.ico";

  const updateCurrentMessage = (e) => {
    setCurrentMessage(e.target.value);
  };
  const formatText = (text) => {
    return text.replace(/\\n/g, '\n');
  };

  const runPrompt = () => {
    // console.log(answerText?answerText: "错误")
    setAnswerText('');
    charIndexRef.current = 0;
    setText('');
    const url=baseURL.substring(0,baseURL.lastIndexOf(':'))
    fetch(url+':8000/eb_stream', {
      method: 'post',
      headers: { 'Content-Type': 'text/plain' },
      body: JSON.stringify({ prompt: currentMessage }),
    })
      .then((response) => response.body)
      .then((body) => {
        const reader = body.getReader();
        const decoder = new TextDecoder();
        const read = () =>
          reader.read().then(({ done, value }) => {
            if (done) return;
            const data = decoder.decode(value, { stream: true });
            const result = data.substring(
              data.indexOf('"result":"') + 10,
              data.indexOf('","', data.indexOf('"result":"') + 9),
            );
            setText((prevText) => prevText + formatText(result));
            return read();
          });
        return read();
      })
      .catch((error) => {
        console.error('Error:', error);
      });
  };




  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };
  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  const sendMessage = () => {
    if (currentMessage) {
      // @ts-ignore
      setMessages([...messages, { text: currentMessage,type: 'user', sender: 'user', avatar:  <Avatar  src={'avatar.jpg'} /> , username: 'You',time: new Date().toLocaleString() }]);
      setCurrentMessage('');
      setLoading(true);
      runPrompt();
      // setMessages(m => [...m, { text: answerText, sender: 'other', avatar: <Avatar icon={<UserOutlined />} />, username: 'Answer' }]);
      setTimeout(() => {
        // @ts-ignore
        setMessages(m => [...m, { text: answerText, type: 'bot',sender: 'other', avatar: <Avatar src={'ChatGPT.ico'} />, username: 'Answer',time: new Date().toLocaleString() }]);
        setLoading(false);
      }, 2000);
    }
  };


  // const sendMessage = () => {
  //   if (currentMessage) {
  //
  //     setMessages([
  //       ...messages,
  //       { text: currentMessage, type: 'user',sender: 'user' }, // 用户消息
  //
  //     ]);
  //     runPrompt();
  //     setLoading(true)
  //     setTimeout(() => {
  //             // @ts-ignore
  //       setMessages([
  //         ...messages,
  //         { text: answerText, type: 'bot',sender: 'bot' }, // AI回复
  //       ]);
  //     }, 1000);
  //     setLoading(false);
  //     setCurrentMessage('');
  //   }
  // };

  useEffect(() => {
    if (answerText) {
      setMessages((prevMessages) => {
        const updatedMessages = [...prevMessages];
        const lastMessageIndex = updatedMessages.length - 1;
        if (lastMessageIndex >= 0 && updatedMessages[lastMessageIndex].type === 'bot') {
          updatedMessages[lastMessageIndex].text = answerText; // 更新AI回复的内容
        }
        return updatedMessages;
      });
    }
  }, [answerText]);

  const handleKeyPress = (e) => {
    if (e.key === 'Enter' && !e.shiftKey) {
      sendMessage();
      e.preventDefault();
    }
  };

  useEffect(() => {
    const type = () => {
      if (charIndexRef.current < text.length) {
        setAnswerText(text.substring(0, charIndexRef.current) + (showCursorRef.current ? '|' : ''));
        charIndexRef.current++;
      } else {
        setAnswerText(text);
      }
      showCursorRef.current = !showCursorRef.current;
    };

    const interval = setInterval(type, 1000 / 20);
    return () => clearInterval(interval);
  }, [text]);




  return (
    <Card style={{ height: '740px', padding: '24px', marginBottom: '-10px' }} bordered={false}>
      <Content style={{ maxHeight: 'calc(70vh - 60px)', overflow: 'auto', marginBottom: '-10px' }}>
        <List
          dataSource={messages}
          renderItem={(item) => (
            <List.Item
              style={{
                display: 'flex',
                // justifyContent: item.sender === 'user' ? 'flex-end' : 'flex-start',
                justifyContent: 'flex-start',
                marginBottom: '0px',
                borderBottom: 'none',
                flexDirection: item.sender === 'user' ? 'row-reverse' : 'row', // 根据发送者调整方向
              }}
            >
              <Avatar
                src={item.sender === 'user' ? 'avatar.jpg' : 'ChatGPT.ico'}
                style={{ alignSelf: 'flex-start' }}
              />
              <div
                style={{
                  alignSelf: item.sender === 'user' ? 'flex-end' : 'flex-start',
                  background: item.sender === 'user' ? '#e6f7ff' : '#f0f0f0',
                  padding: '10px',
                  borderRadius: '10px',
                  maxWidth: '80%',
                  marginLeft: item.sender === 'user' ? 'auto' : '8px',
                  marginRight: item.sender !== 'user' ? 'auto' : '8px',
                  display: 'flex',
                  flexDirection: 'column',
                  marginTop: '20px',
                }}
              >
                <span
                  style={{
                    fontSize: '10px',
                    color: '#999',
                    alignSelf: item.sender === 'user' ? 'flex-end' : 'flex-start',
                    marginBottom: '4px',
                  }}
                >
                  {item.time}
                </span>
                {/*<span style={{ fontSize: '12px', color: '#999', marginBottom: '4px' }}>*/}
                {/*  {item.username}*/}
                {/*</span>*/}
                <div style={{ whiteSpace: 'pre-wrap', overflowWrap: 'break-word' }}>
                  <ReactMarkdown>{item.text}</ReactMarkdown>
                </div>
              </div>
            </List.Item>
          )}
        />
        {loading && (
          <div style={{ textAlign: 'center', marginBottom: '16px' }}>
            <Spin />
          </div>
        )}
        <div ref={messagesEndRef} />
      </Content>
      <div
        style={{
          width: '100%',
          position: 'absolute',
          bottom: 0,
          left: 0,
          padding: '10px 24px',
        }}
      >
        <Space.Compact style={{ marginTop: '10px', display: 'flex' }}>
          <TextArea
            style={{ paddingRight: '80px', borderRadius: '10px', resize: 'none',width:'100%' }}
            rows={2}
            // value={currentMessage}
            // onChange={e => setCurrentMessage(e.target.value)}
            onKeyPress={handleKeyPress}
            value={currentMessage}
            onChange={updateCurrentMessage}
          />
          <Button
            type="primary"
            onClick={sendMessage}
            disabled={!currentMessage}
            style={{
              position: 'absolute', // 绝对定位
              top: '26px', // 顶部对齐
              right: '30px', // 右侧对齐
              height: '50%', // 与输入框同高
              zIndex: 10, // 确保按钮在文本框之上
              borderRadius: '5px',
            }}
          >
            发送
          </Button>
        </Space.Compact>
      </div>
    </Card>
  );
};

export default ChatBox;
