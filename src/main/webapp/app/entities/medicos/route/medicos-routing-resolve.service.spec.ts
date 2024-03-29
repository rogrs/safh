jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IMedicos, Medicos } from '../medicos.model';
import { MedicosService } from '../service/medicos.service';

import { MedicosRoutingResolveService } from './medicos-routing-resolve.service';

describe('Service Tests', () => {
  describe('Medicos routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: MedicosRoutingResolveService;
    let service: MedicosService;
    let resultMedicos: IMedicos | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(MedicosRoutingResolveService);
      service = TestBed.inject(MedicosService);
      resultMedicos = undefined;
    });

    describe('resolve', () => {
      it('should return IMedicos returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMedicos = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMedicos).toEqual({ id: 123 });
      });

      it('should return new IMedicos if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMedicos = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultMedicos).toEqual(new Medicos());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultMedicos = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultMedicos).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
