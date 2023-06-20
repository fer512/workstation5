jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { OrderQueueService } from '../service/order-queue.service';

import { OrderQueueDeleteDialogComponent } from './order-queue-delete-dialog.component';

describe('OrderQueue Management Delete Component', () => {
  let comp: OrderQueueDeleteDialogComponent;
  let fixture: ComponentFixture<OrderQueueDeleteDialogComponent>;
  let service: OrderQueueService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, OrderQueueDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(OrderQueueDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(OrderQueueDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OrderQueueService);
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
