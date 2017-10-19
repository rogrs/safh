import { BaseEntity } from './../../shared';

export class Posologias implements BaseEntity {
    constructor(
        public id?: number,
        public posologia?: string,
        public medicamentos?: BaseEntity[],
        public internacoesDetalhes?: BaseEntity[],
    ) {
    }
}
