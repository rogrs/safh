import { IInternacoes } from 'app/shared/model/internacoes.model';
import { IEspecialidades } from 'app/shared/model/especialidades.model';

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

export interface IMedicos {
  id?: number;
  nome?: string;
  crm?: string;
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
  especialidades?: IEspecialidades;
}

export const defaultValue: Readonly<IMedicos> = {};
