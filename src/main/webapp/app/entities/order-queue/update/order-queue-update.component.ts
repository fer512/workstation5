import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { OrderQueueFormService, OrderQueueFormGroup } from './order-queue-form.service';
import { IOrderQueue } from '../order-queue.model';
import { OrderQueueService } from '../service/order-queue.service';
import { IQueue } from 'app/entities/queue/queue.model';
import { QueueService } from 'app/entities/queue/service/queue.service';
import { IWorkerProfile } from 'app/entities/worker-profile/worker-profile.model';
import { WorkerProfileService } from 'app/entities/worker-profile/service/worker-profile.service';

@Component({
  standalone: true,
  selector: 'jhi-order-queue-update',
  templateUrl: './order-queue-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrderQueueUpdateComponent implements OnInit {
  isSaving = false;
  orderQueue: IOrderQueue | null = null;

  queuesSharedCollection: IQueue[] = [];
  workerProfilesSharedCollection: IWorkerProfile[] = [];

  editForm: OrderQueueFormGroup = this.orderQueueFormService.createOrderQueueFormGroup();

  constructor(
    protected orderQueueService: OrderQueueService,
    protected orderQueueFormService: OrderQueueFormService,
    protected queueService: QueueService,
    protected workerProfileService: WorkerProfileService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareQueue = (o1: IQueue | null, o2: IQueue | null): boolean => this.queueService.compareQueue(o1, o2);

  compareWorkerProfile = (o1: IWorkerProfile | null, o2: IWorkerProfile | null): boolean =>
    this.workerProfileService.compareWorkerProfile(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderQueue }) => {
      this.orderQueue = orderQueue;
      if (orderQueue) {
        this.updateForm(orderQueue);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orderQueue = this.orderQueueFormService.getOrderQueue(this.editForm);
    if (orderQueue.id !== null) {
      this.subscribeToSaveResponse(this.orderQueueService.update(orderQueue));
    } else {
      this.subscribeToSaveResponse(this.orderQueueService.create(orderQueue));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrderQueue>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(orderQueue: IOrderQueue): void {
    this.orderQueue = orderQueue;
    this.orderQueueFormService.resetForm(this.editForm, orderQueue);

    this.queuesSharedCollection = this.queueService.addQueueToCollectionIfMissing<IQueue>(this.queuesSharedCollection, orderQueue.queue);
    this.workerProfilesSharedCollection = this.workerProfileService.addWorkerProfileToCollectionIfMissing<IWorkerProfile>(
      this.workerProfilesSharedCollection,
      orderQueue.workerProfile
    );
  }

  protected loadRelationshipsOptions(): void {
    this.queueService
      .query()
      .pipe(map((res: HttpResponse<IQueue[]>) => res.body ?? []))
      .pipe(map((queues: IQueue[]) => this.queueService.addQueueToCollectionIfMissing<IQueue>(queues, this.orderQueue?.queue)))
      .subscribe((queues: IQueue[]) => (this.queuesSharedCollection = queues));

    this.workerProfileService
      .query()
      .pipe(map((res: HttpResponse<IWorkerProfile[]>) => res.body ?? []))
      .pipe(
        map((workerProfiles: IWorkerProfile[]) =>
          this.workerProfileService.addWorkerProfileToCollectionIfMissing<IWorkerProfile>(workerProfiles, this.orderQueue?.workerProfile)
        )
      )
      .subscribe((workerProfiles: IWorkerProfile[]) => (this.workerProfilesSharedCollection = workerProfiles));
  }
}
