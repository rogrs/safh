import { IInternacoesDetalhes } from 'app/entities/internacoes-detalhes/internacoes-detalhes.model';

export interface IPrescricoes {
  id?: number;
  prescricao?: string;
  internacoesDetalhes?: IInternacoesDetalhes[] | null;
}

export class Prescricoes implements IPrescricoes {
  constructor(public id?: number, public prescricao?: string, public internacoesDetalhes?: IInternacoesDetalhes[] | null) {}
}

export function getPrescricoesIdentifier(prescricoes: IPrescricoes): number | undefined {
  return prescricoes.id;
}
