import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPosologias, Posologias } from '../posologias.model';
import { PosologiasService } from '../service/posologias.service';

@Injectable({ providedIn: 'root' })
export class PosologiasRoutingResolveService implements Resolve<IPosologias> {
  constructor(protected service: PosologiasService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPosologias> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((posologias: HttpResponse<Posologias>) => {
          if (posologias.body) {
            return of(posologias.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Posologias());
  }
}
