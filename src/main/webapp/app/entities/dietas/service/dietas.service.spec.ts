import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDietas, Dietas } from '../dietas.model';

import { DietasService } from './dietas.service';

describe('Service Tests', () => {
  describe('Dietas Service', () => {
    let service: DietasService;
    let httpMock: HttpTestingController;
    let elemDefault: IDietas;
    let expectedResult: IDietas | IDietas[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DietasService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        dieta: 'AAAAAAA',
        descricao: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Dietas', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Dietas()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Dietas', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dieta: 'BBBBBB',
            descricao: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Dietas', () => {
        const patchObject = Object.assign(
          {
            dieta: 'BBBBBB',
            descricao: 'BBBBBB',
          },
          new Dietas()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Dietas', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dieta: 'BBBBBB',
            descricao: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Dietas', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDietasToCollectionIfMissing', () => {
        it('should add a Dietas to an empty array', () => {
          const dietas: IDietas = { id: 123 };
          expectedResult = service.addDietasToCollectionIfMissing([], dietas);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dietas);
        });

        it('should not add a Dietas to an array that contains it', () => {
          const dietas: IDietas = { id: 123 };
          const dietasCollection: IDietas[] = [
            {
              ...dietas,
            },
            { id: 456 },
          ];
          expectedResult = service.addDietasToCollectionIfMissing(dietasCollection, dietas);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Dietas to an array that doesn't contain it", () => {
          const dietas: IDietas = { id: 123 };
          const dietasCollection: IDietas[] = [{ id: 456 }];
          expectedResult = service.addDietasToCollectionIfMissing(dietasCollection, dietas);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dietas);
        });

        it('should add only unique Dietas to an array', () => {
          const dietasArray: IDietas[] = [{ id: 123 }, { id: 456 }, { id: 9354 }];
          const dietasCollection: IDietas[] = [{ id: 123 }];
          expectedResult = service.addDietasToCollectionIfMissing(dietasCollection, ...dietasArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const dietas: IDietas = { id: 123 };
          const dietas2: IDietas = { id: 456 };
          expectedResult = service.addDietasToCollectionIfMissing([], dietas, dietas2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(dietas);
          expect(expectedResult).toContain(dietas2);
        });

        it('should accept null and undefined values', () => {
          const dietas: IDietas = { id: 123 };
          expectedResult = service.addDietasToCollectionIfMissing([], null, dietas, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(dietas);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
