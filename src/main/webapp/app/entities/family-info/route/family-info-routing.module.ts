import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { FamilyInfoComponent } from '../list/family-info.component';
import { FamilyInfoDetailComponent } from '../detail/family-info-detail.component';
import { FamilyInfoUpdateComponent } from '../update/family-info-update.component';
import { FamilyInfoRoutingResolveService } from './family-info-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const familyInfoRoute: Routes = [
  {
    path: '',
    component: FamilyInfoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FamilyInfoDetailComponent,
    resolve: {
      familyInfo: FamilyInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FamilyInfoUpdateComponent,
    resolve: {
      familyInfo: FamilyInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FamilyInfoUpdateComponent,
    resolve: {
      familyInfo: FamilyInfoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(familyInfoRoute)],
  exports: [RouterModule],
})
export class FamilyInfoRoutingModule {}
