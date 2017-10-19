import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SafhSharedModule } from '../../shared';
import {
    PosologiasService,
    PosologiasPopupService,
    PosologiasComponent,
    PosologiasDetailComponent,
    PosologiasDialogComponent,
    PosologiasPopupComponent,
    PosologiasDeletePopupComponent,
    PosologiasDeleteDialogComponent,
    posologiasRoute,
    posologiasPopupRoute,
} from './';

const ENTITY_STATES = [
    ...posologiasRoute,
    ...posologiasPopupRoute,
];

@NgModule({
    imports: [
        SafhSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        PosologiasComponent,
        PosologiasDetailComponent,
        PosologiasDialogComponent,
        PosologiasDeleteDialogComponent,
        PosologiasPopupComponent,
        PosologiasDeletePopupComponent,
    ],
    entryComponents: [
        PosologiasComponent,
        PosologiasDialogComponent,
        PosologiasPopupComponent,
        PosologiasDeleteDialogComponent,
        PosologiasDeletePopupComponent,
    ],
    providers: [
        PosologiasService,
        PosologiasPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SafhPosologiasModule {}
