import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEspecialidades, Especialidades } from '../especialidades.model';
import { EspecialidadesService } from '../service/especialidades.service';

@Injectable({ providedIn: 'root' })
export class EspecialidadesRoutingResolveService implements Resolve<IEspecialidades> {
  constructor(protected service: EspecialidadesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEspecialidades> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((especialidades: HttpResponse<Especialidades>) => {
          if (especialidades.body) {
            return of(especialidades.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Especialidades());
  }
}
