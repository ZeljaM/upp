import React from 'react';
import { Form, notification } from 'antd';
import { useHistory } from 'react-router-dom';
import { useAsync } from 'react-async';

import { Container } from '../components/styledForm';
import Login from '../forms/login';
import NavBar from '../components/NavBar';

import { post, get } from '../services/api';
import { REGISTRATION_START_URL } from '../constants/url';
import { responseOk } from '../utils/responseOk';

// import { withNoAuth } from '../hoc/withNoAuth';

const startProcessRegistration = async () => {
  try {
    // const requests = [BTC_FORM_URL, PAYPAL_FORM_URL].map((url) =>
    //   get(url, authToken)
    // );

    const response = await get(REGISTRATION_START_URL);

    if (responseOk(response)) {
      const result = await response.json();
      api.success({
        placement: 'topRight',
        message: 'Login success'
      });
      return {};
    }
  } catch (error) {
    console.error(error);
  }
  return {};
};

const RegistrationContainer = () => {
  const [api, context] = notification.useNotification();

  const [form] = Form.useForm();

  const history = useHistory();

  console.log('logg')

  const { data } = useAsync({
    promiseFn: startProcessRegistration,
  });

  console.log('data', data);
  const onFinish = async values => {
    const response = await post(LOGIN_URL, values);

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

// export default withNoAuth(RegistrationContainer);
export default RegistrationContainer;