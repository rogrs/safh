import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SafhSharedModule } from '../../shared';
import {
    PrescricoesService,
    PrescricoesPopupService,
    PrescricoesComponent,
    PrescricoesDetailComponent,
    PrescricoesDialogComponent,
    PrescricoesPopupComponent,
    PrescricoesDeletePopupComponent,
    PrescricoesDeleteDialogComponent,
    prescricoesRoute,
    prescricoesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...prescricoesRoute,
    ...prescricoesPopupRoute,
];

@NgModule({
    imports: [
        SafhSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PrescricoesComponent,
        PrescricoesDetailComponent,
        PrescricoesDialogComponent,
        PrescricoesDeleteDialogComponent,
        PrescricoesPopupComponent,
        PrescricoesDeletePopupComponent,
    ],
    entryComponents: [
        PrescricoesComponent,
        PrescricoesDialogComponent,
        PrescricoesPopupComponent,
        PrescricoesDeleteDialogComponent,
        PrescricoesDeletePopupComponent,
    ],
    providers: [
        PrescricoesService,
        PrescricoesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SafhPrescricoesModule {}
