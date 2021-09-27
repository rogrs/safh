import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IFabricantes } from '../fabricantes.model';
import { FabricantesService } from '../service/fabricantes.service';
import { FabricantesDeleteDialogComponent } from '../delete/fabricantes-delete-dialog.component';

@Component({
  selector: 'jhi-fabricantes',
  templateUrl: './fabricantes.component.html',
})
export class FabricantesComponent implements OnInit {
  fabricantes?: IFabricantes[];
  isLoading = false;

  constructor(protected fabricantesService: FabricantesService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.fabricantesService.query().subscribe(
      (res: HttpResponse<IFabricantes[]>) => {
        this.isLoading = false;
        this.fabricantes = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IFabricantes): number {
    return item.id!;
  }

  delete(fabricantes: IFabricantes): void {
    const modalRef = this.modalService.open(FabricantesDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.fabricantes = fabricantes;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
