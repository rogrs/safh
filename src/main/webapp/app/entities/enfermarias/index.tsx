import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Enfermarias from './enfermarias';
import EnfermariasDetail from './enfermarias-detail';
import EnfermariasUpdate from './enfermarias-update';
import EnfermariasDeleteDialog from './enfermarias-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={EnfermariasUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={EnfermariasUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={EnfermariasDetail} />
      <ErrorBoundaryRoute path={match.url} component={Enfermarias} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={EnfermariasDeleteDialog} />
  </>
);

export default Routes;
