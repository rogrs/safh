import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMedicos, Medicos } from '../medicos.model';
import { MedicosService } from '../service/medicos.service';

@Injectable({ providedIn: 'root' })
export class MedicosRoutingResolveService implements Resolve<IMedicos> {
  constructor(protected service: MedicosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMedicos> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((medicos: HttpResponse<Medicos>) => {
          if (medicos.body) {
            return of(medicos.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Medicos());
  }
}
