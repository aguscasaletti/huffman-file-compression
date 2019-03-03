import React from 'react';
import { Switch, Route } from 'react-router';
import { BrowserRouter as Router } from "react-router-dom";

import routes from './constants/routes';
import Home from './components/Home';
import Compressor from './components/Compressor';

export default () => (
  <React.Fragment>
    <Router>
      <Switch>
        <Route path={routes.COMPRESSOR} component={Compressor} />
        <Route path={routes.HOME} component={Home} />
      </Switch>
    </Router>
  </React.Fragment>
);
