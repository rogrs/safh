import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInternacoesDetalhes, InternacoesDetalhes } from '../internacoes-detalhes.model';
import { InternacoesDetalhesService } from '../service/internacoes-detalhes.service';

@Injectable({ providedIn: 'root' })
export class InternacoesDetalhesRoutingResolveService implements Resolve<IInternacoesDetalhes> {
  constructor(protected service: InternacoesDetalhesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IInternacoesDetalhes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((internacoesDetalhes: HttpResponse<InternacoesDetalhes>) => {
          if (internacoesDetalhes.body) {
            return of(internacoesDetalhes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new InternacoesDetalhes());
  }
}
