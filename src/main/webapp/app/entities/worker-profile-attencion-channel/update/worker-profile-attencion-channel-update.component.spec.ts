import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WorkerProfileAttencionChannelFormService } from './worker-profile-attencion-channel-form.service';
import { WorkerProfileAttencionChannelService } from '../service/worker-profile-attencion-channel.service';
import { IWorkerProfileAttencionChannel } from '../worker-profile-attencion-channel.model';

import { WorkerProfileAttencionChannelUpdateComponent } from './worker-profile-attencion-channel-update.component';

describe('WorkerProfileAttencionChannel Management Update Component', () => {
  let comp: WorkerProfileAttencionChannelUpdateComponent;
  let fixture: ComponentFixture<WorkerProfileAttencionChannelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let workerProfileAttencionChannelFormService: WorkerProfileAttencionChannelFormService;
  let workerProfileAttencionChannelService: WorkerProfileAttencionChannelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), WorkerProfileAttencionChannelUpdateComponent],
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
      .overrideTemplate(WorkerProfileAttencionChannelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WorkerProfileAttencionChannelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    workerProfileAttencionChannelFormService = TestBed.inject(WorkerProfileAttencionChannelFormService);
    workerProfileAttencionChannelService = TestBed.inject(WorkerProfileAttencionChannelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const workerProfileAttencionChannel: IWorkerProfileAttencionChannel = { id: 456 };

      activatedRoute.data = of({ workerProfileAttencionChannel });
      comp.ngOnInit();

      expect(comp.workerProfileAttencionChannel).toEqual(workerProfileAttencionChannel);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkerProfileAttencionChannel>>();
      const workerProfileAttencionChannel = { id: 123 };
      jest
        .spyOn(workerProfileAttencionChannelFormService, 'getWorkerProfileAttencionChannel')
        .mockReturnValue(workerProfileAttencionChannel);
      jest.spyOn(workerProfileAttencionChannelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workerProfileAttencionChannel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workerProfileAttencionChannel }));
      saveSubject.complete();

      // THEN
      expect(workerProfileAttencionChannelFormService.getWorkerProfileAttencionChannel).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(workerProfileAttencionChannelService.update).toHaveBeenCalledWith(expect.objectContaining(workerProfileAttencionChannel));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkerProfileAttencionChannel>>();
      const workerProfileAttencionChannel = { id: 123 };
      jest.spyOn(workerProfileAttencionChannelFormService, 'getWorkerProfileAttencionChannel').mockReturnValue({ id: null });
      jest.spyOn(workerProfileAttencionChannelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workerProfileAttencionChannel: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: workerProfileAttencionChannel }));
      saveSubject.complete();

      // THEN
      expect(workerProfileAttencionChannelFormService.getWorkerProfileAttencionChannel).toHaveBeenCalled();
      expect(workerProfileAttencionChannelService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWorkerProfileAttencionChannel>>();
      const workerProfileAttencionChannel = { id: 123 };
      jest.spyOn(workerProfileAttencionChannelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ workerProfileAttencionChannel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(workerProfileAttencionChannelService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
