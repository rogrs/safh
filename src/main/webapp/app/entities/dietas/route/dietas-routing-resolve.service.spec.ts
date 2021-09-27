jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDietas, Dietas } from '../dietas.model';
import { DietasService } from '../service/dietas.service';

import { DietasRoutingResolveService } from './dietas-routing-resolve.service';

describe('Service Tests', () => {
  describe('Dietas routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DietasRoutingResolveService;
    let service: DietasService;
    let resultDietas: IDietas | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DietasRoutingResolveService);
      service = TestBed.inject(DietasService);
      resultDietas = undefined;
    });

    describe('resolve', () => {
      it('should return IDietas returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDietas = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDietas).toEqual({ id: 123 });
      });

      it('should return new IDietas if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDietas = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDietas).toEqual(new Dietas());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDietas = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDietas).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
