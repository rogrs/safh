import { IInternacoes } from 'app/shared/model/internacoes.model';
import { IEspecialidades } from 'app/shared/model/especialidades.model';
import { Estados } from 'app/shared/model/enumerations/estados.model';

export interface IMedicos {
  id?: string;
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
