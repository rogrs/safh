import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Especialidades from './especialidades';
import EspecialidadesDetail from './especialidades-detail';
import EspecialidadesUpdate from './especialidades-update';
import EspecialidadesDeleteDialog from './especialidades-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EspecialidadesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EspecialidadesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EspecialidadesDetail} />
      <ErrorBoundaryRoute path={match.url} component={Especialidades} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={EspecialidadesDeleteDialog} />
  </>
);

export default Routes;
