jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { WaitingRoomAttencionChannelService } from '../service/waiting-room-attencion-channel.service';

import { WaitingRoomAttencionChannelDeleteDialogComponent } from './waiting-room-attencion-channel-delete-dialog.component';

describe('WaitingRoomAttencionChannel Management Delete Component', () => {
  let comp: WaitingRoomAttencionChannelDeleteDialogComponent;
  let fixture: ComponentFixture<WaitingRoomAttencionChannelDeleteDialogComponent>;
  let service: WaitingRoomAttencionChannelService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, WaitingRoomAttencionChannelDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(WaitingRoomAttencionChannelDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(WaitingRoomAttencionChannelDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(WaitingRoomAttencionChannelService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
