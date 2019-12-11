import { IMedicamentos } from 'app/shared/model/medicamentos.model';

export interface IFabricantes {
  id?: string;
  fabricante?: string;
  medicamentos?: IMedicamentos[];
}

export const defaultValue: Readonly<IFabricantes> = {};
