import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IInternacoesDetalhes, InternacoesDetalhes } from '../internacoes-detalhes.model';
import { InternacoesDetalhesService } from '../service/internacoes-detalhes.service';
import { IInternacoes } from 'app/entities/internacoes/internacoes.model';
import { InternacoesService } from 'app/entities/internacoes/service/internacoes.service';
import { IDietas } from 'app/entities/dietas/dietas.model';
import { DietasService } from 'app/entities/dietas/service/dietas.service';
import { IPrescricoes } from 'app/entities/prescricoes/prescricoes.model';
import { PrescricoesService } from 'app/entities/prescricoes/service/prescricoes.service';
import { IPosologias } from 'app/entities/posologias/posologias.model';
import { PosologiasService } from 'app/entities/posologias/service/posologias.service';

@Component({
  selector: 'jhi-internacoes-detalhes-update',
  templateUrl: './internacoes-detalhes-update.component.html',
})
export class InternacoesDetalhesUpdateComponent implements OnInit {
  isSaving = false;

  internacoesSharedCollection: IInternacoes[] = [];
  dietasSharedCollection: IDietas[] = [];
  prescricoesSharedCollection: IPrescricoes[] = [];
  posologiasSharedCollection: IPosologias[] = [];

  editForm = this.fb.group({
    id: [],
    dataDetalhe: [null, [Validators.required]],
    horario: [null, [Validators.required]],
    qtd: [null, [Validators.required]],
    internacoes: [],
    dietas: [],
    prescricoes: [],
    posologias: [],
  });

  constructor(
    protected internacoesDetalhesService: InternacoesDetalhesService,
    protected internacoesService: InternacoesService,
    protected dietasService: DietasService,
    protected prescricoesService: PrescricoesService,
    protected posologiasService: PosologiasService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ internacoesDetalhes }) => {
      this.updateForm(internacoesDetalhes);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const internacoesDetalhes = this.createFromForm();
    if (internacoesDetalhes.id !== undefined) {
      this.subscribeToSaveResponse(this.internacoesDetalhesService.update(internacoesDetalhes));
    } else {
      this.subscribeToSaveResponse(this.internacoesDetalhesService.create(internacoesDetalhes));
    }
  }

  trackInternacoesById(index: number, item: IInternacoes): number {
    return item.id!;
  }

  trackDietasById(index: number, item: IDietas): number {
    return item.id!;
  }

  trackPrescricoesById(index: number, item: IPrescricoes): number {
    return item.id!;
  }

  trackPosologiasById(index: number, item: IPosologias): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInternacoesDetalhes>>): void {
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

  protected updateForm(internacoesDetalhes: IInternacoesDetalhes): void {
    this.editForm.patchValue({
      id: internacoesDetalhes.id,
      dataDetalhe: internacoesDetalhes.dataDetalhe,
      horario: internacoesDetalhes.horario,
      qtd: internacoesDetalhes.qtd,
      internacoes: internacoesDetalhes.internacoes,
      dietas: internacoesDetalhes.dietas,
      prescricoes: internacoesDetalhes.prescricoes,
      posologias: internacoesDetalhes.posologias,
    });

    this.internacoesSharedCollection = this.internacoesService.addInternacoesToCollectionIfMissing(
      this.internacoesSharedCollection,
      internacoesDetalhes.internacoes
    );
    this.dietasSharedCollection = this.dietasService.addDietasToCollectionIfMissing(
      this.dietasSharedCollection,
      internacoesDetalhes.dietas
    );
    this.prescricoesSharedCollection = this.prescricoesService.addPrescricoesToCollectionIfMissing(
      this.prescricoesSharedCollection,
      internacoesDetalhes.prescricoes
    );
    this.posologiasSharedCollection = this.posologiasService.addPosologiasToCollectionIfMissing(
      this.posologiasSharedCollection,
      internacoesDetalhes.posologias
    );
  }

  protected loadRelationshipsOptions(): void {
    this.internacoesService
      .query()
      .pipe(map((res: HttpResponse<IInternacoes[]>) => res.body ?? []))
      .pipe(
        map((internacoes: IInternacoes[]) =>
          this.internacoesService.addInternacoesToCollectionIfMissing(internacoes, this.editForm.get('internacoes')!.value)
        )
      )
      .subscribe((internacoes: IInternacoes[]) => (this.internacoesSharedCollection = internacoes));

    this.dietasService
      .query()
      .pipe(map((res: HttpResponse<IDietas[]>) => res.body ?? []))
      .pipe(map((dietas: IDietas[]) => this.dietasService.addDietasToCollectionIfMissing(dietas, this.editForm.get('dietas')!.value)))
      .subscribe((dietas: IDietas[]) => (this.dietasSharedCollection = dietas));

    this.prescricoesService
      .query()
      .pipe(map((res: HttpResponse<IPrescricoes[]>) => res.body ?? []))
      .pipe(
        map((prescricoes: IPrescricoes[]) =>
          this.prescricoesService.addPrescricoesToCollectionIfMissing(prescricoes, this.editForm.get('prescricoes')!.value)
        )
      )
      .subscribe((prescricoes: IPrescricoes[]) => (this.prescricoesSharedCollection = prescricoes));

    this.posologiasService
      .query()
      .pipe(map((res: HttpResponse<IPosologias[]>) => res.body ?? []))
      .pipe(
        map((posologias: IPosologias[]) =>
          this.posologiasService.addPosologiasToCollectionIfMissing(posologias, this.editForm.get('posologias')!.value)
        )
      )
      .subscribe((posologias: IPosologias[]) => (this.posologiasSharedCollection = posologias));
  }

  protected createFromForm(): IInternacoesDetalhes {
    return {
      ...new InternacoesDetalhes(),
      id: this.editForm.get(['id'])!.value,
      dataDetalhe: this.editForm.get(['dataDetalhe'])!.value,
      horario: this.editForm.get(['horario'])!.value,
      qtd: this.editForm.get(['qtd'])!.value,
      internacoes: this.editForm.get(['internacoes'])!.value,
      dietas: this.editForm.get(['dietas'])!.value,
      prescricoes: this.editForm.get(['prescricoes'])!.value,
      posologias: this.editForm.get(['posologias'])!.value,
    };
  }
}
