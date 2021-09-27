import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMedicamentos } from '../medicamentos.model';
import { MedicamentosService } from '../service/medicamentos.service';
import { MedicamentosDeleteDialogComponent } from '../delete/medicamentos-delete-dialog.component';

@Component({
  selector: 'jhi-medicamentos',
  templateUrl: './medicamentos.component.html',
})
export class MedicamentosComponent implements OnInit {
  medicamentos?: IMedicamentos[];
  isLoading = false;

  constructor(protected medicamentosService: MedicamentosService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.medicamentosService.query().subscribe(
      (res: HttpResponse<IMedicamentos[]>) => {
        this.isLoading = false;
        this.medicamentos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMedicamentos): number {
    return item.id!;
  }

  delete(medicamentos: IMedicamentos): void {
    const modalRef = this.modalService.open(MedicamentosDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.medicamentos = medicamentos;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
