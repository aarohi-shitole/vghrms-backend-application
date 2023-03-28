import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFamilyInfo } from '../family-info.model';

@Component({
  selector: 'jhi-family-info-detail',
  templateUrl: './family-info-detail.component.html',
})
export class FamilyInfoDetailComponent implements OnInit {
  familyInfo: IFamilyInfo | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ familyInfo }) => {
      this.familyInfo = familyInfo;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
