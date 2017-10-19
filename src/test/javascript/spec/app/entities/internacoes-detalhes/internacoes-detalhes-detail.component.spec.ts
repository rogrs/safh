/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SafhTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { InternacoesDetalhesDetailComponent } from '../../../../../../main/webapp/app/entities/internacoes-detalhes/internacoes-detalhes-detail.component';
import { InternacoesDetalhesService } from '../../../../../../main/webapp/app/entities/internacoes-detalhes/internacoes-detalhes.service';
import { InternacoesDetalhes } from '../../../../../../main/webapp/app/entities/internacoes-detalhes/internacoes-detalhes.model';

describe('Component Tests', () => {

    describe('InternacoesDetalhes Management Detail Component', () => {
        let comp: InternacoesDetalhesDetailComponent;
        let fixture: ComponentFixture<InternacoesDetalhesDetailComponent>;
        let service: InternacoesDetalhesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SafhTestModule],
                declarations: [InternacoesDetalhesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    InternacoesDetalhesService,
                    JhiEventManager
                ]
            }).overrideTemplate(InternacoesDetalhesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(InternacoesDetalhesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InternacoesDetalhesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new InternacoesDetalhes(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.internacoesDetalhes).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
