import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { QueueFormService, QueueFormGroup } from './queue-form.service';
import { IQueue } from '../queue.model';
import { QueueService } from '../service/queue.service';
import { IAttencionChannel } from 'app/entities/attencion-channel/attencion-channel.model';
import { AttencionChannelService } from 'app/entities/attencion-channel/service/attencion-channel.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { IBranch } from 'app/entities/branch/branch.model';
import { BranchService } from 'app/entities/branch/service/branch.service';
import { QueueStatus } from 'app/entities/enumerations/queue-status.model';

@Component({
  standalone: true,
  selector: 'jhi-queue-update',
  templateUrl: './queue-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class QueueUpdateComponent implements OnInit {
  isSaving = false;
  queue: IQueue | null = null;
  queueStatusValues = Object.keys(QueueStatus);

  attencionChannelsCollection: IAttencionChannel[] = [];
  companiesSharedCollection: ICompany[] = [];
  branchesSharedCollection: IBranch[] = [];

  editForm: QueueFormGroup = this.queueFormService.createQueueFormGroup();

  constructor(
    protected queueService: QueueService,
    protected queueFormService: QueueFormService,
    protected attencionChannelService: AttencionChannelService,
    protected companyService: CompanyService,
    protected branchService: BranchService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAttencionChannel = (o1: IAttencionChannel | null, o2: IAttencionChannel | null): boolean =>
    this.attencionChannelService.compareAttencionChannel(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  compareBranch = (o1: IBranch | null, o2: IBranch | null): boolean => this.branchService.compareBranch(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ queue }) => {
      this.queue = queue;
      if (queue) {
        this.updateForm(queue);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const queue = this.queueFormService.getQueue(this.editForm);
    if (queue.id !== null) {
      this.subscribeToSaveResponse(this.queueService.update(queue));
    } else {
      this.subscribeToSaveResponse(this.queueService.create(queue));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IQueue>>): void {
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

  protected updateForm(queue: IQueue): void {
    this.queue = queue;
    this.queueFormService.resetForm(this.editForm, queue);

    this.attencionChannelsCollection = this.attencionChannelService.addAttencionChannelToCollectionIfMissing<IAttencionChannel>(
      this.attencionChannelsCollection,
      queue.attencionChannel
    );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      queue.company
    );
    this.branchesSharedCollection = this.branchService.addBranchToCollectionIfMissing<IBranch>(
      this.branchesSharedCollection,
      ...(queue.branches ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.attencionChannelService
      .query({ filter: 'queue-is-null' })
      .pipe(map((res: HttpResponse<IAttencionChannel[]>) => res.body ?? []))
      .pipe(
        map((attencionChannels: IAttencionChannel[]) =>
          this.attencionChannelService.addAttencionChannelToCollectionIfMissing<IAttencionChannel>(
            attencionChannels,
            this.queue?.attencionChannel
          )
        )
      )
      .subscribe((attencionChannels: IAttencionChannel[]) => (this.attencionChannelsCollection = attencionChannels));

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.queue?.company)))
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));

    this.branchService
      .query()
      .pipe(map((res: HttpResponse<IBranch[]>) => res.body ?? []))
      .pipe(
        map((branches: IBranch[]) => this.branchService.addBranchToCollectionIfMissing<IBranch>(branches, ...(this.queue?.branches ?? [])))
      )
      .subscribe((branches: IBranch[]) => (this.branchesSharedCollection = branches));
  }
}
