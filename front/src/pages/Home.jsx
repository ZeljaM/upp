import React from 'react';
import { Layout } from 'antd';
import {
  MenuUnfoldOutlined,
  MenuFoldOutlined,
  LogoutOutlined,
} from '@ant-design/icons';
import { Switch, Route, useHistory } from 'react-router-dom';
import styled from 'styled-components';
import NavBar from '../components/NavBar';
// import { withAuth } from '../hoc/withAuth';

import './home.css';
import LeftBar from '../components/LeftBar';

const { Header } = Layout;

const RightNav = styled.div`
  display: flex;
  align-items: center;
  flex-direction: row;
  margin-right: 20px;
`;

const Logout = styled.div`
  margin-left: 10px;
  &:hover {
    cursor: pointer;
  }
`;

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

  const toggle = () => updateCollapsed((state) => !state);

  /** `use` state hook for updating items in the shop */
  const [items, updateItems] = React.useState([]);


  const history = useHistory();

  // /** `logout` handler */
  // const logout = () => {
  //   localStorage.removeItem('access_token');
  //   updateCollapsed(() => []);
  //   history.push('/login');
  // };

  console.log('usaoooo')

  return (
    <Container>
      {/* <NavBar /> */}
      <LeftBar />
    </Container>
  );
};

// export default withAuth(Home);
export default Home;
