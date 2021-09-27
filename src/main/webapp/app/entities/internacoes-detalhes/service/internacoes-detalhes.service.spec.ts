import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IInternacoesDetalhes, InternacoesDetalhes } from '../internacoes-detalhes.model';

import { InternacoesDetalhesService } from './internacoes-detalhes.service';

describe('Service Tests', () => {
  describe('InternacoesDetalhes Service', () => {
    let service: InternacoesDetalhesService;
    let httpMock: HttpTestingController;
    let elemDefault: IInternacoesDetalhes;
    let expectedResult: IInternacoesDetalhes | IInternacoesDetalhes[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(InternacoesDetalhesService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        dataDetalhe: currentDate,
        horario: currentDate,
        qtd: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dataDetalhe: currentDate.format(DATE_FORMAT),
            horario: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a InternacoesDetalhes', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dataDetalhe: currentDate.format(DATE_FORMAT),
            horario: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataDetalhe: currentDate,
            horario: currentDate,
          },
          returnedFromService
        );

        service.create(new InternacoesDetalhes()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a InternacoesDetalhes', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dataDetalhe: currentDate.format(DATE_FORMAT),
            horario: currentDate.format(DATE_FORMAT),
            qtd: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataDetalhe: currentDate,
            horario: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a InternacoesDetalhes', () => {
        const patchObject = Object.assign({}, new InternacoesDetalhes());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dataDetalhe: currentDate,
            horario: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of InternacoesDetalhes', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dataDetalhe: currentDate.format(DATE_FORMAT),
            horario: currentDate.format(DATE_FORMAT),
            qtd: 1,
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataDetalhe: currentDate,
            horario: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a InternacoesDetalhes', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addInternacoesDetalhesToCollectionIfMissing', () => {
        it('should add a InternacoesDetalhes to an empty array', () => {
          const internacoesDetalhes: IInternacoesDetalhes = { id: 123 };
          expectedResult = service.addInternacoesDetalhesToCollectionIfMissing([], internacoesDetalhes);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(internacoesDetalhes);
        });

        it('should not add a InternacoesDetalhes to an array that contains it', () => {
          const internacoesDetalhes: IInternacoesDetalhes = { id: 123 };
          const internacoesDetalhesCollection: IInternacoesDetalhes[] = [
            {
              ...internacoesDetalhes,
            },
            { id: 456 },
          ];
          expectedResult = service.addInternacoesDetalhesToCollectionIfMissing(internacoesDetalhesCollection, internacoesDetalhes);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a InternacoesDetalhes to an array that doesn't contain it", () => {
          const internacoesDetalhes: IInternacoesDetalhes = { id: 123 };
          const internacoesDetalhesCollection: IInternacoesDetalhes[] = [{ id: 456 }];
          expectedResult = service.addInternacoesDetalhesToCollectionIfMissing(internacoesDetalhesCollection, internacoesDetalhes);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(internacoesDetalhes);
        });

        it('should add only unique InternacoesDetalhes to an array', () => {
          const internacoesDetalhesArray: IInternacoesDetalhes[] = [{ id: 123 }, { id: 456 }, { id: 21154 }];
          const internacoesDetalhesCollection: IInternacoesDetalhes[] = [{ id: 123 }];
          expectedResult = service.addInternacoesDetalhesToCollectionIfMissing(internacoesDetalhesCollection, ...internacoesDetalhesArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const internacoesDetalhes: IInternacoesDetalhes = { id: 123 };
          const internacoesDetalhes2: IInternacoesDetalhes = { id: 456 };
          expectedResult = service.addInternacoesDetalhesToCollectionIfMissing([], internacoesDetalhes, internacoesDetalhes2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(internacoesDetalhes);
          expect(expectedResult).toContain(internacoesDetalhes2);
        });

        it('should accept null and undefined values', () => {
          const internacoesDetalhes: IInternacoesDetalhes = { id: 123 };
          expectedResult = service.addInternacoesDetalhesToCollectionIfMissing([], null, internacoesDetalhes, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(internacoesDetalhes);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
