package app.web.rest;

import app.Application;
import app.domain.Feed;
import app.repository.FeedRepository;

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
 * Test class for the FeedResource REST controller.
 *
 * @see FeedResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class FeedResourceTest {

    private static final String DEFAULT_URL = "SAMPLE_TEXT";
    private static final String UPDATED_URL = "UPDATED_TEXT";
    private static final String DEFAULT_NAME = "SAMPLE_TEXT";
    private static final String UPDATED_NAME = "UPDATED_TEXT";

    @Inject
    private FeedRepository feedRepository;

    private MockMvc restFeedMockMvc;

    private Feed feed;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FeedResource feedResource = new FeedResource();
        ReflectionTestUtils.setField(feedResource, "feedRepository", feedRepository);
        this.restFeedMockMvc = MockMvcBuilders.standaloneSetup(feedResource).build();
    }

    @Before
    public void initTest() {
        feed = new Feed();
        feed.setUrl(DEFAULT_URL);
        feed.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createFeed() throws Exception {
        int databaseSizeBeforeCreate = feedRepository.findAll().size();

        // Create the Feed
        restFeedMockMvc.perform(post("/api/feeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(feed)))
                .andExpect(status().isCreated());

        // Validate the Feed in the database
        List<Feed> feeds = feedRepository.findAll();
        assertThat(feeds).hasSize(databaseSizeBeforeCreate + 1);
        Feed testFeed = feeds.get(feeds.size() - 1);
        assertThat(testFeed.getUrl()).isEqualTo(DEFAULT_URL);
        assertThat(testFeed.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllFeeds() throws Exception {
        // Initialize the database
        feedRepository.saveAndFlush(feed);

        // Get all the feeds
        restFeedMockMvc.perform(get("/api/feeds"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(feed.getId().intValue())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getFeed() throws Exception {
        // Initialize the database
        feedRepository.saveAndFlush(feed);

        // Get the feed
        restFeedMockMvc.perform(get("/api/feeds/{id}", feed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(feed.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingFeed() throws Exception {
        // Get the feed
        restFeedMockMvc.perform(get("/api/feeds/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFeed() throws Exception {
        // Initialize the database
        feedRepository.saveAndFlush(feed);

		int databaseSizeBeforeUpdate = feedRepository.findAll().size();

        // Update the feed
        feed.setUrl(UPDATED_URL);
        feed.setName(UPDATED_NAME);
        restFeedMockMvc.perform(put("/api/feeds")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(feed)))
                .andExpect(status().isOk());

        // Validate the Feed in the database
        List<Feed> feeds = feedRepository.findAll();
        assertThat(feeds).hasSize(databaseSizeBeforeUpdate);
        Feed testFeed = feeds.get(feeds.size() - 1);
        assertThat(testFeed.getUrl()).isEqualTo(UPDATED_URL);
        assertThat(testFeed.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteFeed() throws Exception {
        // Initialize the database
        feedRepository.saveAndFlush(feed);

		int databaseSizeBeforeDelete = feedRepository.findAll().size();

        // Get the feed
        restFeedMockMvc.perform(delete("/api/feeds/{id}", feed.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Feed> feeds = feedRepository.findAll();
        assertThat(feeds).hasSize(databaseSizeBeforeDelete - 1);
    }
}
