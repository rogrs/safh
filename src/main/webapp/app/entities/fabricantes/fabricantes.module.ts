import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SafhSharedModule } from '../../shared';
import {
    FabricantesService,
    FabricantesPopupService,
    FabricantesComponent,
    FabricantesDetailComponent,
    FabricantesDialogComponent,
    FabricantesPopupComponent,
    FabricantesDeletePopupComponent,
    FabricantesDeleteDialogComponent,
    fabricantesRoute,
    fabricantesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...fabricantesRoute,
    ...fabricantesPopupRoute,
];

@NgModule({
    imports: [
        SafhSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        FabricantesComponent,
        FabricantesDetailComponent,
        FabricantesDialogComponent,
        FabricantesDeleteDialogComponent,
        FabricantesPopupComponent,
        FabricantesDeletePopupComponent,
    ],
    entryComponents: [
        FabricantesComponent,
        FabricantesDialogComponent,
        FabricantesPopupComponent,
        FabricantesDeleteDialogComponent,
        FabricantesDeletePopupComponent,
    ],
    providers: [
        FabricantesService,
        FabricantesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SafhFabricantesModule {}
