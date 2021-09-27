import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDietas } from '../dietas.model';
import { DietasService } from '../service/dietas.service';
import { DietasDeleteDialogComponent } from '../delete/dietas-delete-dialog.component';

@Component({
  selector: 'jhi-dietas',
  templateUrl: './dietas.component.html',
})
export class DietasComponent implements OnInit {
  dietas?: IDietas[];
  isLoading = false;

  constructor(protected dietasService: DietasService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.dietasService.query().subscribe(
      (res: HttpResponse<IDietas[]>) => {
        this.isLoading = false;
        this.dietas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDietas): number {
    return item.id!;
  }

  delete(dietas: IDietas): void {
    const modalRef = this.modalService.open(DietasDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dietas = dietas;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
