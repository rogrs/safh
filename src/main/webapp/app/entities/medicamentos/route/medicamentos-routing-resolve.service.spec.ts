jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMedicamentos, Medicamentos } from '../medicamentos.model';
import { MedicamentosService } from '../service/medicamentos.service';

import { MedicamentosRoutingResolveService } from './medicamentos-routing-resolve.service';

describe('Service Tests', () => {
  describe('Medicamentos routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MedicamentosRoutingResolveService;
    let service: MedicamentosService;
    let resultMedicamentos: IMedicamentos | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MedicamentosRoutingResolveService);
      service = TestBed.inject(MedicamentosService);
      resultMedicamentos = undefined;
    });

    describe('resolve', () => {
      it('should return IMedicamentos returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMedicamentos = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMedicamentos).toEqual({ id: 123 });
      });

      it('should return new IMedicamentos if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMedicamentos = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMedicamentos).toEqual(new Medicamentos());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMedicamentos = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMedicamentos).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
