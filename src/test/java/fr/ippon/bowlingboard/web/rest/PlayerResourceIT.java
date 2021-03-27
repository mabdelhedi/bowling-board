package fr.ippon.bowlingboard.web.rest;

import fr.ippon.bowlingboard.BowlingBoardApp;
import fr.ippon.bowlingboard.domain.Player;
import fr.ippon.bowlingboard.repository.PlayerRepository;
import fr.ippon.bowlingboard.service.PlayerService;

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
 * Integration tests for the {@link PlayerResource} REST controller.
 */
@SpringBootTest(classes = BowlingBoardApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class PlayerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlayerMockMvc;

    private Player player;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Player createEntity(EntityManager em) {
        Player player = new Player()
            .name(DEFAULT_NAME);
        return player;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Player createUpdatedEntity(EntityManager em) {
        Player player = new Player()
            .name(UPDATED_NAME);
        return player;
    }

    @BeforeEach
    public void initTest() {
        player = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlayer() throws Exception {
        int databaseSizeBeforeCreate = playerRepository.findAll().size();

        // Create the Player
        restPlayerMockMvc.perform(post("/api/players")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(player)))
            .andExpect(status().isCreated());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeCreate + 1);
        Player testPlayer = playerList.get(playerList.size() - 1);
        assertThat(testPlayer.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createPlayerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = playerRepository.findAll().size();

        // Create the Player with an existing ID
        player.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerMockMvc.perform(post("/api/players")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(player)))
            .andExpect(status().isBadRequest());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = playerRepository.findAll().size();
        // set the field null
        player.setName(null);

        // Create the Player, which fails.

        restPlayerMockMvc.perform(post("/api/players")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(player)))
            .andExpect(status().isBadRequest());

        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlayers() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList
        restPlayerMockMvc.perform(get("/api/players?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(player.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getPlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get the player
        restPlayerMockMvc.perform(get("/api/players/{id}", player.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(player.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingPlayer() throws Exception {
        // Get the player
        restPlayerMockMvc.perform(get("/api/players/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlayer() throws Exception {
        // Initialize the database
        playerService.save(player);

        int databaseSizeBeforeUpdate = playerRepository.findAll().size();

        // Update the player
        Player updatedPlayer = playerRepository.findById(player.getId()).get();
        // Disconnect from session so that the updates on updatedPlayer are not directly saved in db
        em.detach(updatedPlayer);
        updatedPlayer
            .name(UPDATED_NAME);

        restPlayerMockMvc.perform(put("/api/players")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPlayer)))
            .andExpect(status().isOk());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeUpdate);
        Player testPlayer = playerList.get(playerList.size() - 1);
        assertThat(testPlayer.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingPlayer() throws Exception {
        int databaseSizeBeforeUpdate = playerRepository.findAll().size();

        // Create the Player

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerMockMvc.perform(put("/api/players")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(player)))
            .andExpect(status().isBadRequest());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlayer() throws Exception {
        // Initialize the database
        playerService.save(player);

        int databaseSizeBeforeDelete = playerRepository.findAll().size();

        // Delete the player
        restPlayerMockMvc.perform(delete("/api/players/{id}", player.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
