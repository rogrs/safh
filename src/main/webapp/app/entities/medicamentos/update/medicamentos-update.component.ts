import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IMedicamentos, Medicamentos } from '../medicamentos.model';
import { MedicamentosService } from '../service/medicamentos.service';
import { IPosologias } from 'app/entities/posologias/posologias.model';
import { PosologiasService } from 'app/entities/posologias/service/posologias.service';
import { IFabricantes } from 'app/entities/fabricantes/fabricantes.model';
import { FabricantesService } from 'app/entities/fabricantes/service/fabricantes.service';

@Component({
  selector: 'jhi-medicamentos-update',
  templateUrl: './medicamentos-update.component.html',
})
export class MedicamentosUpdateComponent implements OnInit {
  isSaving = false;

  posologiasSharedCollection: IPosologias[] = [];
  fabricantesSharedCollection: IFabricantes[] = [];

  editForm = this.fb.group({
    id: [],
    descricao: [null, [Validators.required, Validators.maxLength(100)]],
    registroMinisterioSaude: [null, [Validators.required, Validators.maxLength(60)]],
    codigoBarras: [null, [Validators.required, Validators.maxLength(13)]],
    qtdAtual: [],
    qtdMin: [],
    qtdMax: [],
    observacoes: [null, [Validators.maxLength(8000)]],
    apresentacao: [],
    posologiaPadrao: [],
    fabricantes: [],
  });

  constructor(
    protected medicamentosService: MedicamentosService,
    protected posologiasService: PosologiasService,
    protected fabricantesService: FabricantesService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medicamentos }) => {
      this.updateForm(medicamentos);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medicamentos = this.createFromForm();
    if (medicamentos.id !== undefined) {
      this.subscribeToSaveResponse(this.medicamentosService.update(medicamentos));
    } else {
      this.subscribeToSaveResponse(this.medicamentosService.create(medicamentos));
    }
  }

  trackPosologiasById(index: number, item: IPosologias): number {
    return item.id!;
  }

  trackFabricantesById(index: number, item: IFabricantes): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedicamentos>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(medicamentos: IMedicamentos): void {
    this.editForm.patchValue({
      id: medicamentos.id,
      descricao: medicamentos.descricao,
      registroMinisterioSaude: medicamentos.registroMinisterioSaude,
      codigoBarras: medicamentos.codigoBarras,
      qtdAtual: medicamentos.qtdAtual,
      qtdMin: medicamentos.qtdMin,
      qtdMax: medicamentos.qtdMax,
      observacoes: medicamentos.observacoes,
      apresentacao: medicamentos.apresentacao,
      posologiaPadrao: medicamentos.posologiaPadrao,
      fabricantes: medicamentos.fabricantes,
    });

    this.posologiasSharedCollection = this.posologiasService.addPosologiasToCollectionIfMissing(
      this.posologiasSharedCollection,
      medicamentos.posologiaPadrao
    );
    this.fabricantesSharedCollection = this.fabricantesService.addFabricantesToCollectionIfMissing(
      this.fabricantesSharedCollection,
      medicamentos.fabricantes
    );
  }

  protected loadRelationshipsOptions(): void {
    this.posologiasService
      .query()
      .pipe(map((res: HttpResponse<IPosologias[]>) => res.body ?? []))
      .pipe(
        map((posologias: IPosologias[]) =>
          this.posologiasService.addPosologiasToCollectionIfMissing(posologias, this.editForm.get('posologiaPadrao')!.value)
        )
      )
      .subscribe((posologias: IPosologias[]) => (this.posologiasSharedCollection = posologias));

    this.fabricantesService
      .query()
      .pipe(map((res: HttpResponse<IFabricantes[]>) => res.body ?? []))
      .pipe(
        map((fabricantes: IFabricantes[]) =>
          this.fabricantesService.addFabricantesToCollectionIfMissing(fabricantes, this.editForm.get('fabricantes')!.value)
        )
      )
      .subscribe((fabricantes: IFabricantes[]) => (this.fabricantesSharedCollection = fabricantes));
  }

  protected createFromForm(): IMedicamentos {
    return {
      ...new Medicamentos(),
      id: this.editForm.get(['id'])!.value,
      descricao: this.editForm.get(['descricao'])!.value,
      registroMinisterioSaude: this.editForm.get(['registroMinisterioSaude'])!.value,
      codigoBarras: this.editForm.get(['codigoBarras'])!.value,
      qtdAtual: this.editForm.get(['qtdAtual'])!.value,
      qtdMin: this.editForm.get(['qtdMin'])!.value,
      qtdMax: this.editForm.get(['qtdMax'])!.value,
      observacoes: this.editForm.get(['observacoes'])!.value,
      apresentacao: this.editForm.get(['apresentacao'])!.value,
      posologiaPadrao: this.editForm.get(['posologiaPadrao'])!.value,
      fabricantes: this.editForm.get(['fabricantes'])!.value,
    };
  }
}
