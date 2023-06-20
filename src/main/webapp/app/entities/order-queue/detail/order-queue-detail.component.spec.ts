import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OrderQueueDetailComponent } from './order-queue-detail.component';

describe('OrderQueue Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrderQueueDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: OrderQueueDetailComponent,
              resolve: { orderQueue: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(OrderQueueDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load orderQueue on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', OrderQueueDetailComponent);

      // THEN
      expect(instance.orderQueue).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
