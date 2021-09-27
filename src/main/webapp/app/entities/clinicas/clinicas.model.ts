import { IPacientes } from 'app/entities/pacientes/pacientes.model';
import { IInternacoes } from 'app/entities/internacoes/internacoes.model';

export interface IClinicas {
  id?: number;
  clinica?: string;
  descricao?: string | null;
  pacientes?: IPacientes[] | null;
  internacoes?: IInternacoes[] | null;
}

export class Clinicas implements IClinicas {
  constructor(
    public id?: number,
    public clinica?: string,
    public descricao?: string | null,
    public pacientes?: IPacientes[] | null,
    public internacoes?: IInternacoes[] | null
  ) {}
}

export function getClinicasIdentifier(clinicas: IClinicas): number | undefined {
  return clinicas.id;
}
