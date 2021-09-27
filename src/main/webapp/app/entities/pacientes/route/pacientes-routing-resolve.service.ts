import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPacientes, Pacientes } from '../pacientes.model';
import { PacientesService } from '../service/pacientes.service';

@Injectable({ providedIn: 'root' })
export class PacientesRoutingResolveService implements Resolve<IPacientes> {
  constructor(protected service: PacientesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPacientes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pacientes: HttpResponse<Pacientes>) => {
          if (pacientes.body) {
            return of(pacientes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Pacientes());
  }
}
