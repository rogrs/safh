import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClinicas, Clinicas } from '../clinicas.model';
import { ClinicasService } from '../service/clinicas.service';

@Injectable({ providedIn: 'root' })
export class ClinicasRoutingResolveService implements Resolve<IClinicas> {
  constructor(protected service: ClinicasService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClinicas> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((clinicas: HttpResponse<Clinicas>) => {
          if (clinicas.body) {
            return of(clinicas.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Clinicas());
  }
}
