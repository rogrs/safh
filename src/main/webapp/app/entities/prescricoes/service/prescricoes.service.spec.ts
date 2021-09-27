import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPrescricoes, Prescricoes } from '../prescricoes.model';

import { PrescricoesService } from './prescricoes.service';

describe('Service Tests', () => {
  describe('Prescricoes Service', () => {
    let service: PrescricoesService;
    let httpMock: HttpTestingController;
    let elemDefault: IPrescricoes;
    let expectedResult: IPrescricoes | IPrescricoes[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(PrescricoesService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        prescricao: 'AAAAAAA',
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

      it('should create a Prescricoes', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Prescricoes()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Prescricoes', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            prescricao: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Prescricoes', () => {
        const patchObject = Object.assign(
          {
            prescricao: 'BBBBBB',
          },
          new Prescricoes()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Prescricoes', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            prescricao: 'BBBBBB',
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

      it('should delete a Prescricoes', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addPrescricoesToCollectionIfMissing', () => {
        it('should add a Prescricoes to an empty array', () => {
          const prescricoes: IPrescricoes = { id: 123 };
          expectedResult = service.addPrescricoesToCollectionIfMissing([], prescricoes);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(prescricoes);
        });

        it('should not add a Prescricoes to an array that contains it', () => {
          const prescricoes: IPrescricoes = { id: 123 };
          const prescricoesCollection: IPrescricoes[] = [
            {
              ...prescricoes,
            },
            { id: 456 },
          ];
          expectedResult = service.addPrescricoesToCollectionIfMissing(prescricoesCollection, prescricoes);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Prescricoes to an array that doesn't contain it", () => {
          const prescricoes: IPrescricoes = { id: 123 };
          const prescricoesCollection: IPrescricoes[] = [{ id: 456 }];
          expectedResult = service.addPrescricoesToCollectionIfMissing(prescricoesCollection, prescricoes);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(prescricoes);
        });

        it('should add only unique Prescricoes to an array', () => {
          const prescricoesArray: IPrescricoes[] = [{ id: 123 }, { id: 456 }, { id: 271 }];
          const prescricoesCollection: IPrescricoes[] = [{ id: 123 }];
          expectedResult = service.addPrescricoesToCollectionIfMissing(prescricoesCollection, ...prescricoesArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const prescricoes: IPrescricoes = { id: 123 };
          const prescricoes2: IPrescricoes = { id: 456 };
          expectedResult = service.addPrescricoesToCollectionIfMissing([], prescricoes, prescricoes2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(prescricoes);
          expect(expectedResult).toContain(prescricoes2);
        });

        it('should accept null and undefined values', () => {
          const prescricoes: IPrescricoes = { id: 123 };
          expectedResult = service.addPrescricoesToCollectionIfMissing([], null, prescricoes, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(prescricoes);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
