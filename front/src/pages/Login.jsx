import React from 'react';
import { Form, notification } from 'antd';
import { useHistory } from 'react-router-dom';

import { Container } from '../components/styledForm';
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

  console.log('logg')

  const onFinish = async values => {
    setIsLoading(true);
    console.log(values)
    const response = await Post(LOGIN_URL, values);

    if (responseOk(response)) {
      const result = await response.json();
      api.success({
        placement: 'topRight',
        message: 'Login success'
      });
      localStorage.setItem('access_token', result.accessToken);
      setTimeout(() => {
        history.push('/');
      }, 1000)
      setIsLoading(false);
      return;
    }

    api.error({
      placement: 'topRight',
      message: 'Invalid credentials'
    })
    setIsLoading(false);
    return;
  };

  return <Container>{context}
            <Login form={form} onFinish={onFinish} isLoading={isLoading} />
            <LeftBar />
          </Container>;
};

export default withNoAuth(LoginContainer);