import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReportingComponent } from '../list/reporting.component';
import { ReportingDetailComponent } from '../detail/reporting-detail.component';
import { ReportingUpdateComponent } from '../update/reporting-update.component';
import { ReportingRoutingResolveService } from './reporting-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const reportingRoute: Routes = [
  {
    path: '',
    component: ReportingComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReportingDetailComponent,
    resolve: {
      reporting: ReportingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReportingUpdateComponent,
    resolve: {
      reporting: ReportingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReportingUpdateComponent,
    resolve: {
      reporting: ReportingRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(reportingRoute)],
  exports: [RouterModule],
})
export class ReportingRoutingModule {}
