import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SafhSharedModule } from '../../shared';
import {
    MedicosService,
    MedicosPopupService,
    MedicosComponent,
    MedicosDetailComponent,
    MedicosDialogComponent,
    MedicosPopupComponent,
    MedicosDeletePopupComponent,
    MedicosDeleteDialogComponent,
    medicosRoute,
    medicosPopupRoute,
} from './';

const ENTITY_STATES = [
    ...medicosRoute,
    ...medicosPopupRoute,
];

@NgModule({
    imports: [
        SafhSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        MedicosComponent,
        MedicosDetailComponent,
        MedicosDialogComponent,
        MedicosDeleteDialogComponent,
        MedicosPopupComponent,
        MedicosDeletePopupComponent,
    ],
    entryComponents: [
        MedicosComponent,
        MedicosDialogComponent,
        MedicosPopupComponent,
        MedicosDeleteDialogComponent,
        MedicosDeletePopupComponent,
    ],
    providers: [
        MedicosService,
        MedicosPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SafhMedicosModule {}
