import { IPosologias } from 'app/entities/posologias/posologias.model';
import { IFabricantes } from 'app/entities/fabricantes/fabricantes.model';

export interface IMedicamentos {
  id?: number;
  descricao?: string;
  registroMinisterioSaude?: string;
  codigoBarras?: string;
  qtdAtual?: number | null;
  qtdMin?: number | null;
  qtdMax?: number | null;
  observacoes?: string | null;
  apresentacao?: string | null;
  posologiaPadrao?: IPosologias | null;
  fabricantes?: IFabricantes | null;
}

export class Medicamentos implements IMedicamentos {
  constructor(
    public id?: number,
    public descricao?: string,
    public registroMinisterioSaude?: string,
    public codigoBarras?: string,
    public qtdAtual?: number | null,
    public qtdMin?: number | null,
    public qtdMax?: number | null,
    public observacoes?: string | null,
    public apresentacao?: string | null,
    public posologiaPadrao?: IPosologias | null,
    public fabricantes?: IFabricantes | null
  ) {}
}

export function getMedicamentosIdentifier(medicamentos: IMedicamentos): number | undefined {
  return medicamentos.id;
}
