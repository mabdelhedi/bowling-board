import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IScore, Score } from 'app/shared/model/score.model';
import { ScoreService } from './score.service';
import { IGame } from 'app/shared/model/game.model';
import { GameService } from 'app/entities/game/game.service';
import { IPlayer } from 'app/shared/model/player.model';
import { PlayerService } from 'app/entities/player/player.service';

type SelectableEntity = IGame | IPlayer;

@Component({
  selector: 'jhi-score-update',
  templateUrl: './score-update.component.html'
})
export class ScoreUpdateComponent implements OnInit {
  isSaving = false;
  games: IGame[] = [];
  players: IPlayer[] = [];

  editForm = this.fb.group({
    id: [],
    nbKeel: [null, [Validators.required]],
    tour: [null, [Validators.required]],
    lancier: [null, [Validators.required]],
    game: [],
    player: []
  });

  constructor(
    protected scoreService: ScoreService,
    protected gameService: GameService,
    protected playerService: PlayerService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ score }) => {
      this.updateForm(score);

      this.gameService.query().subscribe((res: HttpResponse<IGame[]>) => (this.games = res.body || []));

      this.playerService.query().subscribe((res: HttpResponse<IPlayer[]>) => (this.players = res.body || []));
    });
  }

  updateForm(score: IScore): void {
    this.editForm.patchValue({
      id: score.id,
      nbKeel: score.nbKeel,
      tour: score.tour,
      lancier: score.lancier,
      game: score.game,
      player: score.player
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const score = this.createFromForm();
    if (score.id !== undefined) {
      this.subscribeToSaveResponse(this.scoreService.update(score));
    } else {
      this.subscribeToSaveResponse(this.scoreService.create(score));
    }
  }

  private createFromForm(): IScore {
    return {
      ...new Score(),
      id: this.editForm.get(['id'])!.value,
      nbKeel: this.editForm.get(['nbKeel'])!.value,
      tour: this.editForm.get(['tour'])!.value,
      lancier: this.editForm.get(['lancier'])!.value,
      game: this.editForm.get(['game'])!.value,
      player: this.editForm.get(['player'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IScore>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
