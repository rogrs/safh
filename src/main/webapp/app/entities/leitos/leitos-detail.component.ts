import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Leitos } from './leitos.model';
import { LeitosService } from './leitos.service';

@Component({
    selector: 'jhi-leitos-detail',
    templateUrl: './leitos-detail.component.html'
})
export class LeitosDetailComponent implements OnInit, OnDestroy {

    leitos: Leitos;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private leitosService: LeitosService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInLeitos();
    }

    load(id) {
        this.leitosService.find(id).subscribe((leitos) => {
            this.leitos = leitos;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInLeitos() {
        this.eventSubscriber = this.eventManager.subscribe(
            'leitosListModification',
            (response) => this.load(this.leitos.id)
        );
    }
}
