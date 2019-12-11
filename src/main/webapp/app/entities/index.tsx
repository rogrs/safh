import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Clinicas from './clinicas';
import Dietas from './dietas';
import Enfermarias from './enfermarias';
import Especialidades from './especialidades';
import Fabricantes from './fabricantes';
import Internacoes from './internacoes';
import InternacoesDetalhes from './internacoes-detalhes';
import Leitos from './leitos';
import Medicamentos from './medicamentos';
import Medicos from './medicos';
import Pacientes from './pacientes';
import Posologias from './posologias';
import Prescricoes from './prescricoes';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}clinicas`} component={Clinicas} />
      <ErrorBoundaryRoute path={`${match.url}dietas`} component={Dietas} />
      <ErrorBoundaryRoute path={`${match.url}enfermarias`} component={Enfermarias} />
      <ErrorBoundaryRoute path={`${match.url}especialidades`} component={Especialidades} />
      <ErrorBoundaryRoute path={`${match.url}fabricantes`} component={Fabricantes} />
      <ErrorBoundaryRoute path={`${match.url}internacoes`} component={Internacoes} />
      <ErrorBoundaryRoute path={`${match.url}internacoes-detalhes`} component={InternacoesDetalhes} />
      <ErrorBoundaryRoute path={`${match.url}leitos`} component={Leitos} />
      <ErrorBoundaryRoute path={`${match.url}medicamentos`} component={Medicamentos} />
      <ErrorBoundaryRoute path={`${match.url}medicos`} component={Medicos} />
      <ErrorBoundaryRoute path={`${match.url}pacientes`} component={Pacientes} />
      <ErrorBoundaryRoute path={`${match.url}posologias`} component={Posologias} />
      <ErrorBoundaryRoute path={`${match.url}prescricoes`} component={Prescricoes} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
