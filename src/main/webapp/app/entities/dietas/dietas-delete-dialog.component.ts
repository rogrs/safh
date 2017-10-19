import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Dietas } from './dietas.model';
import { DietasPopupService } from './dietas-popup.service';
import { DietasService } from './dietas.service';

@Component({
    selector: 'jhi-dietas-delete-dialog',
    templateUrl: './dietas-delete-dialog.component.html'
})
export class DietasDeleteDialogComponent {

    dietas: Dietas;

    constructor(
        private dietasService: DietasService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.dietasService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'dietasListModification',
                content: 'Deleted an dietas'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-dietas-delete-popup',
    template: ''
})
export class DietasDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private dietasPopupService: DietasPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.dietasPopupService
                .open(DietasDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
