import React from 'react';
import { Form, notification } from 'antd';
import { useHistory } from 'react-router-dom';

import { Container } from '../components/styledForm';
import Login from '../forms/login';
import NavBar from '../components/NavBar';

import { Post } from '../services/api';
import { LOGIN_URL } from '../constants/url';
import { responseOk } from '../utils/responseOk';

// import { withNoAuth } from '../hoc/withNoAuth';


const LoginContainer = () => {
  const [api, context] = notification.useNotification();

  const [form] = Form.useForm();

  const history = useHistory();

  console.log('logg')

  const onFinish = async values => {
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
      return;
    }

    api.error({
      placement: 'topRight',
      message: 'Invalid credentials'
    })
  };

  return <Container>{context}
            <Login form={form} onFinish={onFinish} />
            <NavBar />
          </Container>;
};

// export default withNoAuth(LoginContainer);
export default LoginContainer;