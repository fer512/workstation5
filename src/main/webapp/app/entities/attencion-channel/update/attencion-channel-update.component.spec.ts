import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AttencionChannelFormService } from './attencion-channel-form.service';
import { AttencionChannelService } from '../service/attencion-channel.service';
import { IAttencionChannel } from '../attencion-channel.model';

import { AttencionChannelUpdateComponent } from './attencion-channel-update.component';

describe('AttencionChannel Management Update Component', () => {
  let comp: AttencionChannelUpdateComponent;
  let fixture: ComponentFixture<AttencionChannelUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let attencionChannelFormService: AttencionChannelFormService;
  let attencionChannelService: AttencionChannelService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AttencionChannelUpdateComponent],
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
      .overrideTemplate(AttencionChannelUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AttencionChannelUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    attencionChannelFormService = TestBed.inject(AttencionChannelFormService);
    attencionChannelService = TestBed.inject(AttencionChannelService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const attencionChannel: IAttencionChannel = { id: 456 };

      activatedRoute.data = of({ attencionChannel });
      comp.ngOnInit();

      expect(comp.attencionChannel).toEqual(attencionChannel);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttencionChannel>>();
      const attencionChannel = { id: 123 };
      jest.spyOn(attencionChannelFormService, 'getAttencionChannel').mockReturnValue(attencionChannel);
      jest.spyOn(attencionChannelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attencionChannel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: attencionChannel }));
      saveSubject.complete();

      // THEN
      expect(attencionChannelFormService.getAttencionChannel).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(attencionChannelService.update).toHaveBeenCalledWith(expect.objectContaining(attencionChannel));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttencionChannel>>();
      const attencionChannel = { id: 123 };
      jest.spyOn(attencionChannelFormService, 'getAttencionChannel').mockReturnValue({ id: null });
      jest.spyOn(attencionChannelService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attencionChannel: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: attencionChannel }));
      saveSubject.complete();

      // THEN
      expect(attencionChannelFormService.getAttencionChannel).toHaveBeenCalled();
      expect(attencionChannelService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAttencionChannel>>();
      const attencionChannel = { id: 123 };
      jest.spyOn(attencionChannelService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ attencionChannel });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(attencionChannelService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
