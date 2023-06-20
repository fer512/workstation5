import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { IOrderQueue } from '../order-queue.model';
import { OrderQueueService } from '../service/order-queue.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  standalone: true,
  templateUrl: './order-queue-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class OrderQueueDeleteDialogComponent {
  orderQueue?: IOrderQueue;

  constructor(protected orderQueueService: OrderQueueService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orderQueueService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
