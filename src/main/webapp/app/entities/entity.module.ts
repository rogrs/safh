import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SafhClinicasModule } from './clinicas/clinicas.module';
import { SafhDietasModule } from './dietas/dietas.module';
import { SafhEnfermariasModule } from './enfermarias/enfermarias.module';
import { SafhEspecialidadesModule } from './especialidades/especialidades.module';
import { SafhFabricantesModule } from './fabricantes/fabricantes.module';
import { SafhInternacoesModule } from './internacoes/internacoes.module';
import { SafhInternacoesDetalhesModule } from './internacoes-detalhes/internacoes-detalhes.module';
import { SafhLeitosModule } from './leitos/leitos.module';
import { SafhMedicamentosModule } from './medicamentos/medicamentos.module';
import { SafhMedicosModule } from './medicos/medicos.module';
import { SafhPacientesModule } from './pacientes/pacientes.module';
import { SafhPosologiasModule } from './posologias/posologias.module';
import { SafhPrescricoesModule } from './prescricoes/prescricoes.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        SafhClinicasModule,
        SafhDietasModule,
        SafhEnfermariasModule,
        SafhEspecialidadesModule,
        SafhFabricantesModule,
        SafhInternacoesModule,
        SafhInternacoesDetalhesModule,
        SafhLeitosModule,
        SafhMedicamentosModule,
        SafhMedicosModule,
        SafhPacientesModule,
        SafhPosologiasModule,
        SafhPrescricoesModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SafhEntityModule {}
