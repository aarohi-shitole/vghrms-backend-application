import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PersonalDetailsFormService } from './personal-details-form.service';
import { PersonalDetailsService } from '../service/personal-details.service';
import { IPersonalDetails } from '../personal-details.model';

import { PersonalDetailsUpdateComponent } from './personal-details-update.component';

describe('PersonalDetails Management Update Component', () => {
  let comp: PersonalDetailsUpdateComponent;
  let fixture: ComponentFixture<PersonalDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let personalDetailsFormService: PersonalDetailsFormService;
  let personalDetailsService: PersonalDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PersonalDetailsUpdateComponent],
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
      .overrideTemplate(PersonalDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PersonalDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    personalDetailsFormService = TestBed.inject(PersonalDetailsFormService);
    personalDetailsService = TestBed.inject(PersonalDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const personalDetails: IPersonalDetails = { id: 456 };

      activatedRoute.data = of({ personalDetails });
      comp.ngOnInit();

      expect(comp.personalDetails).toEqual(personalDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersonalDetails>>();
      const personalDetails = { id: 123 };
      jest.spyOn(personalDetailsFormService, 'getPersonalDetails').mockReturnValue(personalDetails);
      jest.spyOn(personalDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personalDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personalDetails }));
      saveSubject.complete();

      // THEN
      expect(personalDetailsFormService.getPersonalDetails).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(personalDetailsService.update).toHaveBeenCalledWith(expect.objectContaining(personalDetails));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersonalDetails>>();
      const personalDetails = { id: 123 };
      jest.spyOn(personalDetailsFormService, 'getPersonalDetails').mockReturnValue({ id: null });
      jest.spyOn(personalDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personalDetails: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personalDetails }));
      saveSubject.complete();

      // THEN
      expect(personalDetailsFormService.getPersonalDetails).toHaveBeenCalled();
      expect(personalDetailsService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersonalDetails>>();
      const personalDetails = { id: 123 };
      jest.spyOn(personalDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personalDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(personalDetailsService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
