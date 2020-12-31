import React from 'react';
import { Form, Input, Button, Checkbox, Select, Spin } from 'antd';
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

const Registration = ({ onFinish = () => { }, form, fields, responseData, isLoading }) => {
  const checkboxField = (value) => {
    console.log(value);
    form.setFields([
      {
        name: 'beta',
        value: String(value),
      },
    ])
  }

  // const multiSelect = (name, value) => {
  //   console.log('values', value)
  //   console.log(form);
  //   form.setFields([
  //     {
  //       name,
  //       value: value.join(';')
  //     },
  //   ])
  // }
  
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
    isLoading ? <Spin size="large"/> :
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
            rules={!field.defaultValue ? [
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
            ] : null}
            >
              {'password' in field.properties ? <Input.Password /> : field.typeName === 'boolean' ? <Checkbox onChange={(e) => checkboxField(e.target.checked)}/> : field.defaultValue ? 
                <Select mode="multiple" >
                  {field.defaultValue.split(";").map(value => (
                    <Select.Option value={value} key={value} >
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