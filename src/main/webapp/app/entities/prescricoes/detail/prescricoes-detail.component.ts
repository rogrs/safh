import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrescricoes } from '../prescricoes.model';

@Component({
  selector: 'jhi-prescricoes-detail',
  templateUrl: './prescricoes-detail.component.html',
})
export class PrescricoesDetailComponent implements OnInit {
  prescricoes: IPrescricoes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prescricoes }) => {
      this.prescricoes = prescricoes;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
