import { IGame } from 'app/shared/model/game.model';
import { IPlayerScores } from 'app/shared/model/player-scores.model';

export interface IBowlingBoard {
  game?: IGame;
  players?: IPlayerScores[];
}

export class BowlingBoard implements IBowlingBoard {
  constructor(public game?: IGame, public players?: IPlayerScores[]) {}
}
