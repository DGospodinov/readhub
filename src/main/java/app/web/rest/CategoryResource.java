package app.web.rest;

import app.domain.Category;
import app.domain.Feed;
import app.domain.Item;
import app.repository.CategoryRepository;
import app.repository.FeedRepository;
import app.repository.ItemRepository;
import app.service.CategoryService;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for managing Category.
 */
@RestController
@RequestMapping("/api")
public class CategoryResource {

    private final Logger log = LoggerFactory.getLogger(CategoryResource.class);

    @Inject
    private CategoryRepository categoryRepository;
    @Inject
    private CategoryService categoryService;
    @Inject
    private FeedRepository feedRepository;

    @Inject
    private ItemRepository itemRepository;

        /**
         * POST  /categorys -> Create a new category.
         */
    @RequestMapping(value = "/categorys",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@Valid @RequestBody Category category,Principal principal) throws URISyntaxException {
        log.debug("REST request to save Category : {}", category);
        if (category.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new category cannot already have an ID").build();
        }
        categoryService.save(category,principal.getName());
        return ResponseEntity.created(new URI("/api/categorys/" + category.getId())).build();
    }

    /**
     * PUT  /categorys -> Updates an existing category.
     */
    @RequestMapping(value = "/categorys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@Valid @RequestBody Category category,Principal principal) throws URISyntaxException {
        log.debug("REST request to update Category : {}", category);
        if (category.getId() == null) {
            return create(category, principal);
        }
        categoryService.save(category,principal.getName());
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /categorys -> get all the categorys.
     */
    @RequestMapping(value = "/categorys",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Category> getAll() {
        log.debug("REST request to get all Categorys");
        return categoryRepository.findAllForCurrentUser();
    }

    /**
     * GET  /categorys/:id -> get the "id" category.
     */
    @RequestMapping(value = "/categorys/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Category> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Category : {}", id);
        Category category = categoryRepository.findOne(id);
        if (category == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    /**
     * DELETE  /categorys/:id -> delete the "id" category.
     */
    @RequestMapping(value = "/categorys/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Category : {}", id);
        categoryRepository.delete(id);
    }
    /**
     * GET  /categorysfeeds/:id -> get the categoryFeeds.
     */
    @RequestMapping(value = "/categoryfeeds/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Feed>> getFeeds(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get CategoryFeeds : {}", id);
        Category category = categoryRepository.findOne(id);
        if (category == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        List<Feed> feeds = feedRepository.findByCategory(category);
        return new ResponseEntity<>(feeds, HttpStatus.OK);
    }
    /**
     * GET  /categorysfeeds/:id -> get the categoryFeeds.
     */
    @RequestMapping(value = "/categoryitems/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Item>> getItems(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get CategoryItems : {}", id);
        List<Item> items = new ArrayList<>();
        Category category = categoryRepository.findOne(id);
        if (category == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        for (Feed feed : feedRepository.findByCategory(category)) {
            for (Item item : itemRepository.findByFeed(feed)) {
                items.add(item);
            }
        }
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}

