import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ReportingFormService, ReportingFormGroup } from './reporting-form.service';
import { IReporting } from '../reporting.model';
import { ReportingService } from '../service/reporting.service';

@Component({
  selector: 'jhi-reporting-update',
  templateUrl: './reporting-update.component.html',
})
export class ReportingUpdateComponent implements OnInit {
  isSaving = false;
  reporting: IReporting | null = null;

  editForm: ReportingFormGroup = this.reportingFormService.createReportingFormGroup();

  constructor(
    protected reportingService: ReportingService,
    protected reportingFormService: ReportingFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reporting }) => {
      this.reporting = reporting;
      if (reporting) {
        this.updateForm(reporting);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reporting = this.reportingFormService.getReporting(this.editForm);
    if (reporting.id !== null) {
      this.subscribeToSaveResponse(this.reportingService.update(reporting));
    } else {
      this.subscribeToSaveResponse(this.reportingService.create(reporting));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReporting>>): void {
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

  protected updateForm(reporting: IReporting): void {
    this.reporting = reporting;
    this.reportingFormService.resetForm(this.editForm, reporting);
  }
}
