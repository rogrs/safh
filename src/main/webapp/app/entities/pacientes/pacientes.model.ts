import { IInternacoes } from 'app/entities/internacoes/internacoes.model';
import { IClinicas } from 'app/entities/clinicas/clinicas.model';
import { IEnfermarias } from 'app/entities/enfermarias/enfermarias.model';
import { ILeitos } from 'app/entities/leitos/leitos.model';
import { Estados } from 'app/entities/enumerations/estados.model';

export interface IPacientes {
  id?: number;
  prontuario?: number;
  nome?: string;
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
  clinicas?: IClinicas | null;
  enfermarias?: IEnfermarias | null;
  leitos?: ILeitos | null;
}

export class Pacientes implements IPacientes {
  constructor(
    public id?: number,
    public prontuario?: number,
    public nome?: string,
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
    public clinicas?: IClinicas | null,
    public enfermarias?: IEnfermarias | null,
    public leitos?: ILeitos | null
  ) {}
}

export function getPacientesIdentifier(pacientes: IPacientes): number | undefined {
  return pacientes.id;
}
