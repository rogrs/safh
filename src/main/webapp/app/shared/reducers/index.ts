import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import clinicas, {
  ClinicasState
} from 'app/entities/clinicas/clinicas.reducer';
// prettier-ignore
import dietas, {
  DietasState
} from 'app/entities/dietas/dietas.reducer';
// prettier-ignore
import enfermarias, {
  EnfermariasState
} from 'app/entities/enfermarias/enfermarias.reducer';
// prettier-ignore
import especialidades, {
  EspecialidadesState
} from 'app/entities/especialidades/especialidades.reducer';
// prettier-ignore
import fabricantes, {
  FabricantesState
} from 'app/entities/fabricantes/fabricantes.reducer';
// prettier-ignore
import internacoes, {
  InternacoesState
} from 'app/entities/internacoes/internacoes.reducer';
// prettier-ignore
import internacoesDetalhes, {
  InternacoesDetalhesState
} from 'app/entities/internacoes-detalhes/internacoes-detalhes.reducer';
// prettier-ignore
import leitos, {
  LeitosState
} from 'app/entities/leitos/leitos.reducer';
// prettier-ignore
import medicamentos, {
  MedicamentosState
} from 'app/entities/medicamentos/medicamentos.reducer';
// prettier-ignore
import medicos, {
  MedicosState
} from 'app/entities/medicos/medicos.reducer';
// prettier-ignore
import pacientes, {
  PacientesState
} from 'app/entities/pacientes/pacientes.reducer';
// prettier-ignore
import posologias, {
  PosologiasState
} from 'app/entities/posologias/posologias.reducer';
// prettier-ignore
import prescricoes, {
  PrescricoesState
} from 'app/entities/prescricoes/prescricoes.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly clinicas: ClinicasState;
  readonly dietas: DietasState;
  readonly enfermarias: EnfermariasState;
  readonly especialidades: EspecialidadesState;
  readonly fabricantes: FabricantesState;
  readonly internacoes: InternacoesState;
  readonly internacoesDetalhes: InternacoesDetalhesState;
  readonly leitos: LeitosState;
  readonly medicamentos: MedicamentosState;
  readonly medicos: MedicosState;
  readonly pacientes: PacientesState;
  readonly posologias: PosologiasState;
  readonly prescricoes: PrescricoesState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  clinicas,
  dietas,
  enfermarias,
  especialidades,
  fabricantes,
  internacoes,
  internacoesDetalhes,
  leitos,
  medicamentos,
  medicos,
  pacientes,
  posologias,
  prescricoes,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar
});

export default rootReducer;
