import React from 'react';
import { Form, Input, Button, Checkbox, Select, Spin, message, Upload, Radio } from 'antd';
import isEmpty from 'lodash/isEmpty';
import { UploadOutlined } from '@ant-design/icons';
import get from 'lodash/get';

import { FormContainer } from '../components/styledForm';

const layout = {
  labelCol: {
    span: 8,
  },
  wrapperCol: {
    span: 16,
  },
};

const Registration = ({ onFinish = () => { }, form, fields, responseData, isLoading, files, comments, displayComments }) => {
  const [value, setValue] = React.useState('');
  
  const checkboxField = (value) => {
    form.setFields([
      {
        name: 'beta',
        value: String(value),
      },
      {
        name: 'plagiarismVote',
        value: String(value),
      },
      {
        name: 'readReject',
        value: String(value),
      },
      {
        name: 'plagiarism',
        value: String(value),
      },
      {
        name: 'moderatorOk',
        value: String(value),
      },
      {
        name: 'moderatorRepeatOk',
        value: String(value),
      },
      {
        name: 'lectorOk',
        value: String(value),
      },
    ])
  }

  
  React.useEffect(() => {
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
      {
        name: 'plagiarismVote',
        value: 'false',
      },
      {
        name: 'readReject',
        value: 'false',
      },
      {
        name: 'plagiarism',
        value: 'false',
      },
      {
        name: 'oneFile',
        value: false,
      },
      {
        name: 'moderatorOk',
        value: 'false',
      },
      {
        name: 'lectorOk',
        value: 'false',
      },
      {
        name: 'moderatorRepeatOk',
        value: 'false',
      },
    ])
  });

  const props = {
    name: 'file',
    action: 'https://www.mocky.io/v2/5cc8019d300000980a055e76',
    headers: {
      authorization: 'authorization-text',
    },
    onChange(info) {
      if (info.file.status !== 'uploading') {
      }
      if (info.file.status === 'done') {
        message.success(`${info.file.name} file uploaded successfully`);
      } else if (info.file.status === 'error') {
        message.error(`${info.file.name} file upload failed.`);
      }
      console.log(info);
    },
  };

  const onChange2 = e => {
    setValue(e.target.value);
  }

  const onClickUpload = value => {
    console.log(value);
    form.setFields([
      {
        name: 'oneFile',
        value: get(value, ['properties', 'min'], 0) === get(value, ['properties', 'max'], 1) ? true : false,
      },
    ])
  }

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
        {!isEmpty(comments) && <p onClick={() => displayComments(true)} className="comments">View comments</p>}
        {files && files.length && 
          <Form.Item
            className="hide-star"
            label={"Download file"} 
          >
              <Select onChange={value => window.open("data:application/octet-stream;charset=utf-16le;base64,"+value.split('index')[0])}>
                {files.map((file, index) => (
                  <Select.Option value={file + "index" + index} key={file + index}>
                    {"File" + index}
                  </Select.Option>
                ))}
              </Select>
          </Form.Item>}
          <div style={{display: 'none'}}>
            <Form.Item
              className="hide-star"
              name="oneFile" 
            />
          </div>
        {fields.map(field => {
          return (
            <>
              <Form.Item 
              label={field.label} 
              name={field.id}
              rules={!field.defaultValue && field.typeName !== "customfile" ? [
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
                  <Select mode={get(field, ['properties', 'min'], 0) === get(field, ['properties', 'max'], 1) ? "single" : "multiple"} >
                    {field.defaultValue.split(";").map(value => (
                      <Select.Option value={value} key={value} >
                        {value}
                      </Select.Option>
                    ))}  
                  </Select> : 
                  field.typeName === "customfile" ? 
                  <Upload {...props} accept="application/pdf" onClick={() => onClickUpload(field)}>
                    <Button icon={<UploadOutlined />}>Click to Upload</Button>
                  </Upload> : 
                    field.typeName === "enum" ? 
                      <Radio.Group optionType="button" buttonStyle="solid" options={Object.keys(field.type.values).map(value =>  ({label: value, value }))} onChange={onChange2} value={value} /> :
                      <Input />}
              </Form.Item>
            </>
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