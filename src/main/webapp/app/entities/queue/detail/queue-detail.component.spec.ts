import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { QueueDetailComponent } from './queue-detail.component';

describe('Queue Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QueueDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: QueueDetailComponent,
              resolve: { queue: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(QueueDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load queue on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', QueueDetailComponent);

      // THEN
      expect(instance.queue).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
