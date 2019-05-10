import { IPacientes } from 'app/shared/model/pacientes.model';
import { IInternacoes } from 'app/shared/model/internacoes.model';

export interface IClinicas {
  id?: number;
  clinica?: string;
  descricao?: string;
  pacientes?: IPacientes[];
  internacoes?: IInternacoes[];
}

export const defaultValue: Readonly<IClinicas> = {};
