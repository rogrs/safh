/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SafhTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { InternacoesDetailComponent } from '../../../../../../main/webapp/app/entities/internacoes/internacoes-detail.component';
import { InternacoesService } from '../../../../../../main/webapp/app/entities/internacoes/internacoes.service';
import { Internacoes } from '../../../../../../main/webapp/app/entities/internacoes/internacoes.model';

describe('Component Tests', () => {

    describe('Internacoes Management Detail Component', () => {
        let comp: InternacoesDetailComponent;
        let fixture: ComponentFixture<InternacoesDetailComponent>;
        let service: InternacoesService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SafhTestModule],
                declarations: [InternacoesDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    InternacoesService,
                    JhiEventManager
                ]
            }).overrideTemplate(InternacoesDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(InternacoesDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InternacoesService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Internacoes(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.internacoes).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
