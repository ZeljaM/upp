import React from 'react';
import styled from 'styled-components';

import './home.css';
import LeftBar from '../components/LeftBar';

const Container = styled.div`
  background: rgb(2, 0, 36);
  background: linear-gradient(
    90deg,
    rgba(2, 0, 36, 1) 0%,
    rgba(9, 9, 121, 0.7570378493194152) 31%,
    rgba(0, 212, 255, 1) 100%
  );
  width: 100%;
  height: ${window.innerHeight}px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const Home = () => {
  return (
    <Container>
      <LeftBar />
    </Container>
  );
};

export default Home;
