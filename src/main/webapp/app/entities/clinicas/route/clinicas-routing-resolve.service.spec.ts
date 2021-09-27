jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IClinicas, Clinicas } from '../clinicas.model';
import { ClinicasService } from '../service/clinicas.service';

import { ClinicasRoutingResolveService } from './clinicas-routing-resolve.service';

describe('Service Tests', () => {
  describe('Clinicas routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ClinicasRoutingResolveService;
    let service: ClinicasService;
    let resultClinicas: IClinicas | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ClinicasRoutingResolveService);
      service = TestBed.inject(ClinicasService);
      resultClinicas = undefined;
    });

    describe('resolve', () => {
      it('should return IClinicas returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClinicas = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultClinicas).toEqual({ id: 123 });
      });

      it('should return new IClinicas if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClinicas = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultClinicas).toEqual(new Clinicas());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultClinicas = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultClinicas).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
