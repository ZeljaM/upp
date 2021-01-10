import React from 'react';
import { Form, notification, Spin } from 'antd';
import { useHistory } from 'react-router-dom';

import Login from '../forms/login';
import LeftBar from '../components/LeftBar';

import { Post } from '../services/api';
import { LOGIN_URL } from '../constants/url';
import { responseOk } from '../utils/responseOk';
import { withNoAuth } from '../hoc/withNoAuth';


const LoginContainer = () => {
  const [api, context] = notification.useNotification();

  const [isLoading, setIsLoading] = React.useState(false);

  const [form] = Form.useForm();

  const history = useHistory();

  const onFinish = async values => {
    setIsLoading(true);
    const response = await Post(LOGIN_URL, values);

    if (responseOk(response)) {
      const result = await response.json();
      api.success({
        placement: 'topRight',
        message: 'Login success'
      });
      localStorage.setItem('access_token', result.accessToken);
      localStorage.setItem('role', result.role);

      setTimeout(() => {
        history.push('/tasks');
      }, 1000)
      return;
    }

    api.error({
      placement: 'topRight',
      message: 'Invalid credentials'
    })
    setIsLoading(false);
    return;
  };

  return <>{context}
            {isLoading ? <Spin size="large"/> : <Login form={form} onFinish={onFinish} isLoading={isLoading} />}
            <LeftBar />
          </>;
};

export default withNoAuth(LoginContainer);