import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SafhSharedModule } from '../../shared';
import {
    EspecialidadesService,
    EspecialidadesPopupService,
    EspecialidadesComponent,
    EspecialidadesDetailComponent,
    EspecialidadesDialogComponent,
    EspecialidadesPopupComponent,
    EspecialidadesDeletePopupComponent,
    EspecialidadesDeleteDialogComponent,
    especialidadesRoute,
    especialidadesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...especialidadesRoute,
    ...especialidadesPopupRoute,
];

@NgModule({
    imports: [
        SafhSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        EspecialidadesComponent,
        EspecialidadesDetailComponent,
        EspecialidadesDialogComponent,
        EspecialidadesDeleteDialogComponent,
        EspecialidadesPopupComponent,
        EspecialidadesDeletePopupComponent,
    ],
    entryComponents: [
        EspecialidadesComponent,
        EspecialidadesDialogComponent,
        EspecialidadesPopupComponent,
        EspecialidadesDeleteDialogComponent,
        EspecialidadesDeletePopupComponent,
    ],
    providers: [
        EspecialidadesService,
        EspecialidadesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SafhEspecialidadesModule {}
