import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClinicas } from '../clinicas.model';

@Component({
  selector: 'jhi-clinicas-detail',
  templateUrl: './clinicas-detail.component.html',
})
export class ClinicasDetailComponent implements OnInit {
  clinicas: IClinicas | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clinicas }) => {
      this.clinicas = clinicas;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
