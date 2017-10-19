/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SafhTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { EnfermariasDetailComponent } from '../../../../../../main/webapp/app/entities/enfermarias/enfermarias-detail.component';
import { EnfermariasService } from '../../../../../../main/webapp/app/entities/enfermarias/enfermarias.service';
import { Enfermarias } from '../../../../../../main/webapp/app/entities/enfermarias/enfermarias.model';

describe('Component Tests', () => {

    describe('Enfermarias Management Detail Component', () => {
        let comp: EnfermariasDetailComponent;
        let fixture: ComponentFixture<EnfermariasDetailComponent>;
        let service: EnfermariasService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SafhTestModule],
                declarations: [EnfermariasDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    EnfermariasService,
                    JhiEventManager
                ]
            }).overrideTemplate(EnfermariasDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(EnfermariasDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EnfermariasService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Enfermarias(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.enfermarias).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
