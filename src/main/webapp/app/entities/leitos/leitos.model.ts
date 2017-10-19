import { BaseEntity } from './../../shared';

export class Leitos implements BaseEntity {
    constructor(
        public id?: number,
        public leito?: string,
        public tipo?: string,
        public pacientes?: BaseEntity[],
    ) {
    }
}
