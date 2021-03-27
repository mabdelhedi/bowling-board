import { IScore } from 'app/shared/model/score.model';

export interface IGame {
  id?: number;
  name?: string;
  scores?: IScore[];
}

export class Game implements IGame {
  constructor(public id?: number, public name?: string, public scores?: IScore[]) {}
}
