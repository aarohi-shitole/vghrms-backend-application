import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonalDetails } from '../personal-details.model';

@Component({
  selector: 'jhi-personal-details-detail',
  templateUrl: './personal-details-detail.component.html',
})
export class PersonalDetailsDetailComponent implements OnInit {
  personalDetails: IPersonalDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personalDetails }) => {
      this.personalDetails = personalDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
