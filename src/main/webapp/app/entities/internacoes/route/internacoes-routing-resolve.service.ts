import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInternacoes, Internacoes } from '../internacoes.model';
import { InternacoesService } from '../service/internacoes.service';

@Injectable({ providedIn: 'root' })
export class InternacoesRoutingResolveService implements Resolve<IInternacoes> {
  constructor(protected service: InternacoesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInternacoes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((internacoes: HttpResponse<Internacoes>) => {
          if (internacoes.body) {
            return of(internacoes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Internacoes());
  }
}
