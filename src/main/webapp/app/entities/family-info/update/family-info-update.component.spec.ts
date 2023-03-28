import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { FamilyInfoFormService } from './family-info-form.service';
import { FamilyInfoService } from '../service/family-info.service';
import { IFamilyInfo } from '../family-info.model';

import { FamilyInfoUpdateComponent } from './family-info-update.component';

describe('FamilyInfo Management Update Component', () => {
  let comp: FamilyInfoUpdateComponent;
  let fixture: ComponentFixture<FamilyInfoUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let familyInfoFormService: FamilyInfoFormService;
  let familyInfoService: FamilyInfoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [FamilyInfoUpdateComponent],
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
      .overrideTemplate(FamilyInfoUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(FamilyInfoUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    familyInfoFormService = TestBed.inject(FamilyInfoFormService);
    familyInfoService = TestBed.inject(FamilyInfoService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const familyInfo: IFamilyInfo = { id: 456 };

      activatedRoute.data = of({ familyInfo });
      comp.ngOnInit();

      expect(comp.familyInfo).toEqual(familyInfo);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFamilyInfo>>();
      const familyInfo = { id: 123 };
      jest.spyOn(familyInfoFormService, 'getFamilyInfo').mockReturnValue(familyInfo);
      jest.spyOn(familyInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ familyInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: familyInfo }));
      saveSubject.complete();

      // THEN
      expect(familyInfoFormService.getFamilyInfo).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(familyInfoService.update).toHaveBeenCalledWith(expect.objectContaining(familyInfo));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFamilyInfo>>();
      const familyInfo = { id: 123 };
      jest.spyOn(familyInfoFormService, 'getFamilyInfo').mockReturnValue({ id: null });
      jest.spyOn(familyInfoService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ familyInfo: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: familyInfo }));
      saveSubject.complete();

      // THEN
      expect(familyInfoFormService.getFamilyInfo).toHaveBeenCalled();
      expect(familyInfoService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IFamilyInfo>>();
      const familyInfo = { id: 123 };
      jest.spyOn(familyInfoService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ familyInfo });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(familyInfoService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
