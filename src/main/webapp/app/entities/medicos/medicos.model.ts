import { IInternacoes } from 'app/entities/internacoes/internacoes.model';
import { IEspecialidades } from 'app/entities/especialidades/especialidades.model';
import { Estados } from 'app/entities/enumerations/estados.model';

export interface IMedicos {
  id?: number;
  nome?: string;
  crm?: string;
  cpf?: string | null;
  email?: string | null;
  cep?: string | null;
  logradouro?: string | null;
  numero?: string | null;
  complemento?: string | null;
  bairro?: string | null;
  cidade?: string | null;
  uF?: Estados | null;
  internacoes?: IInternacoes[] | null;
  especialidades?: IEspecialidades | null;
}

export class Medicos implements IMedicos {
  constructor(
    public id?: number,
    public nome?: string,
    public crm?: string,
    public cpf?: string | null,
    public email?: string | null,
    public cep?: string | null,
    public logradouro?: string | null,
    public numero?: string | null,
    public complemento?: string | null,
    public bairro?: string | null,
    public cidade?: string | null,
    public uF?: Estados | null,
    public internacoes?: IInternacoes[] | null,
    public especialidades?: IEspecialidades | null
  ) {}
}

export function getMedicosIdentifier(medicos: IMedicos): number | undefined {
  return medicos.id;
}
