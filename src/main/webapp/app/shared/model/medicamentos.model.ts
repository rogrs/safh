import { IPosologias } from 'app/shared/model/posologias.model';
import { IFabricantes } from 'app/shared/model/fabricantes.model';

export interface IMedicamentos {
  id?: string;
  descricao?: string;
  registroMinisterioSaude?: string;
  codigoBarras?: string;
  qtdAtual?: number;
  qtdMin?: number;
  qtdMax?: number;
  observacoes?: string;
  apresentacao?: string;
  posologiaPadrao?: IPosologias;
  fabricantes?: IFabricantes;
}

export const defaultValue: Readonly<IMedicamentos> = {};
