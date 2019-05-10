import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Medicamentos from './medicamentos';
import MedicamentosDetail from './medicamentos-detail';
import MedicamentosUpdate from './medicamentos-update';
import MedicamentosDeleteDialog from './medicamentos-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MedicamentosUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MedicamentosUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MedicamentosDetail} />
      <ErrorBoundaryRoute path={match.url} component={Medicamentos} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={MedicamentosDeleteDialog} />
  </>
);

export default Routes;
