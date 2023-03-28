import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ReportingComponent } from './list/reporting.component';
import { ReportingDetailComponent } from './detail/reporting-detail.component';
import { ReportingUpdateComponent } from './update/reporting-update.component';
import { ReportingDeleteDialogComponent } from './delete/reporting-delete-dialog.component';
import { ReportingRoutingModule } from './route/reporting-routing.module';

@NgModule({
  imports: [SharedModule, ReportingRoutingModule],
  declarations: [ReportingComponent, ReportingDetailComponent, ReportingUpdateComponent, ReportingDeleteDialogComponent],
})
export class ReportingModule {}
