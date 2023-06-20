import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { WorkerProfileAttencionChannelDetailComponent } from './worker-profile-attencion-channel-detail.component';

describe('WorkerProfileAttencionChannel Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WorkerProfileAttencionChannelDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: WorkerProfileAttencionChannelDetailComponent,
              resolve: { workerProfileAttencionChannel: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(WorkerProfileAttencionChannelDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load workerProfileAttencionChannel on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', WorkerProfileAttencionChannelDetailComponent);

      // THEN
      expect(instance.workerProfileAttencionChannel).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
