import { IPacientes } from 'app/shared/model/pacientes.model';

export interface IEnfermarias {
  id?: string;
  enfermaria?: string;
  pacientes?: IPacientes[];
}

export const defaultValue: Readonly<IEnfermarias> = {};
