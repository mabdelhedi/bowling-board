import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'player',
        loadChildren: () => import('./player/player.module').then(m => m.BowlingBoardPlayerModule)
      },
      {
        path: 'game',
        loadChildren: () => import('./game/game.module').then(m => m.BowlingBoardGameModule)
      },
      {
        path: 'score',
        loadChildren: () => import('./score/score.module').then(m => m.BowlingBoardScoreModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ]
})
export class BowlingBoardEntityModule {}
