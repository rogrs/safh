import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager } from 'ng-jhipster';

import { Pacientes } from './pacientes.model';
import { PacientesService } from './pacientes.service';

@Component({
    selector: 'jhi-pacientes-detail',
    templateUrl: './pacientes-detail.component.html'
})
export class PacientesDetailComponent implements OnInit, OnDestroy {

    pacientes: Pacientes;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private pacientesService: PacientesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInPacientes();
    }

    load(id) {
        this.pacientesService.find(id).subscribe((pacientes) => {
            this.pacientes = pacientes;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInPacientes() {
        this.eventSubscriber = this.eventManager.subscribe(
            'pacientesListModification',
            (response) => this.load(this.pacientes.id)
        );
    }
}
