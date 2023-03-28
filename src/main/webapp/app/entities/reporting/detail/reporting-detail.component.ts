import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReporting } from '../reporting.model';

@Component({
  selector: 'jhi-reporting-detail',
  templateUrl: './reporting-detail.component.html',
})
export class ReportingDetailComponent implements OnInit {
  reporting: IReporting | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reporting }) => {
      this.reporting = reporting;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
