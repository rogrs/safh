import { IMedicos } from 'app/shared/model/medicos.model';

export interface IEspecialidades {
  id?: string;
  especialidade?: string;
  medicos?: IMedicos[];
}

export const defaultValue: Readonly<IEspecialidades> = {};
