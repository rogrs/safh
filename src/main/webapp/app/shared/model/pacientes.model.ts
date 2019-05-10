import { IInternacoes } from 'app/shared/model/internacoes.model';
import { IClinicas } from 'app/shared/model/clinicas.model';
import { IEnfermarias } from 'app/shared/model/enfermarias.model';
import { ILeitos } from 'app/shared/model/leitos.model';

export const enum Estados {
  AC = 'AC',
  AL = 'AL',
  AM = 'AM',
  AP = 'AP',
  BA = 'BA',
  CE = 'CE',
  DF = 'DF',
  ES = 'ES',
  GO = 'GO',
  MA = 'MA',
  MG = 'MG',
  MS = 'MS',
  MT = 'MT',
  PA = 'PA',
  PB = 'PB',
  PE = 'PE',
  PI = 'PI',
  PR = 'PR',
  RJ = 'RJ',
  RN = 'RN',
  RO = 'RO',
  RR = 'RR',
  RS = 'RS',
  SC = 'SC',
  SE = 'SE',
  SP = 'SP',
  TO = 'TO'
}

export interface IPacientes {
  id?: number;
  prontuario?: number;
  nome?: string;
  cpf?: string;
  email?: string;
  cep?: string;
  logradouro?: string;
  numero?: string;
  complemento?: string;
  bairro?: string;
  cidade?: string;
  uF?: Estados;
  internacoes?: IInternacoes[];
  clinicas?: IClinicas;
  enfermarias?: IEnfermarias;
  leitos?: ILeitos;
}

export const defaultValue: Readonly<IPacientes> = {};
