import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IClinicas } from '../clinicas.model';
import { ClinicasService } from '../service/clinicas.service';
import { ClinicasDeleteDialogComponent } from '../delete/clinicas-delete-dialog.component';

@Component({
  selector: 'jhi-clinicas',
  templateUrl: './clinicas.component.html',
})
export class ClinicasComponent implements OnInit {
  clinicas?: IClinicas[];
  isLoading = false;

  constructor(protected clinicasService: ClinicasService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.clinicasService.query().subscribe(
      (res: HttpResponse<IClinicas[]>) => {
        this.isLoading = false;
        this.clinicas = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IClinicas): number {
    return item.id!;
  }

  delete(clinicas: IClinicas): void {
    const modalRef = this.modalService.open(ClinicasDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.clinicas = clinicas;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
