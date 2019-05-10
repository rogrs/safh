import { IPacientes } from 'app/shared/model/pacientes.model';

export interface IEnfermarias {
  id?: number;
  enfermaria?: string;
  pacientes?: IPacientes[];
}

export const defaultValue: Readonly<IEnfermarias> = {};
