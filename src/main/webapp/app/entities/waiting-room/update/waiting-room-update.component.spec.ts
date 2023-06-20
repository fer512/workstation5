import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WaitingRoomFormService } from './waiting-room-form.service';
import { WaitingRoomService } from '../service/waiting-room.service';
import { IWaitingRoom } from '../waiting-room.model';
import { IWaitingRoomAttencionChannel } from 'app/entities/waiting-room-attencion-channel/waiting-room-attencion-channel.model';
import { WaitingRoomAttencionChannelService } from 'app/entities/waiting-room-attencion-channel/service/waiting-room-attencion-channel.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { IBranch } from 'app/entities/branch/branch.model';
import { BranchService } from 'app/entities/branch/service/branch.service';

import { WaitingRoomUpdateComponent } from './waiting-room-update.component';

describe('WaitingRoom Management Update Component', () => {
  let comp: WaitingRoomUpdateComponent;
  let fixture: ComponentFixture<WaitingRoomUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let waitingRoomFormService: WaitingRoomFormService;
  let waitingRoomService: WaitingRoomService;
  let waitingRoomAttencionChannelService: WaitingRoomAttencionChannelService;
  let companyService: CompanyService;
  let branchService: BranchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), WaitingRoomUpdateComponent],
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
      .overrideTemplate(WaitingRoomUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WaitingRoomUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    waitingRoomFormService = TestBed.inject(WaitingRoomFormService);
    waitingRoomService = TestBed.inject(WaitingRoomService);
    waitingRoomAttencionChannelService = TestBed.inject(WaitingRoomAttencionChannelService);
    companyService = TestBed.inject(CompanyService);
    branchService = TestBed.inject(BranchService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call attencionChannel query and add missing value', () => {
      const waitingRoom: IWaitingRoom = { id: 456 };
      const attencionChannel: IWaitingRoomAttencionChannel = { id: 95674 };
      waitingRoom.attencionChannel = attencionChannel;

      const attencionChannelCollection: IWaitingRoomAttencionChannel[] = [{ id: 20383 }];
      jest.spyOn(waitingRoomAttencionChannelService, 'query').mockReturnValue(of(new HttpResponse({ body: attencionChannelCollection })));
      const expectedCollection: IWaitingRoomAttencionChannel[] = [attencionChannel, ...attencionChannelCollection];
      jest
        .spyOn(waitingRoomAttencionChannelService, 'addWaitingRoomAttencionChannelToCollectionIfMissing')
        .mockReturnValue(expectedCollection);

      activatedRoute.data = of({ waitingRoom });
      comp.ngOnInit();

      expect(waitingRoomAttencionChannelService.query).toHaveBeenCalled();
      expect(waitingRoomAttencionChannelService.addWaitingRoomAttencionChannelToCollectionIfMissing).toHaveBeenCalledWith(
        attencionChannelCollection,
        attencionChannel
      );
      expect(comp.attencionChannelsCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const waitingRoom: IWaitingRoom = { id: 456 };
      const company: ICompany = { id: 25417 };
      waitingRoom.company = company;

      const companyCollection: ICompany[] = [{ id: 65316 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ waitingRoom });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining)
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Branch query and add missing value', () => {
      const waitingRoom: IWaitingRoom = { id: 456 };
      const branches: IBranch[] = [{ id: 77322 }];
      waitingRoom.branches = branches;

      const branchCollection: IBranch[] = [{ id: 68013 }];
      jest.spyOn(branchService, 'query').mockReturnValue(of(new HttpResponse({ body: branchCollection })));
      const additionalBranches = [...branches];
      const expectedCollection: IBranch[] = [...additionalBranches, ...branchCollection];
      jest.spyOn(branchService, 'addBranchToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ waitingRoom });
      comp.ngOnInit();

      expect(branchService.query).toHaveBeenCalled();
      expect(branchService.addBranchToCollectionIfMissing).toHaveBeenCalledWith(
        branchCollection,
        ...additionalBranches.map(expect.objectContaining)
      );
      expect(comp.branchesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const waitingRoom: IWaitingRoom = { id: 456 };
      const attencionChannel: IWaitingRoomAttencionChannel = { id: 72438 };
      waitingRoom.attencionChannel = attencionChannel;
      const company: ICompany = { id: 11016 };
      waitingRoom.company = company;
      const branch: IBranch = { id: 24427 };
      waitingRoom.branches = [branch];

      activatedRoute.data = of({ waitingRoom });
      comp.ngOnInit();

      expect(comp.attencionChannelsCollection).toContain(attencionChannel);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.branchesSharedCollection).toContain(branch);
      expect(comp.waitingRoom).toEqual(waitingRoom);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWaitingRoom>>();
      const waitingRoom = { id: 123 };
      jest.spyOn(waitingRoomFormService, 'getWaitingRoom').mockReturnValue(waitingRoom);
      jest.spyOn(waitingRoomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ waitingRoom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: waitingRoom }));
      saveSubject.complete();

      // THEN
      expect(waitingRoomFormService.getWaitingRoom).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(waitingRoomService.update).toHaveBeenCalledWith(expect.objectContaining(waitingRoom));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWaitingRoom>>();
      const waitingRoom = { id: 123 };
      jest.spyOn(waitingRoomFormService, 'getWaitingRoom').mockReturnValue({ id: null });
      jest.spyOn(waitingRoomService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ waitingRoom: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: waitingRoom }));
      saveSubject.complete();

      // THEN
      expect(waitingRoomFormService.getWaitingRoom).toHaveBeenCalled();
      expect(waitingRoomService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWaitingRoom>>();
      const waitingRoom = { id: 123 };
      jest.spyOn(waitingRoomService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ waitingRoom });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(waitingRoomService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareWaitingRoomAttencionChannel', () => {
      it('Should forward to waitingRoomAttencionChannelService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(waitingRoomAttencionChannelService, 'compareWaitingRoomAttencionChannel');
        comp.compareWaitingRoomAttencionChannel(entity, entity2);
        expect(waitingRoomAttencionChannelService.compareWaitingRoomAttencionChannel).toHaveBeenCalledWith(entity, entity2);
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
