import { BaseEntity } from './../../shared';

export class Clinicas implements BaseEntity {
    constructor(
        public id?: number,
        public clinica?: string,
        public descricao?: string,
        public pacientes?: BaseEntity[],
        public internacoes?: BaseEntity[],
    ) {
    }
}
