import { BaseEntity } from './../../shared';

export class Prescricoes implements BaseEntity {
    constructor(
        public id?: number,
        public prescricao?: string,
        public internacoesDetalhes?: BaseEntity[],
    ) {
    }
}
