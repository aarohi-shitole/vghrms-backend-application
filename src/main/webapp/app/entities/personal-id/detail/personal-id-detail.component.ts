import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPersonalId } from '../personal-id.model';

@Component({
  selector: 'jhi-personal-id-detail',
  templateUrl: './personal-id-detail.component.html',
})
export class PersonalIdDetailComponent implements OnInit {
  personalId: IPersonalId | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ personalId }) => {
      this.personalId = personalId;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
