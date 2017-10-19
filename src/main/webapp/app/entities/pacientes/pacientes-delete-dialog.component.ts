import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Pacientes } from './pacientes.model';
import { PacientesPopupService } from './pacientes-popup.service';
import { PacientesService } from './pacientes.service';

@Component({
    selector: 'jhi-pacientes-delete-dialog',
    templateUrl: './pacientes-delete-dialog.component.html'
})
export class PacientesDeleteDialogComponent {

    pacientes: Pacientes;

    constructor(
        private pacientesService: PacientesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.pacientesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'pacientesListModification',
                content: 'Deleted an pacientes'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-pacientes-delete-popup',
    template: ''
})
export class PacientesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private pacientesPopupService: PacientesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.pacientesPopupService
                .open(PacientesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
