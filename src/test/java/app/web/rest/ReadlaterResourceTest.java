package app.web.rest;

import app.Application;
import app.domain.Readlater;
import app.repository.ReadlaterRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ReadlaterResource REST controller.
 *
 * @see ReadlaterResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ReadlaterResourceTest {


    @Inject
    private ReadlaterRepository readlaterRepository;

    private MockMvc restReadlaterMockMvc;

    private Readlater readlater;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ReadlaterResource readlaterResource = new ReadlaterResource();
        ReflectionTestUtils.setField(readlaterResource, "readlaterRepository", readlaterRepository);
        this.restReadlaterMockMvc = MockMvcBuilders.standaloneSetup(readlaterResource).build();
    }

    @Before
    public void initTest() {
        readlater = new Readlater();
    }

    @Test
    @Transactional
    public void createReadlater() throws Exception {
        int databaseSizeBeforeCreate = readlaterRepository.findAll().size();

        // Create the Readlater
        restReadlaterMockMvc.perform(post("/api/readlaters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(readlater)))
                .andExpect(status().isCreated());

        // Validate the Readlater in the database
        List<Readlater> readlaters = readlaterRepository.findAll();
        assertThat(readlaters).hasSize(databaseSizeBeforeCreate + 1);
        Readlater testReadlater = readlaters.get(readlaters.size() - 1);
    }

    @Test
    @Transactional
    public void getAllReadlaters() throws Exception {
        // Initialize the database
        readlaterRepository.saveAndFlush(readlater);

        // Get all the readlaters
        restReadlaterMockMvc.perform(get("/api/readlaters"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(readlater.getId().intValue())));
    }

    @Test
    @Transactional
    public void getReadlater() throws Exception {
        // Initialize the database
        readlaterRepository.saveAndFlush(readlater);

        // Get the readlater
        restReadlaterMockMvc.perform(get("/api/readlaters/{id}", readlater.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(readlater.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingReadlater() throws Exception {
        // Get the readlater
        restReadlaterMockMvc.perform(get("/api/readlaters/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReadlater() throws Exception {
        // Initialize the database
        readlaterRepository.saveAndFlush(readlater);

		int databaseSizeBeforeUpdate = readlaterRepository.findAll().size();

        // Update the readlater
        restReadlaterMockMvc.perform(put("/api/readlaters")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(readlater)))
                .andExpect(status().isOk());

        // Validate the Readlater in the database
        List<Readlater> readlaters = readlaterRepository.findAll();
        assertThat(readlaters).hasSize(databaseSizeBeforeUpdate);
        Readlater testReadlater = readlaters.get(readlaters.size() - 1);
    }

    @Test
    @Transactional
    public void deleteReadlater() throws Exception {
        // Initialize the database
        readlaterRepository.saveAndFlush(readlater);

		int databaseSizeBeforeDelete = readlaterRepository.findAll().size();

        // Get the readlater
        restReadlaterMockMvc.perform(delete("/api/readlaters/{id}", readlater.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Readlater> readlaters = readlaterRepository.findAll();
        assertThat(readlaters).hasSize(databaseSizeBeforeDelete - 1);
    }
}
