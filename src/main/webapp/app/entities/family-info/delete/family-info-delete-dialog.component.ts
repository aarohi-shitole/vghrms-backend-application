import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFamilyInfo } from '../family-info.model';
import { FamilyInfoService } from '../service/family-info.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './family-info-delete-dialog.component.html',
})
export class FamilyInfoDeleteDialogComponent {
  familyInfo?: IFamilyInfo;

  constructor(protected familyInfoService: FamilyInfoService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.familyInfoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
