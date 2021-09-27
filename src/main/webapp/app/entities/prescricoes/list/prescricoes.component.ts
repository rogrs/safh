import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrescricoes } from '../prescricoes.model';
import { PrescricoesService } from '../service/prescricoes.service';
import { PrescricoesDeleteDialogComponent } from '../delete/prescricoes-delete-dialog.component';

@Component({
  selector: 'jhi-prescricoes',
  templateUrl: './prescricoes.component.html',
})
export class PrescricoesComponent implements OnInit {
  prescricoes?: IPrescricoes[];
  isLoading = false;

  constructor(protected prescricoesService: PrescricoesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.prescricoesService.query().subscribe(
      (res: HttpResponse<IPrescricoes[]>) => {
        this.isLoading = false;
        this.prescricoes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPrescricoes): number {
    return item.id!;
  }

  delete(prescricoes: IPrescricoes): void {
    const modalRef = this.modalService.open(PrescricoesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.prescricoes = prescricoes;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
