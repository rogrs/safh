import { BaseEntity } from './../../shared';

export class Dietas implements BaseEntity {
    constructor(
        public id?: number,
        public dieta?: string,
        public descricao?: string,
        public internacoesDetalhes?: BaseEntity[],
    ) {
    }
}
