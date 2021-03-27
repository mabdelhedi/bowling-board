import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { IGame } from 'app/shared/model/game.model';
import { IBowlingBoard } from 'app/shared/model/bowling-board.model';

type EntityResponseType = HttpResponse<IBowlingBoard>;

@Injectable({ providedIn: 'root' })
export class BowlingBoardService {
  public resourceUrl = SERVER_API_URL + 'api/bowling-board';

  constructor(protected http: HttpClient) {}

  getBoardFromGame(game: IGame): Observable<EntityResponseType> {
    return this.http.post<IBowlingBoard>(this.resourceUrl, game, { observe: 'response' });
  }
}
