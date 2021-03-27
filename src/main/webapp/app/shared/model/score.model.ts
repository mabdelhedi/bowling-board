import { IGame } from 'app/shared/model/game.model';
import { IPlayer } from 'app/shared/model/player.model';

export interface IScore {
  id?: number;
  nbKeel?: number;
  tour?: number;
  lancier?: number;
  game?: IGame;
  player?: IPlayer;
}

export class Score implements IScore {
  constructor(
    public id?: number,
    public nbKeel?: number,
    public tour?: number,
    public lancier?: number,
    public game?: IGame,
    public player?: IPlayer
  ) {}
}
