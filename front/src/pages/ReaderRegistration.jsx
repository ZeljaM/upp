import React from 'react';
import { Form, notification } from 'antd';
import { useHistory } from 'react-router-dom';
import { useAsync } from 'react-async';
import get from 'lodash/get';
import isEmpty from 'lodash/isEmpty';

import { Container } from '../components/styledForm';
import Registration from '../forms/DynamicallyRegistration';
import LeftBar from '../components/LeftBar';

import { Post, Get } from '../services/api';
import { REGISTRATION_START_URL, REGISTRATION_NEXT_URL } from '../constants/url';
import { responseOk } from '../utils/responseOk';
import { withNoAuth } from '../hoc/withNoAuth';

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

const ReaderRegistrationContainer = () => {
  const [api, context] = notification.useNotification();
  const [responseData, setResponseData] = React.useState({});
  const [fields, setFields] = React.useState(get(data, 'fields', []));

  const [form] = Form.useForm();

  const history = useHistory();

  const { data, isLoading } = useAsync({
    promiseFn: startProcessRegistration,
    api,
    onResolve: (data) => { setFields(get(data, 'fields', [])); setResponseData(data)}
  });

  const onFinish = async values => {
    if(values.genres) values = {...values, genres: values.genres.join(';')}
    if(values.genresBeta) values = {...values, genresBeta: values.genresBeta.join(';')}

    console.log(values);
    const response = await Post(REGISTRATION_NEXT_URL, {formKey: responseData.formDataKey, task: responseData.task, process: responseData.process, fields: values});

    if (responseOk(response)) {
      const result = await response.json();
      console.log(result);
      if (!isEmpty(result.errors)) {
        setResponseData(result);
        setFields(get(result, 'fields', []));
        api.error({
          placement: 'topRight',
          message: 'Wrongly entered values'
        })
        return;
      } 
      if (result.success) {
        api.success({
          placement: 'topRight',
          message: result.message
        });
        setResponseData({});
        history.push("/");
        return;
      }
      setResponseData(result);
      setFields(get(result, 'fields', []))
      api.success({
        placement: 'topRight',
        message: 'The second step of registering a beta reader'
      });
      return;
    }

    api.error({
      placement: 'topRight',
      message: 'Registration error'
    })
  };

  return <Container>{context}
            <Registration responseData={responseData} form={form} onFinish={onFinish} fields={fields} isLoading={isLoading} />
            <LeftBar />
          </Container>;
};

export default withNoAuth(ReaderRegistrationContainer);