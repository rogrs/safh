import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Enfermarias } from './enfermarias.model';
import { EnfermariasService } from './enfermarias.service';

@Component({
    selector: 'jhi-enfermarias-detail',
    templateUrl: './enfermarias-detail.component.html'
})
export class EnfermariasDetailComponent implements OnInit, OnDestroy {

    enfermarias: Enfermarias;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private enfermariasService: EnfermariasService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInEnfermarias();
    }

    load(id) {
        this.enfermariasService.find(id).subscribe((enfermarias) => {
            this.enfermarias = enfermarias;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInEnfermarias() {
        this.eventSubscriber = this.eventManager.subscribe(
            'enfermariasListModification',
            (response) => this.load(this.enfermarias.id)
        );
    }
}
