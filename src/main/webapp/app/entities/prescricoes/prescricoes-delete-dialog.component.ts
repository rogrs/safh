import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Prescricoes } from './prescricoes.model';
import { PrescricoesPopupService } from './prescricoes-popup.service';
import { PrescricoesService } from './prescricoes.service';

@Component({
    selector: 'jhi-prescricoes-delete-dialog',
    templateUrl: './prescricoes-delete-dialog.component.html'
})
export class PrescricoesDeleteDialogComponent {

    prescricoes: Prescricoes;

    constructor(
        private prescricoesService: PrescricoesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.prescricoesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'prescricoesListModification',
                content: 'Deleted an prescricoes'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-prescricoes-delete-popup',
    template: ''
})
export class PrescricoesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private prescricoesPopupService: PrescricoesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.prescricoesPopupService
                .open(PrescricoesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
