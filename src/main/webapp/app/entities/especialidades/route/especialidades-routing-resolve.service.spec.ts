jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEspecialidades, Especialidades } from '../especialidades.model';
import { EspecialidadesService } from '../service/especialidades.service';

import { EspecialidadesRoutingResolveService } from './especialidades-routing-resolve.service';

describe('Service Tests', () => {
  describe('Especialidades routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EspecialidadesRoutingResolveService;
    let service: EspecialidadesService;
    let resultEspecialidades: IEspecialidades | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EspecialidadesRoutingResolveService);
      service = TestBed.inject(EspecialidadesService);
      resultEspecialidades = undefined;
    });

    describe('resolve', () => {
      it('should return IEspecialidades returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEspecialidades = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEspecialidades).toEqual({ id: 123 });
      });

      it('should return new IEspecialidades if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEspecialidades = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEspecialidades).toEqual(new Especialidades());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEspecialidades = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEspecialidades).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
