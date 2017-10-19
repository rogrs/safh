import { BaseEntity } from './../../shared';

export class InternacoesDetalhes implements BaseEntity {
    constructor(
        public id?: number,
        public dataDetalhe?: any,
        public horario?: any,
        public qtd?: number,
        public internacoes?: BaseEntity,
        public dietas?: BaseEntity,
        public prescricoes?: BaseEntity,
        public posologias?: BaseEntity,
    ) {
    }
}
