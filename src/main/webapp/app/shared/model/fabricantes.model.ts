import { IMedicamentos } from 'app/shared/model/medicamentos.model';

export interface IFabricantes {
  id?: number;
  fabricante?: string;
  medicamentos?: IMedicamentos[];
}

export const defaultValue: Readonly<IFabricantes> = {};
