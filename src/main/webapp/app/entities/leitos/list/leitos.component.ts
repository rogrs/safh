import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ILeitos } from '../leitos.model';
import { LeitosService } from '../service/leitos.service';
import { LeitosDeleteDialogComponent } from '../delete/leitos-delete-dialog.component';

@Component({
  selector: 'jhi-leitos',
  templateUrl: './leitos.component.html',
})
export class LeitosComponent implements OnInit {
  leitos?: ILeitos[];
  isLoading = false;

  constructor(protected leitosService: LeitosService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.leitosService.query().subscribe(
      (res: HttpResponse<ILeitos[]>) => {
        this.isLoading = false;
        this.leitos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ILeitos): number {
    return item.id!;
  }

  delete(leitos: ILeitos): void {
    const modalRef = this.modalService.open(LeitosDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.leitos = leitos;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
