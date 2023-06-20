import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { WorkerProfileFormService, WorkerProfileFormGroup } from './worker-profile-form.service';
import { IWorkerProfile } from '../worker-profile.model';
import { WorkerProfileService } from '../service/worker-profile.service';
import { IWorkerProfileAttencionChannel } from 'app/entities/worker-profile-attencion-channel/worker-profile-attencion-channel.model';
import { WorkerProfileAttencionChannelService } from 'app/entities/worker-profile-attencion-channel/service/worker-profile-attencion-channel.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { IBranch } from 'app/entities/branch/branch.model';
import { BranchService } from 'app/entities/branch/service/branch.service';
import { WorkerProfileStatus } from 'app/entities/enumerations/worker-profile-status.model';

@Component({
  standalone: true,
  selector: 'jhi-worker-profile-update',
  templateUrl: './worker-profile-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WorkerProfileUpdateComponent implements OnInit {
  isSaving = false;
  workerProfile: IWorkerProfile | null = null;
  workerProfileStatusValues = Object.keys(WorkerProfileStatus);

  attencionChannelsCollection: IWorkerProfileAttencionChannel[] = [];
  companiesSharedCollection: ICompany[] = [];
  branchesSharedCollection: IBranch[] = [];

  editForm: WorkerProfileFormGroup = this.workerProfileFormService.createWorkerProfileFormGroup();

  constructor(
    protected workerProfileService: WorkerProfileService,
    protected workerProfileFormService: WorkerProfileFormService,
    protected workerProfileAttencionChannelService: WorkerProfileAttencionChannelService,
    protected companyService: CompanyService,
    protected branchService: BranchService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareWorkerProfileAttencionChannel = (o1: IWorkerProfileAttencionChannel | null, o2: IWorkerProfileAttencionChannel | null): boolean =>
    this.workerProfileAttencionChannelService.compareWorkerProfileAttencionChannel(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  compareBranch = (o1: IBranch | null, o2: IBranch | null): boolean => this.branchService.compareBranch(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workerProfile }) => {
      this.workerProfile = workerProfile;
      if (workerProfile) {
        this.updateForm(workerProfile);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const workerProfile = this.workerProfileFormService.getWorkerProfile(this.editForm);
    if (workerProfile.id !== null) {
      this.subscribeToSaveResponse(this.workerProfileService.update(workerProfile));
    } else {
      this.subscribeToSaveResponse(this.workerProfileService.create(workerProfile));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkerProfile>>): void {
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

  protected updateForm(workerProfile: IWorkerProfile): void {
    this.workerProfile = workerProfile;
    this.workerProfileFormService.resetForm(this.editForm, workerProfile);

    this.attencionChannelsCollection =
      this.workerProfileAttencionChannelService.addWorkerProfileAttencionChannelToCollectionIfMissing<IWorkerProfileAttencionChannel>(
        this.attencionChannelsCollection,
        workerProfile.attencionChannel
      );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      workerProfile.company
    );
    this.branchesSharedCollection = this.branchService.addBranchToCollectionIfMissing<IBranch>(
      this.branchesSharedCollection,
      ...(workerProfile.branches ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.workerProfileAttencionChannelService
      .query({ filter: 'workerprofile-is-null' })
      .pipe(map((res: HttpResponse<IWorkerProfileAttencionChannel[]>) => res.body ?? []))
      .pipe(
        map((workerProfileAttencionChannels: IWorkerProfileAttencionChannel[]) =>
          this.workerProfileAttencionChannelService.addWorkerProfileAttencionChannelToCollectionIfMissing<IWorkerProfileAttencionChannel>(
            workerProfileAttencionChannels,
            this.workerProfile?.attencionChannel
          )
        )
      )
      .subscribe(
        (workerProfileAttencionChannels: IWorkerProfileAttencionChannel[]) =>
          (this.attencionChannelsCollection = workerProfileAttencionChannels)
      );

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) =>
          this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.workerProfile?.company)
        )
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));

    this.branchService
      .query()
      .pipe(map((res: HttpResponse<IBranch[]>) => res.body ?? []))
      .pipe(
        map((branches: IBranch[]) =>
          this.branchService.addBranchToCollectionIfMissing<IBranch>(branches, ...(this.workerProfile?.branches ?? []))
        )
      )
      .subscribe((branches: IBranch[]) => (this.branchesSharedCollection = branches));
  }
}
