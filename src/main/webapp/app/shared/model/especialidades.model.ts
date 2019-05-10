import { IMedicos } from 'app/shared/model/medicos.model';

export interface IEspecialidades {
  id?: number;
  especialidade?: string;
  medicos?: IMedicos[];
}

export const defaultValue: Readonly<IEspecialidades> = {};
