package app.web.rest;

import app.Application;
import app.domain.Item;
import app.repository.ItemRepository;

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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ItemResource REST controller.
 *
 * @see ItemResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ItemResourceTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static final String DEFAULT_TITLE = "SAMPLE_TEXT";
    private static final String UPDATED_TITLE = "UPDATED_TEXT";
    private static final String DEFAULT_DESRIPTION = "SAMPLE_TEXT";
    private static final String UPDATED_DESRIPTION = "UPDATED_TEXT";

    private static final DateTime DEFAULT_PUB_DATE = new DateTime(0L, DateTimeZone.UTC);
    private static final DateTime UPDATED_PUB_DATE = new DateTime(DateTimeZone.UTC).withMillisOfSecond(0);
    private static final String DEFAULT_PUB_DATE_STR = dateTimeFormatter.print(DEFAULT_PUB_DATE);
    private static final String DEFAULT_LINK = "SAMPLE_TEXT";
    private static final String UPDATED_LINK = "UPDATED_TEXT";

    @Inject
    private ItemRepository itemRepository;

    private MockMvc restItemMockMvc;

    private Item item;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ItemResource itemResource = new ItemResource();
        ReflectionTestUtils.setField(itemResource, "itemRepository", itemRepository);
        this.restItemMockMvc = MockMvcBuilders.standaloneSetup(itemResource).build();
    }

    @Before
    public void initTest() {
        item = new Item();
        item.setTitle(DEFAULT_TITLE);
        item.setDesription(DEFAULT_DESRIPTION);
        item.setPubDate(DEFAULT_PUB_DATE);
        item.setLink(DEFAULT_LINK);
    }

    @Test
    @Transactional
    public void createItem() throws Exception {
        int databaseSizeBeforeCreate = itemRepository.findAll().size();

        // Create the Item
        restItemMockMvc.perform(post("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isCreated());

        // Validate the Item in the database
        List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(databaseSizeBeforeCreate + 1);
        Item testItem = items.get(items.size() - 1);
        assertThat(testItem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testItem.getDesription()).isEqualTo(DEFAULT_DESRIPTION);
        assertThat(testItem.getPubDate().toDateTime(DateTimeZone.UTC)).isEqualTo(DEFAULT_PUB_DATE);
        assertThat(testItem.getLink()).isEqualTo(DEFAULT_LINK);
    }

    @Test
    @Transactional
    public void getAllItems() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get all the items
        restItemMockMvc.perform(get("/api/items"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(item.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].desription").value(hasItem(DEFAULT_DESRIPTION.toString())))
                .andExpect(jsonPath("$.[*].pubDate").value(hasItem(DEFAULT_PUB_DATE_STR)))
                .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK.toString())));
    }

    @Test
    @Transactional
    public void getItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

        // Get the item
        restItemMockMvc.perform(get("/api/items/{id}", item.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(item.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.desription").value(DEFAULT_DESRIPTION.toString()))
            .andExpect(jsonPath("$.pubDate").value(DEFAULT_PUB_DATE_STR))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingItem() throws Exception {
        // Get the item
        restItemMockMvc.perform(get("/api/items/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

		int databaseSizeBeforeUpdate = itemRepository.findAll().size();

        // Update the item
        item.setTitle(UPDATED_TITLE);
        item.setDesription(UPDATED_DESRIPTION);
        item.setPubDate(UPDATED_PUB_DATE);
        item.setLink(UPDATED_LINK);
        restItemMockMvc.perform(put("/api/items")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(item)))
                .andExpect(status().isOk());

        // Validate the Item in the database
        List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(databaseSizeBeforeUpdate);
        Item testItem = items.get(items.size() - 1);
        assertThat(testItem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testItem.getDesription()).isEqualTo(UPDATED_DESRIPTION);
        assertThat(testItem.getPubDate().toDateTime(DateTimeZone.UTC)).isEqualTo(UPDATED_PUB_DATE);
        assertThat(testItem.getLink()).isEqualTo(UPDATED_LINK);
    }

    @Test
    @Transactional
    public void deleteItem() throws Exception {
        // Initialize the database
        itemRepository.saveAndFlush(item);

		int databaseSizeBeforeDelete = itemRepository.findAll().size();

        // Get the item
        restItemMockMvc.perform(delete("/api/items/{id}", item.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Item> items = itemRepository.findAll();
        assertThat(items).hasSize(databaseSizeBeforeDelete - 1);
    }
}
