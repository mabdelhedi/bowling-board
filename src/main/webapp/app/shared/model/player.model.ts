import { IScore } from 'app/shared/model/score.model';

export interface IPlayer {
  id?: number;
  name?: string;
  scores?: IScore[];
}

export class Player implements IPlayer {
  constructor(public id?: number, public name?: string, public scores?: IScore[]) {}
}
