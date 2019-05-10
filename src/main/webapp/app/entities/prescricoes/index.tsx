import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Prescricoes from './prescricoes';
import PrescricoesDetail from './prescricoes-detail';
import PrescricoesUpdate from './prescricoes-update';
import PrescricoesDeleteDialog from './prescricoes-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PrescricoesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PrescricoesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PrescricoesDetail} />
      <ErrorBoundaryRoute path={match.url} component={Prescricoes} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={PrescricoesDeleteDialog} />
  </>
);

export default Routes;
