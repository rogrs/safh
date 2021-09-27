import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPosologias } from '../posologias.model';
import { PosologiasService } from '../service/posologias.service';
import { PosologiasDeleteDialogComponent } from '../delete/posologias-delete-dialog.component';

@Component({
  selector: 'jhi-posologias',
  templateUrl: './posologias.component.html',
})
export class PosologiasComponent implements OnInit {
  posologias?: IPosologias[];
  isLoading = false;

  constructor(protected posologiasService: PosologiasService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.posologiasService.query().subscribe(
      (res: HttpResponse<IPosologias[]>) => {
        this.isLoading = false;
        this.posologias = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPosologias): number {
    return item.id!;
  }

  delete(posologias: IPosologias): void {
    const modalRef = this.modalService.open(PosologiasDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.posologias = posologias;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
