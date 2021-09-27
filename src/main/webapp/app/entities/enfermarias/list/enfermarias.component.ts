import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEnfermarias } from '../enfermarias.model';
import { EnfermariasService } from '../service/enfermarias.service';
import { EnfermariasDeleteDialogComponent } from '../delete/enfermarias-delete-dialog.component';

@Component({
  selector: 'jhi-enfermarias',
  templateUrl: './enfermarias.component.html',
})
export class EnfermariasComponent implements OnInit {
  enfermarias?: IEnfermarias[];
  isLoading = false;

  constructor(protected enfermariasService: EnfermariasService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.enfermariasService.query().subscribe(
      (res: HttpResponse<IEnfermarias[]>) => {
        this.isLoading = false;
        this.enfermarias = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IEnfermarias): number {
    return item.id!;
  }

  delete(enfermarias: IEnfermarias): void {
    const modalRef = this.modalService.open(EnfermariasDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.enfermarias = enfermarias;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
