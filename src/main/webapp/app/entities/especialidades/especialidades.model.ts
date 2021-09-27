import { IMedicos } from 'app/entities/medicos/medicos.model';

export interface IEspecialidades {
  id?: number;
  especialidade?: string;
  medicos?: IMedicos[] | null;
}

export class Especialidades implements IEspecialidades {
  constructor(public id?: number, public especialidade?: string, public medicos?: IMedicos[] | null) {}
}

export function getEspecialidadesIdentifier(especialidades: IEspecialidades): number | undefined {
  return especialidades.id;
}
