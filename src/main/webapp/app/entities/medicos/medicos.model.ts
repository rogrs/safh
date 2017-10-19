import { BaseEntity } from './../../shared';

export const enum Estados {
    'AC',
    'AL',
    'AM',
    'AP',
    'BA',
    'CE',
    'DF',
    'ES',
    'GO',
    'MA',
    'MG',
    'MS',
    'MT',
    'PA',
    'PB',
    'PE',
    'PI',
    'PR',
    'RJ',
    'RN',
    'RO',
    'RR',
    'RS',
    'SC',
    'SE',
    'SP',
    'TO'
}

export class Medicos implements BaseEntity {
    constructor(
        public id?: number,
        public nome?: string,
        public crm?: string,
        public cpf?: string,
        public email?: string,
        public cep?: string,
        public logradouro?: string,
        public numero?: string,
        public complemento?: string,
        public bairro?: string,
        public cidade?: string,
        public uF?: Estados,
        public internacoes?: BaseEntity[],
        public especialidades?: BaseEntity,
    ) {
    }
}
