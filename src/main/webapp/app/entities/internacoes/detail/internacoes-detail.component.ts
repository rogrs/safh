import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInternacoes } from '../internacoes.model';

@Component({
  selector: 'jhi-internacoes-detail',
  templateUrl: './internacoes-detail.component.html',
})
export class InternacoesDetailComponent implements OnInit {
  internacoes: IInternacoes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ internacoes }) => {
      this.internacoes = internacoes;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
