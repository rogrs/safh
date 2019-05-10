import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Pacientes from './pacientes';
import PacientesDetail from './pacientes-detail';
import PacientesUpdate from './pacientes-update';
import PacientesDeleteDialog from './pacientes-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PacientesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PacientesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PacientesDetail} />
      <ErrorBoundaryRoute path={match.url} component={Pacientes} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PacientesDeleteDialog} />
  </>
);

export default Routes;
