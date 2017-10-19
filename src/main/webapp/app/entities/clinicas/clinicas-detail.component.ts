import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Clinicas } from './clinicas.model';
import { ClinicasService } from './clinicas.service';

@Component({
    selector: 'jhi-clinicas-detail',
    templateUrl: './clinicas-detail.component.html'
})
export class ClinicasDetailComponent implements OnInit, OnDestroy {

    clinicas: Clinicas;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private clinicasService: ClinicasService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInClinicas();
    }

    load(id) {
        this.clinicasService.find(id).subscribe((clinicas) => {
            this.clinicas = clinicas;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInClinicas() {
        this.eventSubscriber = this.eventManager.subscribe(
            'clinicasListModification',
            (response) => this.load(this.clinicas.id)
        );
    }
}
