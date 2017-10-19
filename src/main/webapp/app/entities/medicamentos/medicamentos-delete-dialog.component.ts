import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Medicamentos } from './medicamentos.model';
import { MedicamentosPopupService } from './medicamentos-popup.service';
import { MedicamentosService } from './medicamentos.service';

@Component({
    selector: 'jhi-medicamentos-delete-dialog',
    templateUrl: './medicamentos-delete-dialog.component.html'
})
export class MedicamentosDeleteDialogComponent {

    medicamentos: Medicamentos;

    constructor(
        private medicamentosService: MedicamentosService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.medicamentosService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'medicamentosListModification',
                content: 'Deleted an medicamentos'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-medicamentos-delete-popup',
    template: ''
})
export class MedicamentosDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private medicamentosPopupService: MedicamentosPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.medicamentosPopupService
                .open(MedicamentosDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
