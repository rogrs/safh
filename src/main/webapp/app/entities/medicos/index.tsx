import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Medicos from './medicos';
import MedicosDetail from './medicos-detail';
import MedicosUpdate from './medicos-update';
import MedicosDeleteDialog from './medicos-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MedicosUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MedicosUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MedicosDetail} />
      <ErrorBoundaryRoute path={match.url} component={Medicos} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={MedicosDeleteDialog} />
  </>
);

export default Routes;
