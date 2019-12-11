import { Moment } from 'moment';
import { IInternacoesDetalhes } from 'app/shared/model/internacoes-detalhes.model';
import { IPacientes } from 'app/shared/model/pacientes.model';
import { IClinicas } from 'app/shared/model/clinicas.model';
import { IMedicos } from 'app/shared/model/medicos.model';

export interface IInternacoes {
  id?: string;
  dataInternacao?: Moment;
  descricao?: string;
  internacoesDetalhes?: IInternacoesDetalhes[];
  pacientes?: IPacientes;
  clinicas?: IClinicas;
  medicos?: IMedicos;
}

export const defaultValue: Readonly<IInternacoes> = {};
