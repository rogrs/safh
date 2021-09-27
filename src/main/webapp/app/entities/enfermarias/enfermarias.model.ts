import { IPacientes } from 'app/entities/pacientes/pacientes.model';

export interface IEnfermarias {
  id?: number;
  enfermaria?: string;
  pacientes?: IPacientes[] | null;
}

export class Enfermarias implements IEnfermarias {
  constructor(public id?: number, public enfermaria?: string, public pacientes?: IPacientes[] | null) {}
}

export function getEnfermariasIdentifier(enfermarias: IEnfermarias): number | undefined {
  return enfermarias.id;
}
