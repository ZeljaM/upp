import React from 'react';
import { Carousel, Card, Button } from 'antd';
import styled from 'styled-components';
import LeftBar from '../components/LeftBar';

const contentStyle = {
  height: '160px',
  color: '#fff',
  lineHeight: '160px',
  textAlign: 'center',
  background: '#364d79',
};

const CardWrapper = styled.div`
  .ant-card-body {
    padding: 0px;
  }
`;

const ButtonWrapper = styled.div`
  display: flex;
  justify-content: space-between;
  .ant-btn {
    width: 50%;
  }
`;

const TaskCard = () => {
  return (
    <>
      <CardWrapper>
        <Card
          hoverable
          style={{ width: 240 }}
          cover={<img alt="example" src="../img/book.jpg" />}
        >
            <Carousel afterChange={() => null}>
              <div>
                <h3 style={contentStyle}>1</h3>
              </div>
              <div>
                <h3 style={contentStyle}>2</h3>
              </div>
              <div>
                <h3 style={contentStyle}>3</h3>
              </div>
              <div>
                <h3 style={contentStyle}>4</h3>
              </div>
            </Carousel>
            <ButtonWrapper>
              <Button type="primary" danger>
                Primary
              </Button>
              <Button type="primary">
                Primary
              </Button>
            </ButtonWrapper>
        </Card>
      </CardWrapper>
      <LeftBar />
    </>
  );
}

export default TaskCard
