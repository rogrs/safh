import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Posologias } from './posologias.model';
import { PosologiasPopupService } from './posologias-popup.service';
import { PosologiasService } from './posologias.service';

@Component({
    selector: 'jhi-posologias-delete-dialog',
    templateUrl: './posologias-delete-dialog.component.html'
})
export class PosologiasDeleteDialogComponent {

    posologias: Posologias;

    constructor(
        private posologiasService: PosologiasService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.posologiasService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'posologiasListModification',
                content: 'Deleted an posologias'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-posologias-delete-popup',
    template: ''
})
export class PosologiasDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private posologiasPopupService: PosologiasPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.posologiasPopupService
                .open(PosologiasDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
