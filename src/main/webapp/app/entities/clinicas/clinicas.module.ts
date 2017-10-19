import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SafhSharedModule } from '../../shared';
import {
    ClinicasService,
    ClinicasPopupService,
    ClinicasComponent,
    ClinicasDetailComponent,
    ClinicasDialogComponent,
    ClinicasPopupComponent,
    ClinicasDeletePopupComponent,
    ClinicasDeleteDialogComponent,
    clinicasRoute,
    clinicasPopupRoute,
} from './';

const ENTITY_STATES = [
    ...clinicasRoute,
    ...clinicasPopupRoute,
];

@NgModule({
    imports: [
        SafhSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ClinicasComponent,
        ClinicasDetailComponent,
        ClinicasDialogComponent,
        ClinicasDeleteDialogComponent,
        ClinicasPopupComponent,
        ClinicasDeletePopupComponent,
    ],
    entryComponents: [
        ClinicasComponent,
        ClinicasDialogComponent,
        ClinicasPopupComponent,
        ClinicasDeleteDialogComponent,
        ClinicasDeletePopupComponent,
    ],
    providers: [
        ClinicasService,
        ClinicasPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SafhClinicasModule {}
