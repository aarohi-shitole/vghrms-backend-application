import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PersonalIdComponent } from './list/personal-id.component';
import { PersonalIdDetailComponent } from './detail/personal-id-detail.component';
import { PersonalIdUpdateComponent } from './update/personal-id-update.component';
import { PersonalIdDeleteDialogComponent } from './delete/personal-id-delete-dialog.component';
import { PersonalIdRoutingModule } from './route/personal-id-routing.module';

@NgModule({
  imports: [SharedModule, PersonalIdRoutingModule],
  declarations: [PersonalIdComponent, PersonalIdDetailComponent, PersonalIdUpdateComponent, PersonalIdDeleteDialogComponent],
})
export class PersonalIdModule {}
