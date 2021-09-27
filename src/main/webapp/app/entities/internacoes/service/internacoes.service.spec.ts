import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IInternacoes, Internacoes } from '../internacoes.model';

import { InternacoesService } from './internacoes.service';

describe('Service Tests', () => {
  describe('Internacoes Service', () => {
    let service: InternacoesService;
    let httpMock: HttpTestingController;
    let elemDefault: IInternacoes;
    let expectedResult: IInternacoes | IInternacoes[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(InternacoesService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        dataInternacao: currentDate,
        descricao: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            dataInternacao: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Internacoes', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            dataInternacao: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataInternacao: currentDate,
          },
          returnedFromService
        );

        service.create(new Internacoes()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Internacoes', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dataInternacao: currentDate.format(DATE_FORMAT),
            descricao: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataInternacao: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Internacoes', () => {
        const patchObject = Object.assign(
          {
            dataInternacao: currentDate.format(DATE_FORMAT),
            descricao: 'BBBBBB',
          },
          new Internacoes()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            dataInternacao: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Internacoes', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dataInternacao: currentDate.format(DATE_FORMAT),
            descricao: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            dataInternacao: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Internacoes', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addInternacoesToCollectionIfMissing', () => {
        it('should add a Internacoes to an empty array', () => {
          const internacoes: IInternacoes = { id: 123 };
          expectedResult = service.addInternacoesToCollectionIfMissing([], internacoes);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(internacoes);
        });

        it('should not add a Internacoes to an array that contains it', () => {
          const internacoes: IInternacoes = { id: 123 };
          const internacoesCollection: IInternacoes[] = [
            {
              ...internacoes,
            },
            { id: 456 },
          ];
          expectedResult = service.addInternacoesToCollectionIfMissing(internacoesCollection, internacoes);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Internacoes to an array that doesn't contain it", () => {
          const internacoes: IInternacoes = { id: 123 };
          const internacoesCollection: IInternacoes[] = [{ id: 456 }];
          expectedResult = service.addInternacoesToCollectionIfMissing(internacoesCollection, internacoes);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(internacoes);
        });

        it('should add only unique Internacoes to an array', () => {
          const internacoesArray: IInternacoes[] = [{ id: 123 }, { id: 456 }, { id: 2986 }];
          const internacoesCollection: IInternacoes[] = [{ id: 123 }];
          expectedResult = service.addInternacoesToCollectionIfMissing(internacoesCollection, ...internacoesArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const internacoes: IInternacoes = { id: 123 };
          const internacoes2: IInternacoes = { id: 456 };
          expectedResult = service.addInternacoesToCollectionIfMissing([], internacoes, internacoes2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(internacoes);
          expect(expectedResult).toContain(internacoes2);
        });

        it('should accept null and undefined values', () => {
          const internacoes: IInternacoes = { id: 123 };
          expectedResult = service.addInternacoesToCollectionIfMissing([], null, internacoes, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(internacoes);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
