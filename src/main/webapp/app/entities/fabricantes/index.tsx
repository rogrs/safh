import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Fabricantes from './fabricantes';
import FabricantesDetail from './fabricantes-detail';
import FabricantesUpdate from './fabricantes-update';
import FabricantesDeleteDialog from './fabricantes-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={FabricantesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={FabricantesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={FabricantesDetail} />
      <ErrorBoundaryRoute path={match.url} component={Fabricantes} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={FabricantesDeleteDialog} />
  </>
);

export default Routes;
