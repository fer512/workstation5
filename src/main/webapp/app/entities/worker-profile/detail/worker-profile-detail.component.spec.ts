import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { WorkerProfileDetailComponent } from './worker-profile-detail.component';

describe('WorkerProfile Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WorkerProfileDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: WorkerProfileDetailComponent,
              resolve: { workerProfile: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(WorkerProfileDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load workerProfile on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', WorkerProfileDetailComponent);

      // THEN
      expect(instance.workerProfile).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
