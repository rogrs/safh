import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SafhSharedModule } from '../../shared';
import {
    InternacoesService,
    InternacoesPopupService,
    InternacoesComponent,
    InternacoesDetailComponent,
    InternacoesDialogComponent,
    InternacoesPopupComponent,
    InternacoesDeletePopupComponent,
    InternacoesDeleteDialogComponent,
    internacoesRoute,
    internacoesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...internacoesRoute,
    ...internacoesPopupRoute,
];

@NgModule({
    imports: [
        SafhSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        InternacoesComponent,
        InternacoesDetailComponent,
        InternacoesDialogComponent,
        InternacoesDeleteDialogComponent,
        InternacoesPopupComponent,
        InternacoesDeletePopupComponent,
    ],
    entryComponents: [
        InternacoesComponent,
        InternacoesDialogComponent,
        InternacoesPopupComponent,
        InternacoesDeleteDialogComponent,
        InternacoesDeletePopupComponent,
    ],
    providers: [
        InternacoesService,
        InternacoesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SafhInternacoesModule {}
