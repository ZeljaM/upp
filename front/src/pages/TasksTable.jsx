import React from 'react';
import { Form, notification, Spin } from 'antd';
import { useHistory } from 'react-router-dom';
import moment from 'moment';

import { Container } from '../components/styledForm';
import Login from '../forms/login';
import LeftBar from '../components/LeftBar';
import { useAsync } from 'react-async';

import { Get, Post } from '../services/api';
import { responseOk } from '../utils/responseOk';
import Table from '../components/Table';
import { GET_TASKS_OF_USER, FORM_FOR_TASK } from '../constants/url';
import { withAuth } from '../hoc/withAuth';

const getTasksOfUser = async ({ api, authToken }) => {
  try {
    const response = await Get(GET_TASKS_OF_USER, authToken);

    if (responseOk(response)) {
      const result = await response.json();
      api.success({
        placement: 'topRight',
        message: 'Fetch tasks success'
      });
      return result;
    }
  } catch (error) {
    api.error({
      placement: 'topRight',
      message: 'Fetch tasks failed'
    });
  }
  return [];
};

const TasksTable = () => {
  const authToken = localStorage.getItem('access_token');
  const [api, context] = notification.useNotification();

  const { data = [], isLoading } = useAsync({
    promiseFn: getTasksOfUser,
    api,
    authToken,
  });

  console.log('data', data)

  
  const dataSource = data.map((item, index) => {
      return ( 
        {
          key: index,
          name: 'a',
          createdAt: moment(item.createdAt).format('YYYY-MM-DD HH:mm:ss'),
          title: item.title,
          process: item.process,
          task: item.task, 
        }
      );
    });
  
  const columns = [
    {
      title: 'Serial number',
      key: 'index',
      render: (text, record, index) => index,
    },
    {
      title: 'Created At',
      dataIndex: 'createdAt',
      key: 'createdAt',
    },
    {
      title: 'Title',
      dataIndex: 'title',
      key: 'title',
    },
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
    },
  ];

  const onTaskClick = async (item) => {
     console.log(item);
     const { task, process } = item;
    const response = await Post(FORM_FOR_TASK, {task, process}, authToken );

    if (responseOk(response)) {
      const result = await response.json();
      api.success({
        placement: 'topRight',
        message: 'Login success'
      });
      return;
    }

    api.error({
      placement: 'topRight',
      message: 'Invalid credentials'
    })
    return;
  }

  return <Container>{context}
            {isLoading ? <Spin size="large"/> :
            <>
            <Table columns={columns} dataSource={dataSource} onTaskClick={onTaskClick} />
            <LeftBar />
            </>}
          </Container>;
};

export default withAuth(TasksTable);