import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPacientes } from '../pacientes.model';
import { PacientesService } from '../service/pacientes.service';
import { PacientesDeleteDialogComponent } from '../delete/pacientes-delete-dialog.component';

@Component({
  selector: 'jhi-pacientes',
  templateUrl: './pacientes.component.html',
})
export class PacientesComponent implements OnInit {
  pacientes?: IPacientes[];
  isLoading = false;

  constructor(protected pacientesService: PacientesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.pacientesService.query().subscribe(
      (res: HttpResponse<IPacientes[]>) => {
        this.isLoading = false;
        this.pacientes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPacientes): number {
    return item.id!;
  }

  delete(pacientes: IPacientes): void {
    const modalRef = this.modalService.open(PacientesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pacientes = pacientes;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
