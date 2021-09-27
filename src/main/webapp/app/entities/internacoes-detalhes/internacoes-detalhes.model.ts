import * as dayjs from 'dayjs';
import { IInternacoes } from 'app/entities/internacoes/internacoes.model';
import { IDietas } from 'app/entities/dietas/dietas.model';
import { IPrescricoes } from 'app/entities/prescricoes/prescricoes.model';
import { IPosologias } from 'app/entities/posologias/posologias.model';

export interface IInternacoesDetalhes {
  id?: number;
  dataDetalhe?: dayjs.Dayjs;
  horario?: dayjs.Dayjs;
  qtd?: number;
  internacoes?: IInternacoes | null;
  dietas?: IDietas | null;
  prescricoes?: IPrescricoes | null;
  posologias?: IPosologias | null;
}

export class InternacoesDetalhes implements IInternacoesDetalhes {
  constructor(
    public id?: number,
    public dataDetalhe?: dayjs.Dayjs,
    public horario?: dayjs.Dayjs,
    public qtd?: number,
    public internacoes?: IInternacoes | null,
    public dietas?: IDietas | null,
    public prescricoes?: IPrescricoes | null,
    public posologias?: IPosologias | null
  ) {}
}

export function getInternacoesDetalhesIdentifier(internacoesDetalhes: IInternacoesDetalhes): number | undefined {
  return internacoesDetalhes.id;
}
