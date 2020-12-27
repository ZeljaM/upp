import React from 'react';
import { Form, Input, Button, Checkbox } from 'antd';
import { Link } from 'react-router-dom';

import { FormContainer } from '../components/styledForm';

const layout = {
  labelCol: {
    span: 8,
  },
  wrapperCol: {
    span: 16,
  },
};

const Registration = ({ onFinish = () => { }, form, fields }) => {
  const checkboxField = (value) => {
    console.log(value);
    form.setFields([
      {
        name: 'beta',
        value: String(value),
      },
    ])
  }

  
  React.useEffect(() => {
    form.setFields([
      {
        name: 'beta',
        value: 'false',
      },
    ])
  });

  return (
    <FormContainer>
      <h2 className="title">Registration</h2>
      <Form
        {...layout}
        name="basic"
        initialValues={{
          remember: true,
        }}
        onFinish={onFinish}
        form={form}
      >
        {fields.map(field => {
          return (
            <Form.Item 
            label={field.label} 
            name={field.id}
            rules={[
              field.typeName !== 'boolean' && {
                required: 'required' in field.properties,
                message: `Please input ${field.id}`,
              },
              {
                min: parseInt(field.properties.minlength || field.properties.min),
                message: `Enter more than ${field.properties.minlength || field.properties.min}`,
              },
              {
                max: parseInt(field.properties.maxlength || field.properties.max),
                message: `Enter less than ${field.properties.maxlength || field.properties.max}`,
              },
              'email' in field.properties && {
                type: 'email',
              },
            ]}
            >
              {'password' in field.properties ? <Input.Password /> : field.typeName === 'boolean' ? <Checkbox onChange={(e) => checkboxField(e.target.checked)}/> : <Input />}
            </Form.Item>
          );
        })}
        <Button type="primary" block htmlType="submit" className="form-button">
          Registration
        </Button>
      </Form>
    </FormContainer>
  );
};

export default Registration;