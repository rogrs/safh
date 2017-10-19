import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Internacoes } from './internacoes.model';
import { InternacoesPopupService } from './internacoes-popup.service';
import { InternacoesService } from './internacoes.service';

@Component({
    selector: 'jhi-internacoes-delete-dialog',
    templateUrl: './internacoes-delete-dialog.component.html'
})
export class InternacoesDeleteDialogComponent {

    internacoes: Internacoes;

    constructor(
        private internacoesService: InternacoesService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.internacoesService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'internacoesListModification',
                content: 'Deleted an internacoes'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-internacoes-delete-popup',
    template: ''
})
export class InternacoesDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private internacoesPopupService: InternacoesPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.internacoesPopupService
                .open(InternacoesDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
