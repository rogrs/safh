import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Clinicas from './clinicas';
import ClinicasDetail from './clinicas-detail';
import ClinicasUpdate from './clinicas-update';
import ClinicasDeleteDialog from './clinicas-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ClinicasUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ClinicasUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ClinicasDetail} />
      <ErrorBoundaryRoute path={match.url} component={Clinicas} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={ClinicasDeleteDialog} />
  </>
);

export default Routes;
