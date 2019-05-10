import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import InternacoesDetalhes from './internacoes-detalhes';
import InternacoesDetalhesDetail from './internacoes-detalhes-detail';
import InternacoesDetalhesUpdate from './internacoes-detalhes-update';
import InternacoesDetalhesDeleteDialog from './internacoes-detalhes-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={InternacoesDetalhesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={InternacoesDetalhesUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={InternacoesDetalhesDetail} />
      <ErrorBoundaryRoute path={match.url} component={InternacoesDetalhes} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={InternacoesDetalhesDeleteDialog} />
  </>
);

export default Routes;
