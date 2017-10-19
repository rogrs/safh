import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SafhSharedModule } from '../../shared';
import {
    EnfermariasService,
    EnfermariasPopupService,
    EnfermariasComponent,
    EnfermariasDetailComponent,
    EnfermariasDialogComponent,
    EnfermariasPopupComponent,
    EnfermariasDeletePopupComponent,
    EnfermariasDeleteDialogComponent,
    enfermariasRoute,
    enfermariasPopupRoute,
} from './';

const ENTITY_STATES = [
    ...enfermariasRoute,
    ...enfermariasPopupRoute,
];

@NgModule({
    imports: [
        SafhSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        EnfermariasComponent,
        EnfermariasDetailComponent,
        EnfermariasDialogComponent,
        EnfermariasDeleteDialogComponent,
        EnfermariasPopupComponent,
        EnfermariasDeletePopupComponent,
    ],
    entryComponents: [
        EnfermariasComponent,
        EnfermariasDialogComponent,
        EnfermariasPopupComponent,
        EnfermariasDeleteDialogComponent,
        EnfermariasDeletePopupComponent,
    ],
    providers: [
        EnfermariasService,
        EnfermariasPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SafhEnfermariasModule {}
