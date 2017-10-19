import { BaseEntity } from './../../shared';

export class Medicamentos implements BaseEntity {
    constructor(
        public id?: number,
        public descricao?: string,
        public registroMinisterioSaude?: string,
        public codigoBarras?: string,
        public qtdAtual?: number,
        public qtdMin?: number,
        public qtdMax?: number,
        public observacoes?: string,
        public apresentacao?: string,
        public posologiaPadrao?: BaseEntity,
        public fabricantes?: BaseEntity,
    ) {
    }
}
