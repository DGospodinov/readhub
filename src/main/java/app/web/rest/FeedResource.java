package app.web.rest;

import app.domain.Feed;
import app.domain.Item;
import app.repository.FeedRepository;
import app.repository.ItemRepository;
import app.service.FeedService;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.List;

/**
 * REST controller for managing Feed.
 */
@RestController
@RequestMapping("/api")
public class FeedResource {

    private final Logger log = LoggerFactory.getLogger(FeedResource.class);

    @Inject
    private FeedRepository feedRepository;
    @Inject
    private FeedService feedService;
    @Inject
    private ItemRepository itemRepository;

    /**
     * POST  /feeds -> Create a new feed.
     */
    @RequestMapping(value = "/feeds",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Feed feed,Principal principal) throws URISyntaxException {
        log.debug("REST request to save Feed : {}", feed);
        if (feed.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new feed cannot already have an ID").build();
        }
        feedService.save(feed,principal.getName());
        return ResponseEntity.created(new URI("/api/feeds/" + feed.getId())).build();
    }

    /**
     * PUT  /feeds -> Updates an existing feed.
     */
    @RequestMapping(value = "/feeds",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Feed feed,Principal principal) throws URISyntaxException {
        log.debug("REST request to update Feed : {}", feed);
        if (feed.getId() == null) {
            return create(feed, principal);
        }
        feedService.save(feed,principal.getName());
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /feeds -> get all the feeds.
     */
    @RequestMapping(value = "/feeds",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Feed> getAll() {
        log.debug("REST request to get all Feeds");
        return feedRepository.findAllForCurrentUser();
    }

    /**
     * GET  /feeds/:id -> get the "id" feed.
     */
    @RequestMapping(value = "/feeds/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Feed> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Feed : {}", id);
        Feed feed = feedRepository.findOne(id);
        if (feed == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(feed, HttpStatus.OK);
    }
    /**
     * GET  /items/:id -> get the "id" item.
     */
    @RequestMapping(value = "/feeds/{id}/items",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Item>> getItems(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Item : {}", id);
        List<Item> item = itemRepository.findByFeed(feedRepository.findOne(id));
        if (item == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    /**
     * DELETE  /feeds/:id -> delete the "id" feed.
     */
    @RequestMapping(value = "/feeds/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Feed : {}", id);
        feedRepository.delete(id);
    }
}
