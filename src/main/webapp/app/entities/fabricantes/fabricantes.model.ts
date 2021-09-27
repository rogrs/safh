import { IMedicamentos } from 'app/entities/medicamentos/medicamentos.model';

export interface IFabricantes {
  id?: number;
  fabricante?: string;
  medicamentos?: IMedicamentos[] | null;
}

export class Fabricantes implements IFabricantes {
  constructor(public id?: number, public fabricante?: string, public medicamentos?: IMedicamentos[] | null) {}
}

export function getFabricantesIdentifier(fabricantes: IFabricantes): number | undefined {
  return fabricantes.id;
}
