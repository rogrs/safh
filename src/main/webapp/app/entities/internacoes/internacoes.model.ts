import { BaseEntity } from './../../shared';

export class Internacoes implements BaseEntity {
    constructor(
        public id?: number,
        public dataInternacao?: any,
        public descricao?: string,
        public internacoesDetalhes?: BaseEntity[],
        public pacientes?: BaseEntity,
        public clinicas?: BaseEntity,
        public medicos?: BaseEntity,
    ) {
    }
}
