import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEspecialidades } from '../especialidades.model';
import { EspecialidadesService } from '../service/especialidades.service';
import { EspecialidadesDeleteDialogComponent } from '../delete/especialidades-delete-dialog.component';

@Component({
  selector: 'jhi-especialidades',
  templateUrl: './especialidades.component.html',
})
export class EspecialidadesComponent implements OnInit {
  especialidades?: IEspecialidades[];
  isLoading = false;

  constructor(protected especialidadesService: EspecialidadesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.especialidadesService.query().subscribe(
      (res: HttpResponse<IEspecialidades[]>) => {
        this.isLoading = false;
        this.especialidades = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IEspecialidades): number {
    return item.id!;
  }

  delete(especialidades: IEspecialidades): void {
    const modalRef = this.modalService.open(EspecialidadesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.especialidades = especialidades;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
