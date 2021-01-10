import React from 'react';
import './Comments.css';
import { Button } from 'antd';
import { BackwardOutlined } from '@ant-design/icons';

 
const Comments = ({ comments, displayComments }) => {
  return (
    <div>
      <Button icon={<BackwardOutlined />} onClick={() => displayComments(false)}>
        Back
      </Button>
      {comments.map(comment => {
        return (
          <div class="container">
            <img src="../img/comment.jpg" alt="Avatar" />
            <b>{comment}</b>
            <span class="time-right">11:00</span>
          </div>
        );
      })}
    </ div>
  );
};

export default Comments;
