import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SafhSharedModule } from '../../shared';
import {
    InternacoesDetalhesService,
    InternacoesDetalhesPopupService,
    InternacoesDetalhesComponent,
    InternacoesDetalhesDetailComponent,
    InternacoesDetalhesDialogComponent,
    InternacoesDetalhesPopupComponent,
    InternacoesDetalhesDeletePopupComponent,
    InternacoesDetalhesDeleteDialogComponent,
    internacoesDetalhesRoute,
    internacoesDetalhesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...internacoesDetalhesRoute,
    ...internacoesDetalhesPopupRoute,
];

@NgModule({
    imports: [
        SafhSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        InternacoesDetalhesComponent,
        InternacoesDetalhesDetailComponent,
        InternacoesDetalhesDialogComponent,
        InternacoesDetalhesDeleteDialogComponent,
        InternacoesDetalhesPopupComponent,
        InternacoesDetalhesDeletePopupComponent,
    ],
    entryComponents: [
        InternacoesDetalhesComponent,
        InternacoesDetalhesDialogComponent,
        InternacoesDetalhesPopupComponent,
        InternacoesDetalhesDeleteDialogComponent,
        InternacoesDetalhesDeletePopupComponent,
    ],
    providers: [
        InternacoesDetalhesService,
        InternacoesDetalhesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SafhInternacoesDetalhesModule {}
