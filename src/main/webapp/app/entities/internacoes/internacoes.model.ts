import * as dayjs from 'dayjs';
import { IInternacoesDetalhes } from 'app/entities/internacoes-detalhes/internacoes-detalhes.model';
import { IPacientes } from 'app/entities/pacientes/pacientes.model';
import { IClinicas } from 'app/entities/clinicas/clinicas.model';
import { IMedicos } from 'app/entities/medicos/medicos.model';

export interface IInternacoes {
  id?: number;
  dataInternacao?: dayjs.Dayjs;
  descricao?: string;
  internacoesDetalhes?: IInternacoesDetalhes[] | null;
  pacientes?: IPacientes | null;
  clinicas?: IClinicas | null;
  medicos?: IMedicos | null;
}

export class Internacoes implements IInternacoes {
  constructor(
    public id?: number,
    public dataInternacao?: dayjs.Dayjs,
    public descricao?: string,
    public internacoesDetalhes?: IInternacoesDetalhes[] | null,
    public pacientes?: IPacientes | null,
    public clinicas?: IClinicas | null,
    public medicos?: IMedicos | null
  ) {}
}

export function getInternacoesIdentifier(internacoes: IInternacoes): number | undefined {
  return internacoes.id;
}
