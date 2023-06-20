import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { WorkerDetailComponent } from './worker-detail.component';

describe('Worker Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WorkerDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: WorkerDetailComponent,
              resolve: { worker: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(WorkerDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load worker on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', WorkerDetailComponent);

      // THEN
      expect(instance.worker).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
