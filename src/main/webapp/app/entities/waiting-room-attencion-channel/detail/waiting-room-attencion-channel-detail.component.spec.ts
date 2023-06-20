import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { WaitingRoomAttencionChannelDetailComponent } from './waiting-room-attencion-channel-detail.component';

describe('WaitingRoomAttencionChannel Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WaitingRoomAttencionChannelDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: WaitingRoomAttencionChannelDetailComponent,
              resolve: { waitingRoomAttencionChannel: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(WaitingRoomAttencionChannelDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load waitingRoomAttencionChannel on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', WaitingRoomAttencionChannelDetailComponent);

      // THEN
      expect(instance.waitingRoomAttencionChannel).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
