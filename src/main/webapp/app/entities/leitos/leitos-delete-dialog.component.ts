import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Leitos } from './leitos.model';
import { LeitosPopupService } from './leitos-popup.service';
import { LeitosService } from './leitos.service';

@Component({
    selector: 'jhi-leitos-delete-dialog',
    templateUrl: './leitos-delete-dialog.component.html'
})
export class LeitosDeleteDialogComponent {

    leitos: Leitos;

    constructor(
        private leitosService: LeitosService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.leitosService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'leitosListModification',
                content: 'Deleted an leitos'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-leitos-delete-popup',
    template: ''
})
export class LeitosDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private leitosPopupService: LeitosPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.leitosPopupService
                .open(LeitosDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
