import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { FamilyInfoComponent } from './list/family-info.component';
import { FamilyInfoDetailComponent } from './detail/family-info-detail.component';
import { FamilyInfoUpdateComponent } from './update/family-info-update.component';
import { FamilyInfoDeleteDialogComponent } from './delete/family-info-delete-dialog.component';
import { FamilyInfoRoutingModule } from './route/family-info-routing.module';

@NgModule({
  imports: [SharedModule, FamilyInfoRoutingModule],
  declarations: [FamilyInfoComponent, FamilyInfoDetailComponent, FamilyInfoUpdateComponent, FamilyInfoDeleteDialogComponent],
})
export class FamilyInfoModule {}
