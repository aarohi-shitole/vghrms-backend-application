import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PersonalIdDetailComponent } from './personal-id-detail.component';

describe('PersonalId Management Detail Component', () => {
  let comp: PersonalIdDetailComponent;
  let fixture: ComponentFixture<PersonalIdDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PersonalIdDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ personalId: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(PersonalIdDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(PersonalIdDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load personalId on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.personalId).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
