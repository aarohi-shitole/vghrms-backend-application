import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PersonalIdComponent } from '../list/personal-id.component';
import { PersonalIdDetailComponent } from '../detail/personal-id-detail.component';
import { PersonalIdUpdateComponent } from '../update/personal-id-update.component';
import { PersonalIdRoutingResolveService } from './personal-id-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const personalIdRoute: Routes = [
  {
    path: '',
    component: PersonalIdComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PersonalIdDetailComponent,
    resolve: {
      personalId: PersonalIdRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PersonalIdUpdateComponent,
    resolve: {
      personalId: PersonalIdRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PersonalIdUpdateComponent,
    resolve: {
      personalId: PersonalIdRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(personalIdRoute)],
  exports: [RouterModule],
})
export class PersonalIdRoutingModule {}
