import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { WaitingRoomFormService, WaitingRoomFormGroup } from './waiting-room-form.service';
import { IWaitingRoom } from '../waiting-room.model';
import { WaitingRoomService } from '../service/waiting-room.service';
import { IWaitingRoomAttencionChannel } from 'app/entities/waiting-room-attencion-channel/waiting-room-attencion-channel.model';
import { WaitingRoomAttencionChannelService } from 'app/entities/waiting-room-attencion-channel/service/waiting-room-attencion-channel.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { IBranch } from 'app/entities/branch/branch.model';
import { BranchService } from 'app/entities/branch/service/branch.service';
import { WaitingRoomStatus } from 'app/entities/enumerations/waiting-room-status.model';

@Component({
  standalone: true,
  selector: 'jhi-waiting-room-update',
  templateUrl: './waiting-room-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WaitingRoomUpdateComponent implements OnInit {
  isSaving = false;
  waitingRoom: IWaitingRoom | null = null;
  waitingRoomStatusValues = Object.keys(WaitingRoomStatus);

  attencionChannelsCollection: IWaitingRoomAttencionChannel[] = [];
  companiesSharedCollection: ICompany[] = [];
  branchesSharedCollection: IBranch[] = [];

  editForm: WaitingRoomFormGroup = this.waitingRoomFormService.createWaitingRoomFormGroup();

  constructor(
    protected waitingRoomService: WaitingRoomService,
    protected waitingRoomFormService: WaitingRoomFormService,
    protected waitingRoomAttencionChannelService: WaitingRoomAttencionChannelService,
    protected companyService: CompanyService,
    protected branchService: BranchService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareWaitingRoomAttencionChannel = (o1: IWaitingRoomAttencionChannel | null, o2: IWaitingRoomAttencionChannel | null): boolean =>
    this.waitingRoomAttencionChannelService.compareWaitingRoomAttencionChannel(o1, o2);

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  compareBranch = (o1: IBranch | null, o2: IBranch | null): boolean => this.branchService.compareBranch(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ waitingRoom }) => {
      this.waitingRoom = waitingRoom;
      if (waitingRoom) {
        this.updateForm(waitingRoom);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const waitingRoom = this.waitingRoomFormService.getWaitingRoom(this.editForm);
    if (waitingRoom.id !== null) {
      this.subscribeToSaveResponse(this.waitingRoomService.update(waitingRoom));
    } else {
      this.subscribeToSaveResponse(this.waitingRoomService.create(waitingRoom));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWaitingRoom>>): void {
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

  protected updateForm(waitingRoom: IWaitingRoom): void {
    this.waitingRoom = waitingRoom;
    this.waitingRoomFormService.resetForm(this.editForm, waitingRoom);

    this.attencionChannelsCollection =
      this.waitingRoomAttencionChannelService.addWaitingRoomAttencionChannelToCollectionIfMissing<IWaitingRoomAttencionChannel>(
        this.attencionChannelsCollection,
        waitingRoom.attencionChannel
      );
    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      waitingRoom.company
    );
    this.branchesSharedCollection = this.branchService.addBranchToCollectionIfMissing<IBranch>(
      this.branchesSharedCollection,
      ...(waitingRoom.branches ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.waitingRoomAttencionChannelService
      .query({ filter: 'waitingroom-is-null' })
      .pipe(map((res: HttpResponse<IWaitingRoomAttencionChannel[]>) => res.body ?? []))
      .pipe(
        map((waitingRoomAttencionChannels: IWaitingRoomAttencionChannel[]) =>
          this.waitingRoomAttencionChannelService.addWaitingRoomAttencionChannelToCollectionIfMissing<IWaitingRoomAttencionChannel>(
            waitingRoomAttencionChannels,
            this.waitingRoom?.attencionChannel
          )
        )
      )
      .subscribe(
        (waitingRoomAttencionChannels: IWaitingRoomAttencionChannel[]) => (this.attencionChannelsCollection = waitingRoomAttencionChannels)
      );

    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.waitingRoom?.company))
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));

    this.branchService
      .query()
      .pipe(map((res: HttpResponse<IBranch[]>) => res.body ?? []))
      .pipe(
        map((branches: IBranch[]) =>
          this.branchService.addBranchToCollectionIfMissing<IBranch>(branches, ...(this.waitingRoom?.branches ?? []))
        )
      )
      .subscribe((branches: IBranch[]) => (this.branchesSharedCollection = branches));
  }
}
