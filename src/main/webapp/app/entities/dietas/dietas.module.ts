import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SafhSharedModule } from '../../shared';
import {
    DietasService,
    DietasPopupService,
    DietasComponent,
    DietasDetailComponent,
    DietasDialogComponent,
    DietasPopupComponent,
    DietasDeletePopupComponent,
    DietasDeleteDialogComponent,
    dietasRoute,
    dietasPopupRoute,
} from './';

const ENTITY_STATES = [
    ...dietasRoute,
    ...dietasPopupRoute,
];

@NgModule({
    imports: [
        SafhSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        DietasComponent,
        DietasDetailComponent,
        DietasDialogComponent,
        DietasDeleteDialogComponent,
        DietasPopupComponent,
        DietasDeletePopupComponent,
    ],
    entryComponents: [
        DietasComponent,
        DietasDialogComponent,
        DietasPopupComponent,
        DietasDeleteDialogComponent,
        DietasDeletePopupComponent,
    ],
    providers: [
        DietasService,
        DietasPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SafhDietasModule {}
