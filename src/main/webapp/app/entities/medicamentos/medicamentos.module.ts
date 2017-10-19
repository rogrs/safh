import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SafhSharedModule } from '../../shared';
import {
    MedicamentosService,
    MedicamentosPopupService,
    MedicamentosComponent,
    MedicamentosDetailComponent,
    MedicamentosDialogComponent,
    MedicamentosPopupComponent,
    MedicamentosDeletePopupComponent,
    MedicamentosDeleteDialogComponent,
    medicamentosRoute,
    medicamentosPopupRoute,
} from './';

const ENTITY_STATES = [
    ...medicamentosRoute,
    ...medicamentosPopupRoute,
];

@NgModule({
    imports: [
        SafhSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        MedicamentosComponent,
        MedicamentosDetailComponent,
        MedicamentosDialogComponent,
        MedicamentosDeleteDialogComponent,
        MedicamentosPopupComponent,
        MedicamentosDeletePopupComponent,
    ],
    entryComponents: [
        MedicamentosComponent,
        MedicamentosDialogComponent,
        MedicamentosPopupComponent,
        MedicamentosDeleteDialogComponent,
        MedicamentosDeletePopupComponent,
    ],
    providers: [
        MedicamentosService,
        MedicamentosPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SafhMedicamentosModule {}
