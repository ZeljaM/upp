import React from 'react';
import { Form, notification } from 'antd';
import { useHistory } from 'react-router-dom';
import { useAsync } from 'react-async';
import get from 'lodash/get';

import { Container } from '../components/styledForm';
import Registration from '../forms/DynamicallyRegistration';
import NavBar from '../components/NavBar';

import { Post, Get } from '../services/api';
import { REGISTRATION_START_URL, REGISTRATION_NEXT_URL } from '../constants/url';
import { responseOk } from '../utils/responseOk';

// import { withNoAuth } from '../hoc/withNoAuth';

const startProcessRegistration = async ({ api }) => {
  try {
    const response = await Get(REGISTRATION_START_URL);

    if (responseOk(response)) {
      const result = await response.json();
      api.success({
        placement: 'topRight',
        message: 'Registration fetch fields success'
      });
      return result;
    }
  } catch (error) {
    api.error({
      placement: 'topRight',
      message: 'Registration fetch fields failes'
    });
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
    api,
  });

  console.log('data', data);
  const onFinish = async values => {
    console.log(values);
    const response = await Post(REGISTRATION_NEXT_URL, {formKey: data.formDataKey, task: data.task, process: data.process, fields: values});

    if (responseOk(response)) {
      const result = await response.json();
      api.success({
        placement: 'topRight',
        message: 'Registration success'
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
            <Registration form={form} onFinish={onFinish} fields={get(data, 'fields', [])} />
            <NavBar />
          </Container>;
};

// export default withNoAuth(RegistrationContainer);
export default RegistrationContainer;