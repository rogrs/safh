import { IInternacoesDetalhes } from 'app/shared/model/internacoes-detalhes.model';

export interface IDietas {
  id?: string;
  dieta?: string;
  descricao?: string;
  internacoesDetalhes?: IInternacoesDetalhes[];
}

export const defaultValue: Readonly<IDietas> = {};
