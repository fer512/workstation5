jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { AttencionChannelService } from '../service/attencion-channel.service';

import { AttencionChannelDeleteDialogComponent } from './attencion-channel-delete-dialog.component';

describe('AttencionChannel Management Delete Component', () => {
  let comp: AttencionChannelDeleteDialogComponent;
  let fixture: ComponentFixture<AttencionChannelDeleteDialogComponent>;
  let service: AttencionChannelService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, AttencionChannelDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(AttencionChannelDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AttencionChannelDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AttencionChannelService);
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
