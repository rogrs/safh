import { IPacientes } from 'app/shared/model/pacientes.model';

export interface ILeitos {
  id?: string;
  leito?: string;
  tipo?: string;
  pacientes?: IPacientes[];
}

export const defaultValue: Readonly<ILeitos> = {};
