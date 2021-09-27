import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IFabricantes, Fabricantes } from '../fabricantes.model';
import { FabricantesService } from '../service/fabricantes.service';

@Injectable({ providedIn: 'root' })
export class FabricantesRoutingResolveService implements Resolve<IFabricantes> {
  constructor(protected service: FabricantesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFabricantes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((fabricantes: HttpResponse<Fabricantes>) => {
          if (fabricantes.body) {
            return of(fabricantes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Fabricantes());
  }
}
