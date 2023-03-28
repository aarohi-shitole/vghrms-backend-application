import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReportingDetailComponent } from './reporting-detail.component';

describe('Reporting Management Detail Component', () => {
  let comp: ReportingDetailComponent;
  let fixture: ComponentFixture<ReportingDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ReportingDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ reporting: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ReportingDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ReportingDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load reporting on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.reporting).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
