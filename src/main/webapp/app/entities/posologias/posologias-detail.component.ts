import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Posologias } from './posologias.model';
import { PosologiasService } from './posologias.service';

@Component({
    selector: 'jhi-posologias-detail',
    templateUrl: './posologias-detail.component.html'
})
export class PosologiasDetailComponent implements OnInit, OnDestroy {

    posologias: Posologias;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private posologiasService: PosologiasService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPosologias();
    }

    load(id) {
        this.posologiasService.find(id).subscribe((posologias) => {
            this.posologias = posologias;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPosologias() {
        this.eventSubscriber = this.eventManager.subscribe(
            'posologiasListModification',
            (response) => this.load(this.posologias.id)
        );
    }
}
