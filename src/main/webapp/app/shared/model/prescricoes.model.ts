import { IInternacoesDetalhes } from 'app/shared/model/internacoes-detalhes.model';

export interface IPrescricoes {
  id?: number;
  prescricao?: string;
  internacoesDetalhes?: IInternacoesDetalhes[];
}

export const defaultValue: Readonly<IPrescricoes> = {};
