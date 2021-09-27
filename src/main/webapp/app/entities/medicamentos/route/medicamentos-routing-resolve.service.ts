import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMedicamentos, Medicamentos } from '../medicamentos.model';
import { MedicamentosService } from '../service/medicamentos.service';

@Injectable({ providedIn: 'root' })
export class MedicamentosRoutingResolveService implements Resolve<IMedicamentos> {
  constructor(protected service: MedicamentosService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMedicamentos> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((medicamentos: HttpResponse<Medicamentos>) => {
          if (medicamentos.body) {
            return of(medicamentos.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Medicamentos());
  }
}
