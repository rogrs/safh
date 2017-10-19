import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Enfermarias } from './enfermarias.model';
import { EnfermariasPopupService } from './enfermarias-popup.service';
import { EnfermariasService } from './enfermarias.service';

@Component({
    selector: 'jhi-enfermarias-delete-dialog',
    templateUrl: './enfermarias-delete-dialog.component.html'
})
export class EnfermariasDeleteDialogComponent {

    enfermarias: Enfermarias;

    constructor(
        private enfermariasService: EnfermariasService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.enfermariasService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'enfermariasListModification',
                content: 'Deleted an enfermarias'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-enfermarias-delete-popup',
    template: ''
})
export class EnfermariasDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private enfermariasPopupService: EnfermariasPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.enfermariasPopupService
                .open(EnfermariasDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
