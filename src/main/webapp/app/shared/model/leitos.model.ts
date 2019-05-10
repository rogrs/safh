import { IPacientes } from 'app/shared/model/pacientes.model';

export interface ILeitos {
  id?: number;
  leito?: string;
  tipo?: string;
  pacientes?: IPacientes[];
}

export const defaultValue: Readonly<ILeitos> = {};
