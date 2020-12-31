import React from 'react';
import 'antd/dist/antd.css';

import Home from './pages/Home';
import Login from './pages/Login';
import Registration from './pages/Registration';
import TaskCard from './pages/TaskCard';


import { Route, BrowserRouter as Router, Switch } from 'react-router-dom';

const App = () => {
  /** `collapsed` side of the menu */
  const [collapsed, updateCollapsed] = React.useState(false);

  return (
      <Router>
        <Switch>
          <Route path="/tasks">
            <TaskCard />
          </Route>
          <Route path="/login">
            <Login />
          </Route>
          <Route path="/registration">
            <Registration />
          </Route>
          <Route path="/">
            <Home />
          </Route>
        </Switch>
      </Router>
  );
};

export default App;
