import React from 'react';
import { Form, Input, Button, Checkbox, Select } from 'antd';
import isEmpty from 'lodash/isEmpty';

import { FormContainer } from '../components/styledForm';

const layout = {
  labelCol: {
    span: 8,
  },
  wrapperCol: {
    span: 16,
  },
};

const Registration = ({ onFinish = () => { }, form, fields, responseData }) => {
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
    console.log('responseData', responseData);
    if(!isEmpty(responseData.errors))
      Object.keys(responseData.errors).map(key => (
        form.setFields([
          {
            name: key,
            errors: ["The field already exists"],
          },
        ])
      ));
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
              {
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
              {'password' in field.properties ? <Input.Password /> : field.typeName === 'boolean' ? <Checkbox onChange={(e) => checkboxField(e.target.checked)}/> : field.defaultValue ? 
                <Select>
                  {field.defaultValue.split(";").map(value => (
                    <Select.Option value={value} key={value}>
                      {value}
                    </Select.Option>
                  ))}  
                </Select> : <Input />}
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