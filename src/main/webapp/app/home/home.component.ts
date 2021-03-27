import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { AccountService } from 'app/core/auth/account.service';
import { Account } from 'app/core/user/account.model';
import { BowlingBoardService } from 'app/home/bowling-board.service';
import { GameService } from 'app/entities/game/game.service';
import { IPlayer } from 'app/shared/model/player.model';
import { IBowlingBoard, BowlingBoard } from 'app/shared/model/bowling-board.model';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { Game, IGame } from 'app/shared/model/game.model';
import { PlayerService } from 'app/entities/player/player.service';
import { HttpResponse } from '@angular/common/http';
import { IPlayerScores, PlayerScores } from 'app/shared/model/player-scores.model';
import { IScore, Score } from 'app/shared/model/score.model';
import { ScoreService } from 'app/entities/score/score.service';

@Component({
  selector: 'jhi-home',
  templateUrl: './home.component.html',
  styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit, OnDestroy {
  account: Account | null = null;
  authSubscription?: Subscription;
  gameLaunched = false;
  gameOver = false;
  availablePlayers?: IPlayer[];
  playersScores: IPlayerScores[] = [];
  currentIndexPlayer = 0;
  bowlingBoard: IBowlingBoard = new BowlingBoard();
  createGameForm = this.fb.group({
    gameName: [null, [Validators.required]]
  });

  choosePlayerForm = this.fb.group({
    selectedPlayer: new FormControl(this.availablePlayers),
    playerName: [null, [Validators.required]]
  });

  constructor(
    private accountService: AccountService,
    private loginModalService: LoginModalService,
    private fb: FormBuilder,
    private gameService: GameService,
    private playerService: PlayerService,
    private scoreService: ScoreService,
    private bowlingBoardService: BowlingBoardService
  ) {}

  ngOnInit(): void {
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  isGameLaunched(): boolean {
    return this.gameLaunched;
  }

  isStrike(scoresForTour: IScore[]): boolean {
    return scoresForTour[0]?.nbKeel === 10;
  }

  isSpare(scoresForTour: IScore[]): boolean {
    if (scoresForTour.length > 1 && scoresForTour[0]?.nbKeel && scoresForTour[1].nbKeel) {
      return scoresForTour[0].nbKeel !== scoresForTour[1].nbKeel && scoresForTour[0].nbKeel + scoresForTour[1].nbKeel === 10;
    }
    return false;
  }

  getAvailablePlayers(): IPlayer[] {
    if (!this.availablePlayers) {
      this.playerService.query().subscribe((res: HttpResponse<IPlayer[]>) => (this.availablePlayers = res.body || []));
    }

    return this.availablePlayers ? this.availablePlayers : [];
  }

  isGameOver(): boolean {
    if (!this.gameOver && this.bowlingBoard.players && this.bowlingBoard.players.length > 0) {
      this.gameOver = !this.bowlingBoard.players
        .map(playerScore => playerScore.scores)
        .map(this.isGameOverForPlayer)
        .includes(false);

      if (this.gameOver && this.bowlingBoard.game) {
        this.bowlingBoardService.getBoardFromGame(this.bowlingBoard.game).subscribe(finalBowlingBoard => {
          if (finalBowlingBoard.body) {
            this.bowlingBoard = finalBowlingBoard.body;
          }
        });
      }
    }

    return this.gameOver;
  }

  private isGameOverForPlayer(playerScores: IScore[] | undefined): boolean {
    const scoresAtTour10 = playerScores?.filter(score => score.tour === 10);
    if (scoresAtTour10 && scoresAtTour10?.length >= 2) {
      const nbKeelLancier1 = scoresAtTour10.find(score => score.lancier === 1)?.nbKeel;
      const nbKeelLancier2 = scoresAtTour10.find(score => score.lancier === 2)?.nbKeel;
      if (nbKeelLancier1 && nbKeelLancier2 && nbKeelLancier1 + nbKeelLancier2 >= 10) {
        return scoresAtTour10.find(score => score.lancier === 3)?.nbKeel !== undefined;
      } else {
        return true;
      }
    } else {
      return false;
    }
  }

  private createGameFromForm(): IGame {
    return {
      ...new Game(),
      name: this.createGameForm.get(['gameName'])!.value
    };
  }

  private createPlayerFromForm(): IPlayer {
    return {
      name: this.choosePlayerForm.get(['playerName'])!.value
    };
  }

  createGame(): void {
    this.gameService.create(this.createGameFromForm()).subscribe(resp => {
      const createdGame = resp.body;
      if (createdGame) {
        this.gameLaunched = true;
        this.bowlingBoard = {
          ...this.bowlingBoard,
          game: createdGame
        };
      }
    });
  }

  createPlayer(): void {
    const playerToCreate = this.createPlayerFromForm();
    this.playerService.create(playerToCreate).subscribe(resp => {
      const createdPlayer = resp.body;
      if (createdPlayer) {
        if (!this.bowlingBoard.players) {
          this.bowlingBoard.players = [];
        }
        this.bowlingBoard.players?.push({
          ...new PlayerScores(),
          player: createdPlayer,
          scores: []
        });
      }
    });
  }

  choosePlayer(): void {
    const playerId = this.choosePlayerForm.get('selectedPlayer')?.value.id;
    const index = this.availablePlayers?.map(player => player.id).indexOf(playerId);
    if (index !== undefined) {
      if (this.availablePlayers) {
        if (!this.bowlingBoard.players) {
          this.bowlingBoard.players = [];
        }
        this.bowlingBoard.players?.push({
          ...new PlayerScores(),
          player: this.availablePlayers[index],
          scores: []
        });
      }
      this.availablePlayers?.splice(index, 1);
    }
  }

  play(): void {
    if (this.bowlingBoard.players) {
      const playerScore = this.bowlingBoard.players[this.currentIndexPlayer];
      const maxTour = this.getPlayerCurrentTour(playerScore);
      const firstLancier = Math.floor(Math.random() * 11);
      const firstScore = {
        ...new Score(),
        tour: maxTour,
        game: this.bowlingBoard.game,
        lancier: 1,
        nbKeel: firstLancier,
        player: playerScore.player
      };
      this.scoreService.create(firstScore).subscribe(createdScoreResp => {
        if (createdScoreResp.body) {
          playerScore.scores?.push(createdScoreResp.body);
          let secondLancier = 0;

          if (firstLancier < 10 || maxTour === 10) {
            secondLancier = Math.floor(Math.random() * (11 - (firstLancier === 10 ? 0 : firstLancier)));
            this.scoreService
              .create({
                ...new Score(),
                tour: maxTour,
                game: this.bowlingBoard.game,
                lancier: 2,
                nbKeel: secondLancier,
                player: playerScore.player
              })
              .subscribe(createdScoreResp2 => {
                if (createdScoreResp2.body) {
                  playerScore.scores?.push(createdScoreResp2.body);
                }
              });
          }

          if (maxTour === 10 && firstLancier + secondLancier >= 10) {
            const nbKeelRemains = (firstLancier + secondLancier) % 10 === 0 ? 0 : secondLancier;
            const thirdLancier = Math.floor(Math.random() * (11 - nbKeelRemains));
            this.scoreService
              .create({
                ...new Score(),
                tour: maxTour,
                game: this.bowlingBoard.game,
                lancier: 3,
                nbKeel: thirdLancier,
                player: playerScore.player
              })
              .subscribe(createdScoreResp2 => {
                if (createdScoreResp2.body) {
                  playerScore.scores?.push(createdScoreResp2.body);
                }
              });
          }
        }
      });

      if (this.currentIndexPlayer === this.bowlingBoard.players.length - 1) {
        this.currentIndexPlayer = 0;
      } else {
        this.currentIndexPlayer++;
      }
    }
  }

  getScoresByTours(scoresForPlayer: IScore[]): Map<string, IScore[]> {
    const scoresByTours: Map<string, IScore[]> = new Map<string, IScore[]>();

    for (let i = 1; i < 11; i++) {
      scoresByTours.set(i.toString(), []);
    }

    scoresForPlayer?.forEach(value => {
      if (value?.tour) {
        scoresByTours.set(value.tour.toString(), [...(scoresByTours.get(value.tour.toString()) || []), value]);
      }
    });

    return scoresByTours;
  }

  login(): void {
    this.loginModalService.open();
  }

  private getPlayerCurrentTour(playerScores: PlayerScores): number {
    let maxTour = 0;
    playerScores.scores?.forEach(score => {
      if (score.tour) {
        maxTour = Math.max(score.tour, maxTour);
      }
    });
    return maxTour + 1;
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }
}
