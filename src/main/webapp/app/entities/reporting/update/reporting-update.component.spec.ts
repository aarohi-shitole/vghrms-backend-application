import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ReportingFormService } from './reporting-form.service';
import { ReportingService } from '../service/reporting.service';
import { IReporting } from '../reporting.model';

import { ReportingUpdateComponent } from './reporting-update.component';

describe('Reporting Management Update Component', () => {
  let comp: ReportingUpdateComponent;
  let fixture: ComponentFixture<ReportingUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let reportingFormService: ReportingFormService;
  let reportingService: ReportingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ReportingUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ReportingUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ReportingUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    reportingFormService = TestBed.inject(ReportingFormService);
    reportingService = TestBed.inject(ReportingService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const reporting: IReporting = { id: 456 };

      activatedRoute.data = of({ reporting });
      comp.ngOnInit();

      expect(comp.reporting).toEqual(reporting);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReporting>>();
      const reporting = { id: 123 };
      jest.spyOn(reportingFormService, 'getReporting').mockReturnValue(reporting);
      jest.spyOn(reportingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reporting });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reporting }));
      saveSubject.complete();

      // THEN
      expect(reportingFormService.getReporting).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(reportingService.update).toHaveBeenCalledWith(expect.objectContaining(reporting));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReporting>>();
      const reporting = { id: 123 };
      jest.spyOn(reportingFormService, 'getReporting').mockReturnValue({ id: null });
      jest.spyOn(reportingService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reporting: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: reporting }));
      saveSubject.complete();

      // THEN
      expect(reportingFormService.getReporting).toHaveBeenCalled();
      expect(reportingService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IReporting>>();
      const reporting = { id: 123 };
      jest.spyOn(reportingService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ reporting });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(reportingService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
