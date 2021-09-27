import { IPacientes } from 'app/entities/pacientes/pacientes.model';

export interface ILeitos {
  id?: number;
  leito?: string;
  tipo?: string | null;
  pacientes?: IPacientes[] | null;
}

export class Leitos implements ILeitos {
  constructor(public id?: number, public leito?: string, public tipo?: string | null, public pacientes?: IPacientes[] | null) {}
}

export function getLeitosIdentifier(leitos: ILeitos): number | undefined {
  return leitos.id;
}
