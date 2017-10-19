import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Prescricoes } from './prescricoes.model';
import { PrescricoesService } from './prescricoes.service';

@Component({
    selector: 'jhi-prescricoes-detail',
    templateUrl: './prescricoes-detail.component.html'
})
export class PrescricoesDetailComponent implements OnInit, OnDestroy {

    prescricoes: Prescricoes;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private prescricoesService: PrescricoesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPrescricoes();
    }

    load(id) {
        this.prescricoesService.find(id).subscribe((prescricoes) => {
            this.prescricoes = prescricoes;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPrescricoes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'prescricoesListModification',
            (response) => this.load(this.prescricoes.id)
        );
    }
}
