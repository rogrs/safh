/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SafhTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { LeitosDetailComponent } from '../../../../../../main/webapp/app/entities/leitos/leitos-detail.component';
import { LeitosService } from '../../../../../../main/webapp/app/entities/leitos/leitos.service';
import { Leitos } from '../../../../../../main/webapp/app/entities/leitos/leitos.model';

describe('Component Tests', () => {

    describe('Leitos Management Detail Component', () => {
        let comp: LeitosDetailComponent;
        let fixture: ComponentFixture<LeitosDetailComponent>;
        let service: LeitosService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SafhTestModule],
                declarations: [LeitosDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    LeitosService,
                    JhiEventManager
                ]
            }).overrideTemplate(LeitosDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(LeitosDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(LeitosService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Leitos(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.leitos).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
