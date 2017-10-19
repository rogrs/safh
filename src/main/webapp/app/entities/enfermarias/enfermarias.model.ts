import { BaseEntity } from './../../shared';

export class Enfermarias implements BaseEntity {
    constructor(
        public id?: number,
        public enfermaria?: string,
        public pacientes?: BaseEntity[],
    ) {
    }
}
