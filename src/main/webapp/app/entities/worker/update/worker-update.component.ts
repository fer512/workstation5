import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { WorkerFormService, WorkerFormGroup } from './worker-form.service';
import { IWorker } from '../worker.model';
import { WorkerService } from '../service/worker.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { IBranch } from 'app/entities/branch/branch.model';
import { BranchService } from 'app/entities/branch/service/branch.service';
import { IWaitingRoom } from 'app/entities/waiting-room/waiting-room.model';
import { WaitingRoomService } from 'app/entities/waiting-room/service/waiting-room.service';
import { WorkerStatus } from 'app/entities/enumerations/worker-status.model';

@Component({
  standalone: true,
  selector: 'jhi-worker-update',
  templateUrl: './worker-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class WorkerUpdateComponent implements OnInit {
  isSaving = false;
  worker: IWorker | null = null;
  workerStatusValues = Object.keys(WorkerStatus);

  companiesSharedCollection: ICompany[] = [];
  branchesSharedCollection: IBranch[] = [];
  waitingRoomsSharedCollection: IWaitingRoom[] = [];

  editForm: WorkerFormGroup = this.workerFormService.createWorkerFormGroup();

  constructor(
    protected workerService: WorkerService,
    protected workerFormService: WorkerFormService,
    protected companyService: CompanyService,
    protected branchService: BranchService,
    protected waitingRoomService: WaitingRoomService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  compareBranch = (o1: IBranch | null, o2: IBranch | null): boolean => this.branchService.compareBranch(o1, o2);

  compareWaitingRoom = (o1: IWaitingRoom | null, o2: IWaitingRoom | null): boolean => this.waitingRoomService.compareWaitingRoom(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ worker }) => {
      this.worker = worker;
      if (worker) {
        this.updateForm(worker);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const worker = this.workerFormService.getWorker(this.editForm);
    if (worker.id !== null) {
      this.subscribeToSaveResponse(this.workerService.update(worker));
    } else {
      this.subscribeToSaveResponse(this.workerService.create(worker));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorker>>): void {
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

  protected updateForm(worker: IWorker): void {
    this.worker = worker;
    this.workerFormService.resetForm(this.editForm, worker);

    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      worker.company
    );
    this.branchesSharedCollection = this.branchService.addBranchToCollectionIfMissing<IBranch>(
      this.branchesSharedCollection,
      ...(worker.branches ?? [])
    );
    this.waitingRoomsSharedCollection = this.waitingRoomService.addWaitingRoomToCollectionIfMissing<IWaitingRoom>(
      this.waitingRoomsSharedCollection,
      worker.waitingRoom
    );
  }

  protected loadRelationshipsOptions(): void {
    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.worker?.company)))
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));

    this.branchService
      .query()
      .pipe(map((res: HttpResponse<IBranch[]>) => res.body ?? []))
      .pipe(
        map((branches: IBranch[]) => this.branchService.addBranchToCollectionIfMissing<IBranch>(branches, ...(this.worker?.branches ?? [])))
      )
      .subscribe((branches: IBranch[]) => (this.branchesSharedCollection = branches));

    this.waitingRoomService
      .query()
      .pipe(map((res: HttpResponse<IWaitingRoom[]>) => res.body ?? []))
      .pipe(
        map((waitingRooms: IWaitingRoom[]) =>
          this.waitingRoomService.addWaitingRoomToCollectionIfMissing<IWaitingRoom>(waitingRooms, this.worker?.waitingRoom)
        )
      )
      .subscribe((waitingRooms: IWaitingRoom[]) => (this.waitingRoomsSharedCollection = waitingRooms));
  }
}
