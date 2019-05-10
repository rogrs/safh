import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Dietas from './dietas';
import DietasDetail from './dietas-detail';
import DietasUpdate from './dietas-update';
import DietasDeleteDialog from './dietas-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={DietasUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={DietasUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={DietasDetail} />
      <ErrorBoundaryRoute path={match.url} component={Dietas} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={DietasDeleteDialog} />
  </>
);

export default Routes;
