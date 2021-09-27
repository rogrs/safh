import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDietas, Dietas } from '../dietas.model';
import { DietasService } from '../service/dietas.service';

@Injectable({ providedIn: 'root' })
export class DietasRoutingResolveService implements Resolve<IDietas> {
  constructor(protected service: DietasService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDietas> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((dietas: HttpResponse<Dietas>) => {
          if (dietas.body) {
            return of(dietas.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Dietas());
  }
}
