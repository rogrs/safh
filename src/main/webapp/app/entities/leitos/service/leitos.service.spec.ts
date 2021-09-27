import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ILeitos, Leitos } from '../leitos.model';

import { LeitosService } from './leitos.service';

describe('Service Tests', () => {
  describe('Leitos Service', () => {
    let service: LeitosService;
    let httpMock: HttpTestingController;
    let elemDefault: ILeitos;
    let expectedResult: ILeitos | ILeitos[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(LeitosService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        leito: 'AAAAAAA',
        tipo: 'AAAAAAA',
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

      it('should create a Leitos', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Leitos()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Leitos', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            leito: 'BBBBBB',
            tipo: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Leitos', () => {
        const patchObject = Object.assign(
          {
            leito: 'BBBBBB',
            tipo: 'BBBBBB',
          },
          new Leitos()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Leitos', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            leito: 'BBBBBB',
            tipo: 'BBBBBB',
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

      it('should delete a Leitos', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addLeitosToCollectionIfMissing', () => {
        it('should add a Leitos to an empty array', () => {
          const leitos: ILeitos = { id: 123 };
          expectedResult = service.addLeitosToCollectionIfMissing([], leitos);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(leitos);
        });

        it('should not add a Leitos to an array that contains it', () => {
          const leitos: ILeitos = { id: 123 };
          const leitosCollection: ILeitos[] = [
            {
              ...leitos,
            },
            { id: 456 },
          ];
          expectedResult = service.addLeitosToCollectionIfMissing(leitosCollection, leitos);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Leitos to an array that doesn't contain it", () => {
          const leitos: ILeitos = { id: 123 };
          const leitosCollection: ILeitos[] = [{ id: 456 }];
          expectedResult = service.addLeitosToCollectionIfMissing(leitosCollection, leitos);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(leitos);
        });

        it('should add only unique Leitos to an array', () => {
          const leitosArray: ILeitos[] = [{ id: 123 }, { id: 456 }, { id: 89697 }];
          const leitosCollection: ILeitos[] = [{ id: 123 }];
          expectedResult = service.addLeitosToCollectionIfMissing(leitosCollection, ...leitosArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const leitos: ILeitos = { id: 123 };
          const leitos2: ILeitos = { id: 456 };
          expectedResult = service.addLeitosToCollectionIfMissing([], leitos, leitos2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(leitos);
          expect(expectedResult).toContain(leitos2);
        });

        it('should accept null and undefined values', () => {
          const leitos: ILeitos = { id: 123 };
          expectedResult = service.addLeitosToCollectionIfMissing([], null, leitos, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(leitos);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
