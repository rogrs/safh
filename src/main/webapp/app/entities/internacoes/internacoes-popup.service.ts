import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Internacoes } from './internacoes.model';
import { InternacoesService } from './internacoes.service';

@Injectable()
export class InternacoesPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private internacoesService: InternacoesService

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
                this.internacoesService.find(id).subscribe((internacoes) => {
                    if (internacoes.dataInternacao) {
                        internacoes.dataInternacao = {
                            year: internacoes.dataInternacao.getFullYear(),
                            month: internacoes.dataInternacao.getMonth() + 1,
                            day: internacoes.dataInternacao.getDate()
                        };
                    }
                    this.ngbModalRef = this.internacoesModalRef(component, internacoes);
                    resolve(this.ngbModalRef);
                });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.internacoesModalRef(component, new Internacoes());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    internacoesModalRef(component: Component, internacoes: Internacoes): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.internacoes = internacoes;
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
