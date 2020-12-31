import React from 'react';
import { Table } from 'antd';

const TableContainer = ({ dataSource, columns, onTaskClick}) => {
  return <Table dataSource={dataSource}  columns={columns} bordered onRow={(record) => {
    return {
      onClick: () => {onTaskClick(record)}, 
    };
  }}/>
};

export default TableContainer;