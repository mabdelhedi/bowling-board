package fr.ippon.bowlingboard.web.rest;

import fr.ippon.bowlingboard.BowlingBoardApp;
import fr.ippon.bowlingboard.domain.Score;
import fr.ippon.bowlingboard.repository.ScoreRepository;
import fr.ippon.bowlingboard.service.ScoreService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ScoreResource} REST controller.
 */
@SpringBootTest(classes = BowlingBoardApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ScoreResourceIT {

    private static final Integer DEFAULT_NB_KEEL = 1;
    private static final Integer UPDATED_NB_KEEL = 2;

    private static final Integer DEFAULT_TOUR = 1;
    private static final Integer UPDATED_TOUR = 2;

    private static final Integer DEFAULT_LANCIER = 1;
    private static final Integer UPDATED_LANCIER = 2;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private ScoreService scoreService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScoreMockMvc;

    private Score score;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Score createEntity(EntityManager em) {
        Score score = new Score()
            .nbKeel(DEFAULT_NB_KEEL)
            .tour(DEFAULT_TOUR)
            .lancier(DEFAULT_LANCIER);
        return score;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Score createUpdatedEntity(EntityManager em) {
        Score score = new Score()
            .nbKeel(UPDATED_NB_KEEL)
            .tour(UPDATED_TOUR)
            .lancier(UPDATED_LANCIER);
        return score;
    }

    @BeforeEach
    public void initTest() {
        score = createEntity(em);
    }

    @Test
    @Transactional
    public void createScore() throws Exception {
        int databaseSizeBeforeCreate = scoreRepository.findAll().size();

        // Create the Score
        restScoreMockMvc.perform(post("/api/scores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(score)))
            .andExpect(status().isCreated());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeCreate + 1);
        Score testScore = scoreList.get(scoreList.size() - 1);
        assertThat(testScore.getNbKeel()).isEqualTo(DEFAULT_NB_KEEL);
        assertThat(testScore.getTour()).isEqualTo(DEFAULT_TOUR);
        assertThat(testScore.getLancier()).isEqualTo(DEFAULT_LANCIER);
    }

    @Test
    @Transactional
    public void createScoreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = scoreRepository.findAll().size();

        // Create the Score with an existing ID
        score.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restScoreMockMvc.perform(post("/api/scores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(score)))
            .andExpect(status().isBadRequest());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNbKeelIsRequired() throws Exception {
        int databaseSizeBeforeTest = scoreRepository.findAll().size();
        // set the field null
        score.setNbKeel(null);

        // Create the Score, which fails.

        restScoreMockMvc.perform(post("/api/scores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(score)))
            .andExpect(status().isBadRequest());

        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTourIsRequired() throws Exception {
        int databaseSizeBeforeTest = scoreRepository.findAll().size();
        // set the field null
        score.setTour(null);

        // Create the Score, which fails.

        restScoreMockMvc.perform(post("/api/scores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(score)))
            .andExpect(status().isBadRequest());

        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLancierIsRequired() throws Exception {
        int databaseSizeBeforeTest = scoreRepository.findAll().size();
        // set the field null
        score.setLancier(null);

        // Create the Score, which fails.

        restScoreMockMvc.perform(post("/api/scores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(score)))
            .andExpect(status().isBadRequest());

        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllScores() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get all the scoreList
        restScoreMockMvc.perform(get("/api/scores?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(score.getId().intValue())))
            .andExpect(jsonPath("$.[*].nbKeel").value(hasItem(DEFAULT_NB_KEEL)))
            .andExpect(jsonPath("$.[*].tour").value(hasItem(DEFAULT_TOUR)))
            .andExpect(jsonPath("$.[*].lancier").value(hasItem(DEFAULT_LANCIER)));
    }
    
    @Test
    @Transactional
    public void getScore() throws Exception {
        // Initialize the database
        scoreRepository.saveAndFlush(score);

        // Get the score
        restScoreMockMvc.perform(get("/api/scores/{id}", score.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(score.getId().intValue()))
            .andExpect(jsonPath("$.nbKeel").value(DEFAULT_NB_KEEL))
            .andExpect(jsonPath("$.tour").value(DEFAULT_TOUR))
            .andExpect(jsonPath("$.lancier").value(DEFAULT_LANCIER));
    }

    @Test
    @Transactional
    public void getNonExistingScore() throws Exception {
        // Get the score
        restScoreMockMvc.perform(get("/api/scores/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateScore() throws Exception {
        // Initialize the database
        scoreService.save(score);

        int databaseSizeBeforeUpdate = scoreRepository.findAll().size();

        // Update the score
        Score updatedScore = scoreRepository.findById(score.getId()).get();
        // Disconnect from session so that the updates on updatedScore are not directly saved in db
        em.detach(updatedScore);
        updatedScore
            .nbKeel(UPDATED_NB_KEEL)
            .tour(UPDATED_TOUR)
            .lancier(UPDATED_LANCIER);

        restScoreMockMvc.perform(put("/api/scores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedScore)))
            .andExpect(status().isOk());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeUpdate);
        Score testScore = scoreList.get(scoreList.size() - 1);
        assertThat(testScore.getNbKeel()).isEqualTo(UPDATED_NB_KEEL);
        assertThat(testScore.getTour()).isEqualTo(UPDATED_TOUR);
        assertThat(testScore.getLancier()).isEqualTo(UPDATED_LANCIER);
    }

    @Test
    @Transactional
    public void updateNonExistingScore() throws Exception {
        int databaseSizeBeforeUpdate = scoreRepository.findAll().size();

        // Create the Score

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScoreMockMvc.perform(put("/api/scores")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(score)))
            .andExpect(status().isBadRequest());

        // Validate the Score in the database
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteScore() throws Exception {
        // Initialize the database
        scoreService.save(score);

        int databaseSizeBeforeDelete = scoreRepository.findAll().size();

        // Delete the score
        restScoreMockMvc.perform(delete("/api/scores/{id}", score.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Score> scoreList = scoreRepository.findAll();
        assertThat(scoreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
