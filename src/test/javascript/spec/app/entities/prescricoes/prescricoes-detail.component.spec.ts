/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SafhTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { PrescricoesDetailComponent } from '../../../../../../main/webapp/app/entities/prescricoes/prescricoes-detail.component';
import { PrescricoesService } from '../../../../../../main/webapp/app/entities/prescricoes/prescricoes.service';
import { Prescricoes } from '../../../../../../main/webapp/app/entities/prescricoes/prescricoes.model';

describe('Component Tests', () => {

    describe('Prescricoes Management Detail Component', () => {
        let comp: PrescricoesDetailComponent;
        let fixture: ComponentFixture<PrescricoesDetailComponent>;
        let service: PrescricoesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SafhTestModule],
                declarations: [PrescricoesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    PrescricoesService,
                    JhiEventManager
                ]
            }).overrideTemplate(PrescricoesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(PrescricoesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PrescricoesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Prescricoes(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.prescricoes).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
