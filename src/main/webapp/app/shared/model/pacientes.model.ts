import { IInternacoes } from 'app/shared/model/internacoes.model';
import { IClinicas } from 'app/shared/model/clinicas.model';
import { IEnfermarias } from 'app/shared/model/enfermarias.model';
import { ILeitos } from 'app/shared/model/leitos.model';
import { Estados } from 'app/shared/model/enumerations/estados.model';

export interface IPacientes {
  id?: string;
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
