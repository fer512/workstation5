import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IWorkerProfile } from '../worker-profile.model';
import { WorkerProfileService } from '../service/worker-profile.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './worker-profile-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WorkerProfileDeleteDialogComponent {
  workerProfile?: IWorkerProfile;

  constructor(protected workerProfileService: WorkerProfileService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workerProfileService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
