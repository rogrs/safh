import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { InternacoesDetalhes } from './internacoes-detalhes.model';
import { InternacoesDetalhesService } from './internacoes-detalhes.service';

@Injectable()
export class InternacoesDetalhesPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private internacoesDetalhesService: InternacoesDetalhesService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.internacoesDetalhesService.find(id).subscribe((internacoesDetalhes) => {
                    if (internacoesDetalhes.dataDetalhe) {
                        internacoesDetalhes.dataDetalhe = {
                            year: internacoesDetalhes.dataDetalhe.getFullYear(),
                            month: internacoesDetalhes.dataDetalhe.getMonth() + 1,
                            day: internacoesDetalhes.dataDetalhe.getDate()
                        };
                    }
                    if (internacoesDetalhes.horario) {
                        internacoesDetalhes.horario = {
                            year: internacoesDetalhes.horario.getFullYear(),
                            month: internacoesDetalhes.horario.getMonth() + 1,
                            day: internacoesDetalhes.horario.getDate()
                        };
                    }
                    this.ngbModalRef = this.internacoesDetalhesModalRef(component, internacoesDetalhes);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.internacoesDetalhesModalRef(component, new InternacoesDetalhes());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    internacoesDetalhesModalRef(component: Component, internacoesDetalhes: InternacoesDetalhes): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.internacoesDetalhes = internacoesDetalhes;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
