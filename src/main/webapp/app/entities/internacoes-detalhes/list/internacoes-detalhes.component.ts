import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInternacoesDetalhes } from '../internacoes-detalhes.model';
import { InternacoesDetalhesService } from '../service/internacoes-detalhes.service';
import { InternacoesDetalhesDeleteDialogComponent } from '../delete/internacoes-detalhes-delete-dialog.component';

@Component({
  selector: 'jhi-internacoes-detalhes',
  templateUrl: './internacoes-detalhes.component.html',
})
export class InternacoesDetalhesComponent implements OnInit {
  internacoesDetalhes?: IInternacoesDetalhes[];
  isLoading = false;

  constructor(protected internacoesDetalhesService: InternacoesDetalhesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.internacoesDetalhesService.query().subscribe(
      (res: HttpResponse<IInternacoesDetalhes[]>) => {
        this.isLoading = false;
        this.internacoesDetalhes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IInternacoesDetalhes): number {
    return item.id!;
  }

  delete(internacoesDetalhes: IInternacoesDetalhes): void {
    const modalRef = this.modalService.open(InternacoesDetalhesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.internacoesDetalhes = internacoesDetalhes;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
