import { IMedicamentos } from 'app/shared/model/medicamentos.model';
import { IInternacoesDetalhes } from 'app/shared/model/internacoes-detalhes.model';

export interface IPosologias {
  id?: number;
  posologia?: string;
  medicamentos?: IMedicamentos[];
  internacoesDetalhes?: IInternacoesDetalhes[];
}

export const defaultValue: Readonly<IPosologias> = {};
