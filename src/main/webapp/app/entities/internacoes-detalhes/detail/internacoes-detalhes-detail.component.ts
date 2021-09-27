import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IInternacoesDetalhes } from '../internacoes-detalhes.model';

@Component({
  selector: 'jhi-internacoes-detalhes-detail',
  templateUrl: './internacoes-detalhes-detail.component.html',
})
export class InternacoesDetalhesDetailComponent implements OnInit {
  internacoesDetalhes: IInternacoesDetalhes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ internacoesDetalhes }) => {
      this.internacoesDetalhes = internacoesDetalhes;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
