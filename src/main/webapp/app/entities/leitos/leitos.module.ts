import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SafhSharedModule } from '../../shared';
import {
    LeitosService,
    LeitosPopupService,
    LeitosComponent,
    LeitosDetailComponent,
    LeitosDialogComponent,
    LeitosPopupComponent,
    LeitosDeletePopupComponent,
    LeitosDeleteDialogComponent,
    leitosRoute,
    leitosPopupRoute,
} from './';

const ENTITY_STATES = [
    ...leitosRoute,
    ...leitosPopupRoute,
];

@NgModule({
    imports: [
        SafhSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        LeitosComponent,
        LeitosDetailComponent,
        LeitosDialogComponent,
        LeitosDeleteDialogComponent,
        LeitosPopupComponent,
        LeitosDeletePopupComponent,
    ],
    entryComponents: [
        LeitosComponent,
        LeitosDialogComponent,
        LeitosPopupComponent,
        LeitosDeleteDialogComponent,
        LeitosDeletePopupComponent,
    ],
    providers: [
        LeitosService,
        LeitosPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SafhLeitosModule {}
