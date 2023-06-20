import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { WaitingRoomDetailComponent } from './waiting-room-detail.component';

describe('WaitingRoom Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WaitingRoomDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: WaitingRoomDetailComponent,
              resolve: { waitingRoom: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(WaitingRoomDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load waitingRoom on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', WaitingRoomDetailComponent);

      // THEN
      expect(instance.waitingRoom).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
