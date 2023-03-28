import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IFamilyInfo, NewFamilyInfo } from '../family-info.model';

export type PartialUpdateFamilyInfo = Partial<IFamilyInfo> & Pick<IFamilyInfo, 'id'>;

type RestOf<T extends IFamilyInfo | NewFamilyInfo> = Omit<T, 'dateOfBirth' | 'lastModified'> & {
  dateOfBirth?: string | null;
  lastModified?: string | null;
};

export type RestFamilyInfo = RestOf<IFamilyInfo>;

export type NewRestFamilyInfo = RestOf<NewFamilyInfo>;

export type PartialUpdateRestFamilyInfo = RestOf<PartialUpdateFamilyInfo>;

export type EntityResponseType = HttpResponse<IFamilyInfo>;
export type EntityArrayResponseType = HttpResponse<IFamilyInfo[]>;

@Injectable({ providedIn: 'root' })
export class FamilyInfoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/family-infos');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(familyInfo: NewFamilyInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(familyInfo);
    return this.http
      .post<RestFamilyInfo>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(familyInfo: IFamilyInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(familyInfo);
    return this.http
      .put<RestFamilyInfo>(`${this.resourceUrl}/${this.getFamilyInfoIdentifier(familyInfo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(familyInfo: PartialUpdateFamilyInfo): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(familyInfo);
    return this.http
      .patch<RestFamilyInfo>(`${this.resourceUrl}/${this.getFamilyInfoIdentifier(familyInfo)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestFamilyInfo>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestFamilyInfo[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getFamilyInfoIdentifier(familyInfo: Pick<IFamilyInfo, 'id'>): number {
    return familyInfo.id;
  }

  compareFamilyInfo(o1: Pick<IFamilyInfo, 'id'> | null, o2: Pick<IFamilyInfo, 'id'> | null): boolean {
    return o1 && o2 ? this.getFamilyInfoIdentifier(o1) === this.getFamilyInfoIdentifier(o2) : o1 === o2;
  }

  addFamilyInfoToCollectionIfMissing<Type extends Pick<IFamilyInfo, 'id'>>(
    familyInfoCollection: Type[],
    ...familyInfosToCheck: (Type | null | undefined)[]
  ): Type[] {
    const familyInfos: Type[] = familyInfosToCheck.filter(isPresent);
    if (familyInfos.length > 0) {
      const familyInfoCollectionIdentifiers = familyInfoCollection.map(familyInfoItem => this.getFamilyInfoIdentifier(familyInfoItem)!);
      const familyInfosToAdd = familyInfos.filter(familyInfoItem => {
        const familyInfoIdentifier = this.getFamilyInfoIdentifier(familyInfoItem);
        if (familyInfoCollectionIdentifiers.includes(familyInfoIdentifier)) {
          return false;
        }
        familyInfoCollectionIdentifiers.push(familyInfoIdentifier);
        return true;
      });
      return [...familyInfosToAdd, ...familyInfoCollection];
    }
    return familyInfoCollection;
  }

  protected convertDateFromClient<T extends IFamilyInfo | NewFamilyInfo | PartialUpdateFamilyInfo>(familyInfo: T): RestOf<T> {
    return {
      ...familyInfo,
      dateOfBirth: familyInfo.dateOfBirth?.format(DATE_FORMAT) ?? null,
      lastModified: familyInfo.lastModified?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restFamilyInfo: RestFamilyInfo): IFamilyInfo {
    return {
      ...restFamilyInfo,
      dateOfBirth: restFamilyInfo.dateOfBirth ? dayjs(restFamilyInfo.dateOfBirth) : undefined,
      lastModified: restFamilyInfo.lastModified ? dayjs(restFamilyInfo.lastModified) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestFamilyInfo>): HttpResponse<IFamilyInfo> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestFamilyInfo[]>): HttpResponse<IFamilyInfo[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
