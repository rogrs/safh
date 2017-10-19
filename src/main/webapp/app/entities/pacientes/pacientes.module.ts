import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SafhSharedModule } from '../../shared';
import {
    PacientesService,
    PacientesPopupService,
    PacientesComponent,
    PacientesDetailComponent,
    PacientesDialogComponent,
    PacientesPopupComponent,
    PacientesDeletePopupComponent,
    PacientesDeleteDialogComponent,
    pacientesRoute,
    pacientesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...pacientesRoute,
    ...pacientesPopupRoute,
];

@NgModule({
    imports: [
        SafhSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PacientesComponent,
        PacientesDetailComponent,
        PacientesDialogComponent,
        PacientesDeleteDialogComponent,
        PacientesPopupComponent,
        PacientesDeletePopupComponent,
    ],
    entryComponents: [
        PacientesComponent,
        PacientesDialogComponent,
        PacientesPopupComponent,
        PacientesDeleteDialogComponent,
        PacientesDeletePopupComponent,
    ],
    providers: [
        PacientesService,
        PacientesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SafhPacientesModule {}
