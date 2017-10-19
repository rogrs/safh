import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Clinicas } from './clinicas.model';
import { ClinicasPopupService } from './clinicas-popup.service';
import { ClinicasService } from './clinicas.service';

@Component({
    selector: 'jhi-clinicas-delete-dialog',
    templateUrl: './clinicas-delete-dialog.component.html'
})
export class ClinicasDeleteDialogComponent {

    clinicas: Clinicas;

    constructor(
        private clinicasService: ClinicasService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.clinicasService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'clinicasListModification',
                content: 'Deleted an clinicas'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-clinicas-delete-popup',
    template: ''
})
export class ClinicasDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private clinicasPopupService: ClinicasPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.clinicasPopupService
                .open(ClinicasDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
