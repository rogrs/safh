import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPrescricoes, Prescricoes } from '../prescricoes.model';
import { PrescricoesService } from '../service/prescricoes.service';

@Injectable({ providedIn: 'root' })
export class PrescricoesRoutingResolveService implements Resolve<IPrescricoes> {
  constructor(protected service: PrescricoesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPrescricoes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((prescricoes: HttpResponse<Prescricoes>) => {
          if (prescricoes.body) {
            return of(prescricoes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Prescricoes());
  }
}
