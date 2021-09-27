import { IInternacoesDetalhes } from 'app/entities/internacoes-detalhes/internacoes-detalhes.model';

export interface IDietas {
  id?: number;
  dieta?: string;
  descricao?: string | null;
  internacoesDetalhes?: IInternacoesDetalhes[] | null;
}

export class Dietas implements IDietas {
  constructor(
    public id?: number,
    public dieta?: string,
    public descricao?: string | null,
    public internacoesDetalhes?: IInternacoesDetalhes[] | null
  ) {}
}

export function getDietasIdentifier(dietas: IDietas): number | undefined {
  return dietas.id;
}
