import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { QueueFormService } from './queue-form.service';
import { QueueService } from '../service/queue.service';
import { IQueue } from '../queue.model';
import { IAttencionChannel } from 'app/entities/attencion-channel/attencion-channel.model';
import { AttencionChannelService } from 'app/entities/attencion-channel/service/attencion-channel.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { IBranch } from 'app/entities/branch/branch.model';
import { BranchService } from 'app/entities/branch/service/branch.service';

import { QueueUpdateComponent } from './queue-update.component';

describe('Queue Management Update Component', () => {
  let comp: QueueUpdateComponent;
  let fixture: ComponentFixture<QueueUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let queueFormService: QueueFormService;
  let queueService: QueueService;
  let attencionChannelService: AttencionChannelService;
  let companyService: CompanyService;
  let branchService: BranchService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), QueueUpdateComponent],
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
      .overrideTemplate(QueueUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(QueueUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    queueFormService = TestBed.inject(QueueFormService);
    queueService = TestBed.inject(QueueService);
    attencionChannelService = TestBed.inject(AttencionChannelService);
    companyService = TestBed.inject(CompanyService);
    branchService = TestBed.inject(BranchService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call attencionChannel query and add missing value', () => {
      const queue: IQueue = { id: 456 };
      const attencionChannel: IAttencionChannel = { id: 59231 };
      queue.attencionChannel = attencionChannel;

      const attencionChannelCollection: IAttencionChannel[] = [{ id: 7435 }];
      jest.spyOn(attencionChannelService, 'query').mockReturnValue(of(new HttpResponse({ body: attencionChannelCollection })));
      const expectedCollection: IAttencionChannel[] = [attencionChannel, ...attencionChannelCollection];
      jest.spyOn(attencionChannelService, 'addAttencionChannelToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ queue });
      comp.ngOnInit();

      expect(attencionChannelService.query).toHaveBeenCalled();
      expect(attencionChannelService.addAttencionChannelToCollectionIfMissing).toHaveBeenCalledWith(
        attencionChannelCollection,
        attencionChannel
      );
      expect(comp.attencionChannelsCollection).toEqual(expectedCollection);
    });

    it('Should call Company query and add missing value', () => {
      const queue: IQueue = { id: 456 };
      const company: ICompany = { id: 25687 };
      queue.company = company;

      const companyCollection: ICompany[] = [{ id: 84787 }];
      jest.spyOn(companyService, 'query').mockReturnValue(of(new HttpResponse({ body: companyCollection })));
      const additionalCompanies = [company];
      const expectedCollection: ICompany[] = [...additionalCompanies, ...companyCollection];
      jest.spyOn(companyService, 'addCompanyToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ queue });
      comp.ngOnInit();

      expect(companyService.query).toHaveBeenCalled();
      expect(companyService.addCompanyToCollectionIfMissing).toHaveBeenCalledWith(
        companyCollection,
        ...additionalCompanies.map(expect.objectContaining)
      );
      expect(comp.companiesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Branch query and add missing value', () => {
      const queue: IQueue = { id: 456 };
      const branches: IBranch[] = [{ id: 90724 }];
      queue.branches = branches;

      const branchCollection: IBranch[] = [{ id: 14166 }];
      jest.spyOn(branchService, 'query').mockReturnValue(of(new HttpResponse({ body: branchCollection })));
      const additionalBranches = [...branches];
      const expectedCollection: IBranch[] = [...additionalBranches, ...branchCollection];
      jest.spyOn(branchService, 'addBranchToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ queue });
      comp.ngOnInit();

      expect(branchService.query).toHaveBeenCalled();
      expect(branchService.addBranchToCollectionIfMissing).toHaveBeenCalledWith(
        branchCollection,
        ...additionalBranches.map(expect.objectContaining)
      );
      expect(comp.branchesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const queue: IQueue = { id: 456 };
      const attencionChannel: IAttencionChannel = { id: 29536 };
      queue.attencionChannel = attencionChannel;
      const company: ICompany = { id: 56705 };
      queue.company = company;
      const branch: IBranch = { id: 20106 };
      queue.branches = [branch];

      activatedRoute.data = of({ queue });
      comp.ngOnInit();

      expect(comp.attencionChannelsCollection).toContain(attencionChannel);
      expect(comp.companiesSharedCollection).toContain(company);
      expect(comp.branchesSharedCollection).toContain(branch);
      expect(comp.queue).toEqual(queue);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQueue>>();
      const queue = { id: 123 };
      jest.spyOn(queueFormService, 'getQueue').mockReturnValue(queue);
      jest.spyOn(queueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ queue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: queue }));
      saveSubject.complete();

      // THEN
      expect(queueFormService.getQueue).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(queueService.update).toHaveBeenCalledWith(expect.objectContaining(queue));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQueue>>();
      const queue = { id: 123 };
      jest.spyOn(queueFormService, 'getQueue').mockReturnValue({ id: null });
      jest.spyOn(queueService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ queue: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: queue }));
      saveSubject.complete();

      // THEN
      expect(queueFormService.getQueue).toHaveBeenCalled();
      expect(queueService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IQueue>>();
      const queue = { id: 123 };
      jest.spyOn(queueService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ queue });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(queueService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareAttencionChannel', () => {
      it('Should forward to attencionChannelService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(attencionChannelService, 'compareAttencionChannel');
        comp.compareAttencionChannel(entity, entity2);
        expect(attencionChannelService.compareAttencionChannel).toHaveBeenCalledWith(entity, entity2);
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
