import React from 'react';
import { notification, Spin, Form } from 'antd';
import moment from 'moment';
import get from 'lodash/get';
import { useAsync } from 'react-async';

import LeftBar from '../components/LeftBar';
import { Get, Post, PostFile } from '../services/api';
import { responseOk } from '../utils/responseOk';
import Table from '../components/Table';
import { GET_TASKS_OF_USER, FORM_FOR_TASK, REGISTRATION_WRITER_UPLOAD_FILES_URL } from '../constants/url';
import { withAuth } from '../hoc/withAuth';
import Registration from '../forms/DynamicallyRegistration';


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
  const [existForm, setExistForm] = React.useState(false);
  const [responseData, setResponseData] = React.useState({});
  const [fields, setFields] = React.useState([]);
  const [task, setTask] = React.useState('');
  const [process, setProcess] = React.useState('');

  const [form] = Form.useForm();

  const { data = [], isLoading } = useAsync({
    promiseFn: getTasksOfUser,
    api,
    authToken,
  });
  
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
     setTask(task);
     setProcess(process);
    const response = await Post(FORM_FOR_TASK, {task, process}, authToken );

    if (responseOk(response)) {
      const result = await response.json();
      if(get(result, 'fields', []).length) {
        setExistForm(true);
        setFields(get(result, 'fields', []));
        setResponseData(result);
        api.success({
          placement: 'topRight',
          message: 'Fetch form success'
        });
        return;
      }
    }

    api.error({
      placement: 'topRight',
      message: 'Invalid credentials'
    })
    return;
  }

  const onFinish = async (values) => {
    if(values.files.fileList.length >= 2) {
      const data = new FormData();

      values.files.fileList.forEach(file => {
        console.log('usao', file.originFileObj);
        data.append('files', file.originFileObj, file.originFileObj.name);
      });
      data.append('task', task);
      data.append('process', process);

      console.log('formData', data);
      const response = await PostFile(REGISTRATION_WRITER_UPLOAD_FILES_URL, data, authToken );

      if (responseOk(response)) {
        api.success({
          placement: 'topRight',
          message: 'Files uploaded successfully'
        });
        window.location.reload();
        return;
      }

      api.error({
        placement: 'topRight',
        message: 'Files upload failed '
      })
      return;
    }
    api.warning({
      placement: 'topRight',
      message: 'Must enter more then 2 files'
    })
  }

  return <>{context}
            {isLoading ? <Spin size="large"/> :
            <>
            {existForm ? <Registration responseData={responseData} form={form} onFinish={onFinish} fields={fields} isLoading={isLoading} />
                        : <Table columns={columns} dataSource={dataSource} onTaskClick={onTaskClick} />}
            <LeftBar />
            </>}
          </>;
};

export default withAuth(TasksTable);