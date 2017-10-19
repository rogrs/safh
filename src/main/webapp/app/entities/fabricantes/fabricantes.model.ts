import { BaseEntity } from './../../shared';

export class Fabricantes implements BaseEntity {
    constructor(
        public id?: number,
        public fabricante?: string,
        public medicamentos?: BaseEntity[],
    ) {
    }
}
