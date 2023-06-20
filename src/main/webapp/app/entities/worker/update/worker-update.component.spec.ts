import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WorkerFormService } from './worker-form.service';
import { WorkerService } from '../service/worker.service';
import { IWorker } from '../worker.model';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { IBranch } from 'app/entities/branch/branch.model';
import { BranchService } from 'app/entities/branch/service/branch.service';
import { IWaitingRoom } from 'app/entities/waiting-room/waiting-room.model';
import { WaitingRoomService } from 'app/entities/waiting-room/service/waiting-room.service';

import { WorkerUpdateComponent } from './worker-update.component';

describe('Worker Management Update Component', () => {
  let comp: WorkerUpdateComponent;
  let fixture: ComponentFixture<WorkerUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let workerFormService: WorkerFormService;
  let workerService: WorkerService;
  let companyService: CompanyService;
  let branchService: BranchService;
  let waitingRoomService: WaitingRoomService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), WorkerUpdateComponent],
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
      .overrideTemplate(WorkerUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorkerUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    workerFormService = TestBed.inject(WorkerFormService);
    workerService = TestBed.inject(WorkerService);
    companyService = TestBed.inject(CompanyService);
    branchService = TestBed.inject(BranchService);
    waitingRoomService = TestBed.inject(WaitingRoomService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Company query and add missing value', () => {
      const worker: IWorker = { id: 456 };
      const company: ICompany = { id: 20954 };
      worker.company = company;

      const companyCollection: ICompany[] = [{ id: 40478 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ worker });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining)
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Branch query and add missing value', () => {
      const worker: IWorker = { id: 456 };
      const branches: IBranch[] = [{ id: 53489 }];
      worker.branches = branches;

      const branchCollection: IBranch[] = [{ id: 45650 }];
      jest.spyOn(branchService, 'query').mockReturnValue(of(new HttpResponse({ body: branchCollection })));
      const additionalBranches = [...branches];
      const expectedCollection: IBranch[] = [...additionalBranches, ...branchCollection];
      jest.spyOn(branchService, 'addBranchToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ worker });
      comp.ngOnInit();

      expect(branchService.query).toHaveBeenCalled();
      expect(branchService.addBranchToCollectionIfMissing).toHaveBeenCalledWith(
        branchCollection,
        ...additionalBranches.map(expect.objectContaining)
      );
      expect(comp.branchesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call WaitingRoom query and add missing value', () => {
      const worker: IWorker = { id: 456 };
      const waitingRoom: IWaitingRoom = { id: 9074 };
      worker.waitingRoom = waitingRoom;

      const waitingRoomCollection: IWaitingRoom[] = [{ id: 43686 }];
      jest.spyOn(waitingRoomService, 'query').mockReturnValue(of(new HttpResponse({ body: waitingRoomCollection })));
      const additionalWaitingRooms = [waitingRoom];
      const expectedCollection: IWaitingRoom[] = [...additionalWaitingRooms, ...waitingRoomCollection];
      jest.spyOn(waitingRoomService, 'addWaitingRoomToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ worker });
      comp.ngOnInit();

      expect(waitingRoomService.query).toHaveBeenCalled();
      expect(waitingRoomService.addWaitingRoomToCollectionIfMissing).toHaveBeenCalledWith(
        waitingRoomCollection,
        ...additionalWaitingRooms.map(expect.objectContaining)
      );
      expect(comp.waitingRoomsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const worker: IWorker = { id: 456 };
      const company: ICompany = { id: 38255 };
      worker.company = company;
      const branch: IBranch = { id: 86491 };
      worker.branches = [branch];
      const waitingRoom: IWaitingRoom = { id: 22200 };
      worker.waitingRoom = waitingRoom;

      activatedRoute.data = of({ worker });
      comp.ngOnInit();

      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.branchesSharedCollection).toContain(branch);
      expect(comp.waitingRoomsSharedCollection).toContain(waitingRoom);
      expect(comp.worker).toEqual(worker);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorker>>();
      const worker = { id: 123 };
      jest.spyOn(workerFormService, 'getWorker').mockReturnValue(worker);
      jest.spyOn(workerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ worker });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: worker }));
      saveSubject.complete();

      // THEN
      expect(workerFormService.getWorker).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(workerService.update).toHaveBeenCalledWith(expect.objectContaining(worker));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorker>>();
      const worker = { id: 123 };
      jest.spyOn(workerFormService, 'getWorker').mockReturnValue({ id: null });
      jest.spyOn(workerService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ worker: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: worker }));
      saveSubject.complete();

      // THEN
      expect(workerFormService.getWorker).toHaveBeenCalled();
      expect(workerService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorker>>();
      const worker = { id: 123 };
      jest.spyOn(workerService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ worker });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(workerService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
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

    describe('compareWaitingRoom', () => {
      it('Should forward to waitingRoomService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(waitingRoomService, 'compareWaitingRoom');
        comp.compareWaitingRoom(entity, entity2);
        expect(waitingRoomService.compareWaitingRoom).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
