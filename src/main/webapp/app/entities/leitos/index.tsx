import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Leitos from './leitos';
import LeitosDetail from './leitos-detail';
import LeitosUpdate from './leitos-update';
import LeitosDeleteDialog from './leitos-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={LeitosUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={LeitosUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={LeitosDetail} />
      <ErrorBoundaryRoute path={match.url} component={Leitos} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={LeitosDeleteDialog} />
  </>
);

export default Routes;
