import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { PersonalIdFormService, PersonalIdFormGroup } from './personal-id-form.service';
import { IPersonalId } from '../personal-id.model';
import { PersonalIdService } from '../service/personal-id.service';

@Component({
  selector: 'jhi-personal-id-update',
  templateUrl: './personal-id-update.component.html',
})
export class PersonalIdUpdateComponent implements OnInit {
  isSaving = false;
  personalId: IPersonalId | null = null;

  editForm: PersonalIdFormGroup = this.personalIdFormService.createPersonalIdFormGroup();

  constructor(
    protected personalIdService: PersonalIdService,
    protected personalIdFormService: PersonalIdFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personalId }) => {
      this.personalId = personalId;
      if (personalId) {
        this.updateForm(personalId);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const personalId = this.personalIdFormService.getPersonalId(this.editForm);
    if (personalId.id !== null) {
      this.subscribeToSaveResponse(this.personalIdService.update(personalId));
    } else {
      this.subscribeToSaveResponse(this.personalIdService.create(personalId));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPersonalId>>): void {
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

  protected updateForm(personalId: IPersonalId): void {
    this.personalId = personalId;
    this.personalIdFormService.resetForm(this.editForm, personalId);
  }
}
