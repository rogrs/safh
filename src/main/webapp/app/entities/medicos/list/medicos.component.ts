import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IMedicos } from '../medicos.model';
import { MedicosService } from '../service/medicos.service';
import { MedicosDeleteDialogComponent } from '../delete/medicos-delete-dialog.component';

@Component({
  selector: 'jhi-medicos',
  templateUrl: './medicos.component.html',
})
export class MedicosComponent implements OnInit {
  medicos?: IMedicos[];
  isLoading = false;

  constructor(protected medicosService: MedicosService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.medicosService.query().subscribe(
      (res: HttpResponse<IMedicos[]>) => {
        this.isLoading = false;
        this.medicos = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IMedicos): number {
    return item.id!;
  }

  delete(medicos: IMedicos): void {
    const modalRef = this.modalService.open(MedicosDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.medicos = medicos;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
