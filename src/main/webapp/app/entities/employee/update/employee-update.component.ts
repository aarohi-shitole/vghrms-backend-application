import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { EmployeeFormService, EmployeeFormGroup } from './employee-form.service';
import { IEmployee } from '../employee.model';
import { EmployeeService } from '../service/employee.service';
import { IDesignation } from 'app/entities/designation/designation.model';
import { DesignationService } from 'app/entities/designation/service/designation.service';
import { IDepartment } from 'app/entities/department/department.model';
import { DepartmentService } from 'app/entities/department/service/department.service';
import { IBranch } from 'app/entities/branch/branch.model';
import { BranchService } from 'app/entities/branch/service/branch.service';
import { IRegion } from 'app/entities/region/region.model';
import { RegionService } from 'app/entities/region/service/region.service';

@Component({
  selector: 'jhi-employee-update',
  templateUrl: './employee-update.component.html',
})
export class EmployeeUpdateComponent implements OnInit {
  isSaving = false;
  employee: IEmployee | null = null;

  designationsSharedCollection: IDesignation[] = [];
  departmentsSharedCollection: IDepartment[] = [];
  branchesSharedCollection: IBranch[] = [];
  regionsSharedCollection: IRegion[] = [];

  editForm: EmployeeFormGroup = this.employeeFormService.createEmployeeFormGroup();

  constructor(
    protected employeeService: EmployeeService,
    protected employeeFormService: EmployeeFormService,
    protected designationService: DesignationService,
    protected departmentService: DepartmentService,
    protected branchService: BranchService,
    protected regionService: RegionService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDesignation = (o1: IDesignation | null, o2: IDesignation | null): boolean => this.designationService.compareDesignation(o1, o2);

  compareDepartment = (o1: IDepartment | null, o2: IDepartment | null): boolean => this.departmentService.compareDepartment(o1, o2);

  compareBranch = (o1: IBranch | null, o2: IBranch | null): boolean => this.branchService.compareBranch(o1, o2);

  compareRegion = (o1: IRegion | null, o2: IRegion | null): boolean => this.regionService.compareRegion(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employee }) => {
      this.employee = employee;
      if (employee) {
        this.updateForm(employee);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const employee = this.employeeFormService.getEmployee(this.editForm);
    if (employee.id !== null) {
      this.subscribeToSaveResponse(this.employeeService.update(employee));
    } else {
      this.subscribeToSaveResponse(this.employeeService.create(employee));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployee>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(employee: IEmployee): void {
    this.employee = employee;
    this.employeeFormService.resetForm(this.editForm, employee);

    this.designationsSharedCollection = this.designationService.addDesignationToCollectionIfMissing<IDesignation>(
      this.designationsSharedCollection,
      employee.designation
    );
    this.departmentsSharedCollection = this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(
      this.departmentsSharedCollection,
      employee.department
    );
    this.branchesSharedCollection = this.branchService.addBranchToCollectionIfMissing<IBranch>(
      this.branchesSharedCollection,
      employee.branch
    );
    this.regionsSharedCollection = this.regionService.addRegionToCollectionIfMissing<IRegion>(
      this.regionsSharedCollection,
      employee.region
    );
  }

  protected loadRelationshipsOptions(): void {
    this.designationService
      .query()
      .pipe(map((res: HttpResponse<IDesignation[]>) => res.body ?? []))
      .pipe(
        map((designations: IDesignation[]) =>
          this.designationService.addDesignationToCollectionIfMissing<IDesignation>(designations, this.employee?.designation)
        )
      )
      .subscribe((designations: IDesignation[]) => (this.designationsSharedCollection = designations));

    this.departmentService
      .query()
      .pipe(map((res: HttpResponse<IDepartment[]>) => res.body ?? []))
      .pipe(
        map((departments: IDepartment[]) =>
          this.departmentService.addDepartmentToCollectionIfMissing<IDepartment>(departments, this.employee?.department)
        )
      )
      .subscribe((departments: IDepartment[]) => (this.departmentsSharedCollection = departments));

    this.branchService
      .query()
      .pipe(map((res: HttpResponse<IBranch[]>) => res.body ?? []))
      .pipe(map((branches: IBranch[]) => this.branchService.addBranchToCollectionIfMissing<IBranch>(branches, this.employee?.branch)))
      .subscribe((branches: IBranch[]) => (this.branchesSharedCollection = branches));

    this.regionService
      .query()
      .pipe(map((res: HttpResponse<IRegion[]>) => res.body ?? []))
      .pipe(map((regions: IRegion[]) => this.regionService.addRegionToCollectionIfMissing<IRegion>(regions, this.employee?.region)))
      .subscribe((regions: IRegion[]) => (this.regionsSharedCollection = regions));
  }
}
