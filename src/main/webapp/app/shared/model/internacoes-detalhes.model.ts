import { Moment } from 'moment';
import { IInternacoes } from 'app/shared/model/internacoes.model';
import { IDietas } from 'app/shared/model/dietas.model';
import { IPrescricoes } from 'app/shared/model/prescricoes.model';
import { IPosologias } from 'app/shared/model/posologias.model';

export interface IInternacoesDetalhes {
  id?: string;
  dataDetalhe?: Moment;
  horario?: Moment;
  qtd?: number;
  internacoes?: IInternacoes;
  dietas?: IDietas;
  prescricoes?: IPrescricoes;
  posologias?: IPosologias;
}

export const defaultValue: Readonly<IInternacoesDetalhes> = {};
