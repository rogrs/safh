/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { SafhTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { DietasDetailComponent } from '../../../../../../main/webapp/app/entities/dietas/dietas-detail.component';
import { DietasService } from '../../../../../../main/webapp/app/entities/dietas/dietas.service';
import { Dietas } from '../../../../../../main/webapp/app/entities/dietas/dietas.model';

describe('Component Tests', () => {

    describe('Dietas Management Detail Component', () => {
        let comp: DietasDetailComponent;
        let fixture: ComponentFixture<DietasDetailComponent>;
        let service: DietasService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SafhTestModule],
                declarations: [DietasDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    DietasService,
                    JhiEventManager
                ]
            }).overrideTemplate(DietasDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(DietasDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(DietasService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Dietas(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.dietas).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
