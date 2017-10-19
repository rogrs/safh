import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Especialidades } from './especialidades.model';
import { EspecialidadesPopupService } from './especialidades-popup.service';
import { EspecialidadesService } from './especialidades.service';

@Component({
    selector: 'jhi-especialidades-delete-dialog',
    templateUrl: './especialidades-delete-dialog.component.html'
})
export class EspecialidadesDeleteDialogComponent {

    especialidades: Especialidades;

    constructor(
        private especialidadesService: EspecialidadesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.especialidadesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'especialidadesListModification',
                content: 'Deleted an especialidades'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-especialidades-delete-popup',
    template: ''
})
export class EspecialidadesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private especialidadesPopupService: EspecialidadesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.especialidadesPopupService
                .open(EspecialidadesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
