import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IEspecialidades, getEspecialidadesIdentifier } from '../especialidades.model';

export type EntityResponseType = HttpResponse<IEspecialidades>;
export type EntityArrayResponseType = HttpResponse<IEspecialidades[]>;

@Injectable({ providedIn: 'root' })
export class EspecialidadesService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/especialidades');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(especialidades: IEspecialidades): Observable<EntityResponseType> {
    return this.http.post<IEspecialidades>(this.resourceUrl, especialidades, { observe: 'response' });
  }

  update(especialidades: IEspecialidades): Observable<EntityResponseType> {
    return this.http.put<IEspecialidades>(`${this.resourceUrl}/${getEspecialidadesIdentifier(especialidades) as number}`, especialidades, {
      observe: 'response',
    });
  }

  partialUpdate(especialidades: IEspecialidades): Observable<EntityResponseType> {
    return this.http.patch<IEspecialidades>(
      `${this.resourceUrl}/${getEspecialidadesIdentifier(especialidades) as number}`,
      especialidades,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEspecialidades>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEspecialidades[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addEspecialidadesToCollectionIfMissing(
    especialidadesCollection: IEspecialidades[],
    ...especialidadesToCheck: (IEspecialidades | null | undefined)[]
  ): IEspecialidades[] {
    const especialidades: IEspecialidades[] = especialidadesToCheck.filter(isPresent);
    if (especialidades.length > 0) {
      const especialidadesCollectionIdentifiers = especialidadesCollection.map(
        especialidadesItem => getEspecialidadesIdentifier(especialidadesItem)!
      );
      const especialidadesToAdd = especialidades.filter(especialidadesItem => {
        const especialidadesIdentifier = getEspecialidadesIdentifier(especialidadesItem);
        if (especialidadesIdentifier == null || especialidadesCollectionIdentifiers.includes(especialidadesIdentifier)) {
          return false;
        }
        especialidadesCollectionIdentifiers.push(especialidadesIdentifier);
        return true;
      });
      return [...especialidadesToAdd, ...especialidadesCollection];
    }
    return especialidadesCollection;
  }
}
