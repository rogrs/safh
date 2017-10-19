import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Medicos } from './medicos.model';
import { MedicosPopupService } from './medicos-popup.service';
import { MedicosService } from './medicos.service';

@Component({
    selector: 'jhi-medicos-delete-dialog',
    templateUrl: './medicos-delete-dialog.component.html'
})
export class MedicosDeleteDialogComponent {

    medicos: Medicos;

    constructor(
        private medicosService: MedicosService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.medicosService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'medicosListModification',
                content: 'Deleted an medicos'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-medicos-delete-popup',
    template: ''
})
export class MedicosDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private medicosPopupService: MedicosPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.medicosPopupService
                .open(MedicosDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
