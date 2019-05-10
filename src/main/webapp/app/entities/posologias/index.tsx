import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Posologias from './posologias';
import PosologiasDetail from './posologias-detail';
import PosologiasUpdate from './posologias-update';
import PosologiasDeleteDialog from './posologias-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PosologiasUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PosologiasUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PosologiasDetail} />
      <ErrorBoundaryRoute path={match.url} component={Posologias} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PosologiasDeleteDialog} />
  </>
);

export default Routes;
