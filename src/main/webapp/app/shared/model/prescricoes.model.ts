import { IInternacoesDetalhes } from 'app/shared/model/internacoes-detalhes.model';

export interface IPrescricoes {
  id?: string;
  prescricao?: string;
  internacoesDetalhes?: IInternacoesDetalhes[];
}

export const defaultValue: Readonly<IPrescricoes> = {};
