import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EmployeeFormService } from './employee-form.service';
import { EmployeeService } from '../service/employee.service';
import { IEmployee } from '../employee.model';
import { IDesignation } from 'app/entities/designation/designation.model';
import { DesignationService } from 'app/entities/designation/service/designation.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IBranch } from 'app/entities/branch/branch.model';
import { BranchService } from 'app/entities/branch/service/branch.service';
import { IRegion } from 'app/entities/region/region.model';
import { RegionService } from 'app/entities/region/service/region.service';

import { EmployeeUpdateComponent } from './employee-update.component';

describe('Employee Management Update Component', () => {
  let comp: EmployeeUpdateComponent;
  let fixture: ComponentFixture<EmployeeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let employeeFormService: EmployeeFormService;
  let employeeService: EmployeeService;
  let designationService: DesignationService;
  let departmentService: DepartmentService;
  let branchService: BranchService;
  let regionService: RegionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EmployeeUpdateComponent],
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
      .overrideTemplate(EmployeeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EmployeeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    employeeFormService = TestBed.inject(EmployeeFormService);
    employeeService = TestBed.inject(EmployeeService);
    designationService = TestBed.inject(DesignationService);
    departmentService = TestBed.inject(DepartmentService);
    branchService = TestBed.inject(BranchService);
    regionService = TestBed.inject(RegionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Designation query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const designation: IDesignation = { id: 96982 };
      employee.designation = designation;

      const designationCollection: IDesignation[] = [{ id: 66817 }];
      jest.spyOn(designationService, 'query').mockReturnValue(of(new HttpResponse({ body: designationCollection })));
      const additionalDesignations = [designation];
      const expectedCollection: IDesignation[] = [...additionalDesignations, ...designationCollection];
      jest.spyOn(designationService, 'addDesignationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(designationService.query).toHaveBeenCalled();
      expect(designationService.addDesignationToCollectionIfMissing).toHaveBeenCalledWith(
        designationCollection,
        ...additionalDesignations.map(expect.objectContaining)
      );
      expect(comp.designationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Department query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const department: IDepartment = { id: 35363 };
      employee.department = department;

      const departmentCollection: IDepartment[] = [{ id: 78278 }];
      jest.spyOn(departmentService, 'query').mockReturnValue(of(new HttpResponse({ body: departmentCollection })));
      const additionalDepartments = [department];
      const expectedCollection: IDepartment[] = [...additionalDepartments, ...departmentCollection];
      jest.spyOn(departmentService, 'addDepartmentToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(departmentService.query).toHaveBeenCalled();
      expect(departmentService.addDepartmentToCollectionIfMissing).toHaveBeenCalledWith(
        departmentCollection,
        ...additionalDepartments.map(expect.objectContaining)
      );
      expect(comp.departmentsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Branch query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const branch: IBranch = { id: 71655 };
      employee.branch = branch;

      const branchCollection: IBranch[] = [{ id: 89930 }];
      jest.spyOn(branchService, 'query').mockReturnValue(of(new HttpResponse({ body: branchCollection })));
      const additionalBranches = [branch];
      const expectedCollection: IBranch[] = [...additionalBranches, ...branchCollection];
      jest.spyOn(branchService, 'addBranchToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(branchService.query).toHaveBeenCalled();
      expect(branchService.addBranchToCollectionIfMissing).toHaveBeenCalledWith(
        branchCollection,
        ...additionalBranches.map(expect.objectContaining)
      );
      expect(comp.branchesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Region query and add missing value', () => {
      const employee: IEmployee = { id: 456 };
      const region: IRegion = { id: 92117 };
      employee.region = region;

      const regionCollection: IRegion[] = [{ id: 60312 }];
      jest.spyOn(regionService, 'query').mockReturnValue(of(new HttpResponse({ body: regionCollection })));
      const additionalRegions = [region];
      const expectedCollection: IRegion[] = [...additionalRegions, ...regionCollection];
      jest.spyOn(regionService, 'addRegionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(regionService.query).toHaveBeenCalled();
      expect(regionService.addRegionToCollectionIfMissing).toHaveBeenCalledWith(
        regionCollection,
        ...additionalRegions.map(expect.objectContaining)
      );
      expect(comp.regionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const employee: IEmployee = { id: 456 };
      const designation: IDesignation = { id: 92741 };
      employee.designation = designation;
      const department: IDepartment = { id: 60127 };
      employee.department = department;
      const branch: IBranch = { id: 99499 };
      employee.branch = branch;
      const region: IRegion = { id: 51111 };
      employee.region = region;

      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      expect(comp.designationsSharedCollection).toContain(designation);
      expect(comp.departmentsSharedCollection).toContain(department);
      expect(comp.branchesSharedCollection).toContain(branch);
      expect(comp.regionsSharedCollection).toContain(region);
      expect(comp.employee).toEqual(employee);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeFormService, 'getEmployee').mockReturnValue(employee);
      jest.spyOn(employeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employee }));
      saveSubject.complete();

      // THEN
      expect(employeeFormService.getEmployee).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(employeeService.update).toHaveBeenCalledWith(expect.objectContaining(employee));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeFormService, 'getEmployee').mockReturnValue({ id: null });
      jest.spyOn(employeeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: employee }));
      saveSubject.complete();

      // THEN
      expect(employeeFormService.getEmployee).toHaveBeenCalled();
      expect(employeeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEmployee>>();
      const employee = { id: 123 };
      jest.spyOn(employeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ employee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(employeeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareDesignation', () => {
      it('Should forward to designationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(designationService, 'compareDesignation');
        comp.compareDesignation(entity, entity2);
        expect(designationService.compareDesignation).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareDepartment', () => {
      it('Should forward to departmentService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(departmentService, 'compareDepartment');
        comp.compareDepartment(entity, entity2);
        expect(departmentService.compareDepartment).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBranch', () => {
      it('Should forward to branchService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(branchService, 'compareBranch');
        comp.compareBranch(entity, entity2);
        expect(branchService.compareBranch).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareRegion', () => {
      it('Should forward to regionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(regionService, 'compareRegion');
        comp.compareRegion(entity, entity2);
        expect(regionService.compareRegion).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
