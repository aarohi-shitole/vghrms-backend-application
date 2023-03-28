import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FamilyInfoDetailComponent } from './family-info-detail.component';

describe('FamilyInfo Management Detail Component', () => {
  let comp: FamilyInfoDetailComponent;
  let fixture: ComponentFixture<FamilyInfoDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FamilyInfoDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ familyInfo: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FamilyInfoDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FamilyInfoDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load familyInfo on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.familyInfo).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
