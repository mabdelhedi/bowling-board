package fr.ippon.bowlingboard.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Score.
 */
@Entity
@Table(name = "score")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Score implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "nb_keel", nullable = false)
    private Integer nbKeel;

    @NotNull
    @Column(name = "tour", nullable = false)
    private Integer tour;

    @NotNull
    @Column(name = "lancier", nullable = false)
    private Integer lancier;

    @ManyToOne
    @JsonIgnoreProperties("scores")
    private Game game;

    @ManyToOne
    @JsonIgnoreProperties("scores")
    private Player player;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNbKeel() {
        return nbKeel;
    }

    public Score nbKeel(Integer nbKeel) {
        this.nbKeel = nbKeel;
        return this;
    }

    public void setNbKeel(Integer nbKeel) {
        this.nbKeel = nbKeel;
    }

    public Integer getTour() {
        return tour;
    }

    public Score tour(Integer tour) {
        this.tour = tour;
        return this;
    }

    public void setTour(Integer tour) {
        this.tour = tour;
    }

    public Integer getLancier() {
        return lancier;
    }

    public Score lancier(Integer lancier) {
        this.lancier = lancier;
        return this;
    }

    public void setLancier(Integer lancier) {
        this.lancier = lancier;
    }

    public Game getGame() {
        return game;
    }

    public Score game(Game game) {
        this.game = game;
        return this;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Player getPlayer() {
        return player;
    }

    public Score player(Player player) {
        this.player = player;
        return this;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Score)) {
            return false;
        }
        return id != null && id.equals(((Score) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Score{" +
            "id=" + getId() +
            ", nbKeel=" + getNbKeel() +
            ", tour=" + getTour() +
            ", lancier=" + getLancier() +
            "}";
    }
}
