import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Internacoes from './internacoes';
import InternacoesDetail from './internacoes-detail';
import InternacoesUpdate from './internacoes-update';
import InternacoesDeleteDialog from './internacoes-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={InternacoesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={InternacoesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={InternacoesDetail} />
      <ErrorBoundaryRoute path={match.url} component={Internacoes} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={InternacoesDeleteDialog} />
  </>
);

export default Routes;
