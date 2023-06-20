import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { WaitingRoomAttencionChannelFormService } from './waiting-room-attencion-channel-form.service';
import { WaitingRoomAttencionChannelService } from '../service/waiting-room-attencion-channel.service';
import { IWaitingRoomAttencionChannel } from '../waiting-room-attencion-channel.model';

import { WaitingRoomAttencionChannelUpdateComponent } from './waiting-room-attencion-channel-update.component';

describe('WaitingRoomAttencionChannel Management Update Component', () => {
  let comp: WaitingRoomAttencionChannelUpdateComponent;
  let fixture: ComponentFixture<WaitingRoomAttencionChannelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let waitingRoomAttencionChannelFormService: WaitingRoomAttencionChannelFormService;
  let waitingRoomAttencionChannelService: WaitingRoomAttencionChannelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), WaitingRoomAttencionChannelUpdateComponent],
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
      .overrideTemplate(WaitingRoomAttencionChannelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(WaitingRoomAttencionChannelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    waitingRoomAttencionChannelFormService = TestBed.inject(WaitingRoomAttencionChannelFormService);
    waitingRoomAttencionChannelService = TestBed.inject(WaitingRoomAttencionChannelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const waitingRoomAttencionChannel: IWaitingRoomAttencionChannel = { id: 456 };

      activatedRoute.data = of({ waitingRoomAttencionChannel });
      comp.ngOnInit();

      expect(comp.waitingRoomAttencionChannel).toEqual(waitingRoomAttencionChannel);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWaitingRoomAttencionChannel>>();
      const waitingRoomAttencionChannel = { id: 123 };
      jest.spyOn(waitingRoomAttencionChannelFormService, 'getWaitingRoomAttencionChannel').mockReturnValue(waitingRoomAttencionChannel);
      jest.spyOn(waitingRoomAttencionChannelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ waitingRoomAttencionChannel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: waitingRoomAttencionChannel }));
      saveSubject.complete();

      // THEN
      expect(waitingRoomAttencionChannelFormService.getWaitingRoomAttencionChannel).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(waitingRoomAttencionChannelService.update).toHaveBeenCalledWith(expect.objectContaining(waitingRoomAttencionChannel));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWaitingRoomAttencionChannel>>();
      const waitingRoomAttencionChannel = { id: 123 };
      jest.spyOn(waitingRoomAttencionChannelFormService, 'getWaitingRoomAttencionChannel').mockReturnValue({ id: null });
      jest.spyOn(waitingRoomAttencionChannelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ waitingRoomAttencionChannel: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: waitingRoomAttencionChannel }));
      saveSubject.complete();

      // THEN
      expect(waitingRoomAttencionChannelFormService.getWaitingRoomAttencionChannel).toHaveBeenCalled();
      expect(waitingRoomAttencionChannelService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IWaitingRoomAttencionChannel>>();
      const waitingRoomAttencionChannel = { id: 123 };
      jest.spyOn(waitingRoomAttencionChannelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ waitingRoomAttencionChannel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(waitingRoomAttencionChannelService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
