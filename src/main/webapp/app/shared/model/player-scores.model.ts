import { IScore } from 'app/shared/model/score.model';
import { IPlayer } from 'app/shared/model/player.model';

export interface IPlayerScores {
  player?: IPlayer;
  scores?: IScore[];
  currentScore?: number;
}

export class PlayerScores implements IPlayerScores {
  constructor(public player?: IPlayer, public scores?: IScore[], public currentScore?: number) {}
}
