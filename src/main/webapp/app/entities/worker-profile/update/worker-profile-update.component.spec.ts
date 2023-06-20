import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WorkerProfileFormService } from './worker-profile-form.service';
import { WorkerProfileService } from '../service/worker-profile.service';
import { IWorkerProfile } from '../worker-profile.model';
import { IWorkerProfileAttencionChannel } from 'app/entities/worker-profile-attencion-channel/worker-profile-attencion-channel.model';
import { WorkerProfileAttencionChannelService } from 'app/entities/worker-profile-attencion-channel/service/worker-profile-attencion-channel.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { IBranch } from 'app/entities/branch/branch.model';
import { BranchService } from 'app/entities/branch/service/branch.service';

import { WorkerProfileUpdateComponent } from './worker-profile-update.component';

describe('WorkerProfile Management Update Component', () => {
  let comp: WorkerProfileUpdateComponent;
  let fixture: ComponentFixture<WorkerProfileUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let workerProfileFormService: WorkerProfileFormService;
  let workerProfileService: WorkerProfileService;
  let workerProfileAttencionChannelService: WorkerProfileAttencionChannelService;
  let companyService: CompanyService;
  let branchService: BranchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), WorkerProfileUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(WorkerProfileUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorkerProfileUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    workerProfileFormService = TestBed.inject(WorkerProfileFormService);
    workerProfileService = TestBed.inject(WorkerProfileService);
    workerProfileAttencionChannelService = TestBed.inject(WorkerProfileAttencionChannelService);
    companyService = TestBed.inject(CompanyService);
    branchService = TestBed.inject(BranchService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call attencionChannel query and add missing value', () => {
      const workerProfile: IWorkerProfile = { id: 456 };
      const attencionChannel: IWorkerProfileAttencionChannel = { id: 74790 };
      workerProfile.attencionChannel = attencionChannel;

      const attencionChannelCollection: IWorkerProfileAttencionChannel[] = [{ id: 40003 }];
      jest.spyOn(workerProfileAttencionChannelService, 'query').mockReturnValue(of(new HttpResponse({ body: attencionChannelCollection })));
      const expectedCollection: IWorkerProfileAttencionChannel[] = [attencionChannel, ...attencionChannelCollection];
      jest
        .spyOn(workerProfileAttencionChannelService, 'addWorkerProfileAttencionChannelToCollectionIfMissing')
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workerProfile });
      comp.ngOnInit();

      expect(workerProfileAttencionChannelService.query).toHaveBeenCalled();
      expect(workerProfileAttencionChannelService.addWorkerProfileAttencionChannelToCollectionIfMissing).toHaveBeenCalledWith(
        attencionChannelCollection,
        attencionChannel
      );
      expect(comp.attencionChannelsCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const workerProfile: IWorkerProfile = { id: 456 };
      const company: ICompany = { id: 66816 };
      workerProfile.company = company;

      const companyCollection: ICompany[] = [{ id: 96089 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workerProfile });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining)
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Branch query and add missing value', () => {
      const workerProfile: IWorkerProfile = { id: 456 };
      const branches: IBranch[] = [{ id: 3296 }];
      workerProfile.branches = branches;

      const branchCollection: IBranch[] = [{ id: 55250 }];
      jest.spyOn(branchService, 'query').mockReturnValue(of(new HttpResponse({ body: branchCollection })));
      const additionalBranches = [...branches];
      const expectedCollection: IBranch[] = [...additionalBranches, ...branchCollection];
      jest.spyOn(branchService, 'addBranchToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ workerProfile });
      comp.ngOnInit();

      expect(branchService.query).toHaveBeenCalled();
      expect(branchService.addBranchToCollectionIfMissing).toHaveBeenCalledWith(
        branchCollection,
        ...additionalBranches.map(expect.objectContaining)
      );
      expect(comp.branchesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const workerProfile: IWorkerProfile = { id: 456 };
      const attencionChannel: IWorkerProfileAttencionChannel = { id: 96022 };
      workerProfile.attencionChannel = attencionChannel;
      const company: ICompany = { id: 51825 };
      workerProfile.company = company;
      const branches: IBranch = { id: 46061 };
      workerProfile.branches = [branches];

      activatedRoute.data = of({ workerProfile });
      comp.ngOnInit();

      expect(comp.attencionChannelsCollection).toContain(attencionChannel);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.branchesSharedCollection).toContain(branches);
      expect(comp.workerProfile).toEqual(workerProfile);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkerProfile>>();
      const workerProfile = { id: 123 };
      jest.spyOn(workerProfileFormService, 'getWorkerProfile').mockReturnValue(workerProfile);
      jest.spyOn(workerProfileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workerProfile });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workerProfile }));
      saveSubject.complete();

      // THEN
      expect(workerProfileFormService.getWorkerProfile).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(workerProfileService.update).toHaveBeenCalledWith(expect.objectContaining(workerProfile));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkerProfile>>();
      const workerProfile = { id: 123 };
      jest.spyOn(workerProfileFormService, 'getWorkerProfile').mockReturnValue({ id: null });
      jest.spyOn(workerProfileService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workerProfile: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workerProfile }));
      saveSubject.complete();

      // THEN
      expect(workerProfileFormService.getWorkerProfile).toHaveBeenCalled();
      expect(workerProfileService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkerProfile>>();
      const workerProfile = { id: 123 };
      jest.spyOn(workerProfileService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workerProfile });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(workerProfileService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareWorkerProfileAttencionChannel', () => {
      it('Should forward to workerProfileAttencionChannelService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(workerProfileAttencionChannelService, 'compareWorkerProfileAttencionChannel');
        comp.compareWorkerProfileAttencionChannel(entity, entity2);
        expect(workerProfileAttencionChannelService.compareWorkerProfileAttencionChannel).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCompany', () => {
      it('Should forward to companyService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(companyService, 'compareCompany');
        comp.compareCompany(entity, entity2);
        expect(companyService.compareCompany).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareBranch', () => {
      it('Should forward to branchService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(branchService, 'compareBranch');
        comp.compareBranch(entity, entity2);
        expect(branchService.compareBranch).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
