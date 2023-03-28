import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonalDetailsDetailComponent } from './personal-details-detail.component';

describe('PersonalDetails Management Detail Component', () => {
  let comp: PersonalDetailsDetailComponent;
  let fixture: ComponentFixture<PersonalDetailsDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PersonalDetailsDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ personalDetails: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PersonalDetailsDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PersonalDetailsDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load personalDetails on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.personalDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
