import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IWaitingRoom } from '../waiting-room.model';
import { WaitingRoomService } from '../service/waiting-room.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './waiting-room-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class WaitingRoomDeleteDialogComponent {
  waitingRoom?: IWaitingRoom;

  constructor(protected waitingRoomService: WaitingRoomService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.waitingRoomService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
