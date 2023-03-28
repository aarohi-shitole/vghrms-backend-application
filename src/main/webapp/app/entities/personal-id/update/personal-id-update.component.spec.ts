import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PersonalIdFormService } from './personal-id-form.service';
import { PersonalIdService } from '../service/personal-id.service';
import { IPersonalId } from '../personal-id.model';

import { PersonalIdUpdateComponent } from './personal-id-update.component';

describe('PersonalId Management Update Component', () => {
  let comp: PersonalIdUpdateComponent;
  let fixture: ComponentFixture<PersonalIdUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let personalIdFormService: PersonalIdFormService;
  let personalIdService: PersonalIdService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PersonalIdUpdateComponent],
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
      .overrideTemplate(PersonalIdUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PersonalIdUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    personalIdFormService = TestBed.inject(PersonalIdFormService);
    personalIdService = TestBed.inject(PersonalIdService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const personalId: IPersonalId = { id: 456 };

      activatedRoute.data = of({ personalId });
      comp.ngOnInit();

      expect(comp.personalId).toEqual(personalId);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersonalId>>();
      const personalId = { id: 123 };
      jest.spyOn(personalIdFormService, 'getPersonalId').mockReturnValue(personalId);
      jest.spyOn(personalIdService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personalId });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personalId }));
      saveSubject.complete();

      // THEN
      expect(personalIdFormService.getPersonalId).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(personalIdService.update).toHaveBeenCalledWith(expect.objectContaining(personalId));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersonalId>>();
      const personalId = { id: 123 };
      jest.spyOn(personalIdFormService, 'getPersonalId').mockReturnValue({ id: null });
      jest.spyOn(personalIdService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personalId: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: personalId }));
      saveSubject.complete();

      // THEN
      expect(personalIdFormService.getPersonalId).toHaveBeenCalled();
      expect(personalIdService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPersonalId>>();
      const personalId = { id: 123 };
      jest.spyOn(personalIdService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ personalId });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(personalIdService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
