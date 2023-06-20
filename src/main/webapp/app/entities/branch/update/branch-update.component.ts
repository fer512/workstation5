import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { BranchFormService, BranchFormGroup } from './branch-form.service';
import { IBranch } from '../branch.model';
import { BranchService } from '../service/branch.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { BranchStatus } from 'app/entities/enumerations/branch-status.model';
import { Language } from 'app/entities/enumerations/language.model';

@Component({
  standalone: true,
  selector: 'jhi-branch-update',
  templateUrl: './branch-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class BranchUpdateComponent implements OnInit {
  isSaving = false;
  branch: IBranch | null = null;
  branchStatusValues = Object.keys(BranchStatus);
  languageValues = Object.keys(Language);

  companiesSharedCollection: ICompany[] = [];

  editForm: BranchFormGroup = this.branchFormService.createBranchFormGroup();

  constructor(
    protected branchService: BranchService,
    protected branchFormService: BranchFormService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareCompany = (o1: ICompany | null, o2: ICompany | null): boolean => this.companyService.compareCompany(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ branch }) => {
      this.branch = branch;
      if (branch) {
        this.updateForm(branch);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const branch = this.branchFormService.getBranch(this.editForm);
    if (branch.id !== null) {
      this.subscribeToSaveResponse(this.branchService.update(branch));
    } else {
      this.subscribeToSaveResponse(this.branchService.create(branch));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBranch>>): void {
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

  protected updateForm(branch: IBranch): void {
    this.branch = branch;
    this.branchFormService.resetForm(this.editForm, branch);

    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing<ICompany>(
      this.companiesSharedCollection,
      branch.company
    );
  }

  protected loadRelationshipsOptions(): void {
    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing<ICompany>(companies, this.branch?.company)))
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }
}
