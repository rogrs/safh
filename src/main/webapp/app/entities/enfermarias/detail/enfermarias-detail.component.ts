import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEnfermarias } from '../enfermarias.model';

@Component({
  selector: 'jhi-enfermarias-detail',
  templateUrl: './enfermarias-detail.component.html',
})
export class EnfermariasDetailComponent implements OnInit {
  enfermarias: IEnfermarias | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ enfermarias }) => {
      this.enfermarias = enfermarias;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
