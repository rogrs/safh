import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IInternacoes } from '../internacoes.model';
import { InternacoesService } from '../service/internacoes.service';
import { InternacoesDeleteDialogComponent } from '../delete/internacoes-delete-dialog.component';

@Component({
  selector: 'jhi-internacoes',
  templateUrl: './internacoes.component.html',
})
export class InternacoesComponent implements OnInit {
  internacoes?: IInternacoes[];
  isLoading = false;

  constructor(protected internacoesService: InternacoesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.internacoesService.query().subscribe(
      (res: HttpResponse<IInternacoes[]>) => {
        this.isLoading = false;
        this.internacoes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IInternacoes): number {
    return item.id!;
  }

  delete(internacoes: IInternacoes): void {
    const modalRef = this.modalService.open(InternacoesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.internacoes = internacoes;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
