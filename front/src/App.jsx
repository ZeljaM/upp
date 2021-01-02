import React from 'react';
import 'antd/dist/antd.css';

import { Container } from './components/styledForm';
import Home from './pages/Home';
import Login from './pages/Login';
import ReaderRegistration from './pages/ReaderRegistration';
import WriterRegistration from './pages/WriterRegistration';
import TasksTable from './pages/TasksTable';


import { Route, BrowserRouter as Router, Switch } from 'react-router-dom';

const App = () => {

  return (
    <Container>
      <Router>
        <Switch>
          <Route path="/tasks">
            <TasksTable />
          </Route>
          <Route path="/login">
            <Login />
          </Route>
          <Route path="/reader/registration">
            <ReaderRegistration />
          </Route>
          <Route path="/writer/registration">
            <WriterRegistration />
          </Route>
          <Route path="/">
            <Home />
          </Route>
        </Switch>
      </Router>
    </Container>
  );
};

export default App;
