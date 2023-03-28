import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PersonalDetailsFormService, PersonalDetailsFormGroup } from './personal-details-form.service';
import { IPersonalDetails } from '../personal-details.model';
import { PersonalDetailsService } from '../service/personal-details.service';

@Component({
  selector: 'jhi-personal-details-update',
  templateUrl: './personal-details-update.component.html',
})
export class PersonalDetailsUpdateComponent implements OnInit {
  isSaving = false;
  personalDetails: IPersonalDetails | null = null;

  editForm: PersonalDetailsFormGroup = this.personalDetailsFormService.createPersonalDetailsFormGroup();

  constructor(
    protected personalDetailsService: PersonalDetailsService,
    protected personalDetailsFormService: PersonalDetailsFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personalDetails }) => {
      this.personalDetails = personalDetails;
      if (personalDetails) {
        this.updateForm(personalDetails);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personalDetails = this.personalDetailsFormService.getPersonalDetails(this.editForm);
    if (personalDetails.id !== null) {
      this.subscribeToSaveResponse(this.personalDetailsService.update(personalDetails));
    } else {
      this.subscribeToSaveResponse(this.personalDetailsService.create(personalDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonalDetails>>): void {
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

  protected updateForm(personalDetails: IPersonalDetails): void {
    this.personalDetails = personalDetails;
    this.personalDetailsFormService.resetForm(this.editForm, personalDetails);
  }
}
