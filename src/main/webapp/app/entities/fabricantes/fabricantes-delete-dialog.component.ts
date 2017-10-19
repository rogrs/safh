import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Fabricantes } from './fabricantes.model';
import { FabricantesPopupService } from './fabricantes-popup.service';
import { FabricantesService } from './fabricantes.service';

@Component({
    selector: 'jhi-fabricantes-delete-dialog',
    templateUrl: './fabricantes-delete-dialog.component.html'
})
export class FabricantesDeleteDialogComponent {

    fabricantes: Fabricantes;

    constructor(
        private fabricantesService: FabricantesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.fabricantesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'fabricantesListModification',
                content: 'Deleted an fabricantes'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-fabricantes-delete-popup',
    template: ''
})
export class FabricantesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private fabricantesPopupService: FabricantesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.fabricantesPopupService
                .open(FabricantesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
