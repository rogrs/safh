import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ILeitos, Leitos } from '../leitos.model';
import { LeitosService } from '../service/leitos.service';

@Injectable({ providedIn: 'root' })
export class LeitosRoutingResolveService implements Resolve<ILeitos> {
  constructor(protected service: LeitosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ILeitos> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((leitos: HttpResponse<Leitos>) => {
          if (leitos.body) {
            return of(leitos.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Leitos());
  }
}
