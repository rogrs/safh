import { IMedicamentos } from 'app/entities/medicamentos/medicamentos.model';
import { IInternacoesDetalhes } from 'app/entities/internacoes-detalhes/internacoes-detalhes.model';

export interface IPosologias {
  id?: number;
  posologia?: string;
  medicamentos?: IMedicamentos[] | null;
  internacoesDetalhes?: IInternacoesDetalhes[] | null;
}

export class Posologias implements IPosologias {
  constructor(
    public id?: number,
    public posologia?: string,
    public medicamentos?: IMedicamentos[] | null,
    public internacoesDetalhes?: IInternacoesDetalhes[] | null
  ) {}
}

export function getPosologiasIdentifier(posologias: IPosologias): number | undefined {
  return posologias.id;
}
