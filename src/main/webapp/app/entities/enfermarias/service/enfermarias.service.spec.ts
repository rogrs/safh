import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEnfermarias, Enfermarias } from '../enfermarias.model';

import { EnfermariasService } from './enfermarias.service';

describe('Service Tests', () => {
  describe('Enfermarias Service', () => {
    let service: EnfermariasService;
    let httpMock: HttpTestingController;
    let elemDefault: IEnfermarias;
    let expectedResult: IEnfermarias | IEnfermarias[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EnfermariasService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        enfermaria: 'AAAAAAA',
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

      it('should create a Enfermarias', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Enfermarias()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Enfermarias', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            enfermaria: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Enfermarias', () => {
        const patchObject = Object.assign({}, new Enfermarias());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Enfermarias', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            enfermaria: 'BBBBBB',
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

      it('should delete a Enfermarias', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEnfermariasToCollectionIfMissing', () => {
        it('should add a Enfermarias to an empty array', () => {
          const enfermarias: IEnfermarias = { id: 123 };
          expectedResult = service.addEnfermariasToCollectionIfMissing([], enfermarias);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(enfermarias);
        });

        it('should not add a Enfermarias to an array that contains it', () => {
          const enfermarias: IEnfermarias = { id: 123 };
          const enfermariasCollection: IEnfermarias[] = [
            {
              ...enfermarias,
            },
            { id: 456 },
          ];
          expectedResult = service.addEnfermariasToCollectionIfMissing(enfermariasCollection, enfermarias);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Enfermarias to an array that doesn't contain it", () => {
          const enfermarias: IEnfermarias = { id: 123 };
          const enfermariasCollection: IEnfermarias[] = [{ id: 456 }];
          expectedResult = service.addEnfermariasToCollectionIfMissing(enfermariasCollection, enfermarias);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(enfermarias);
        });

        it('should add only unique Enfermarias to an array', () => {
          const enfermariasArray: IEnfermarias[] = [{ id: 123 }, { id: 456 }, { id: 65196 }];
          const enfermariasCollection: IEnfermarias[] = [{ id: 123 }];
          expectedResult = service.addEnfermariasToCollectionIfMissing(enfermariasCollection, ...enfermariasArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const enfermarias: IEnfermarias = { id: 123 };
          const enfermarias2: IEnfermarias = { id: 456 };
          expectedResult = service.addEnfermariasToCollectionIfMissing([], enfermarias, enfermarias2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(enfermarias);
          expect(expectedResult).toContain(enfermarias2);
        });

        it('should accept null and undefined values', () => {
          const enfermarias: IEnfermarias = { id: 123 };
          expectedResult = service.addEnfermariasToCollectionIfMissing([], null, enfermarias, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(enfermarias);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
