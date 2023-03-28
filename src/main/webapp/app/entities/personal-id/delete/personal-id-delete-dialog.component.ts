import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPersonalId } from '../personal-id.model';
import { PersonalIdService } from '../service/personal-id.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './personal-id-delete-dialog.component.html',
})
export class PersonalIdDeleteDialogComponent {
  personalId?: IPersonalId;

  constructor(protected personalIdService: PersonalIdService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.personalIdService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
