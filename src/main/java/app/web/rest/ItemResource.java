package app.web.rest;

import app.domain.Item;
import app.repository.FeedRepository;
import app.repository.ItemRepository;
import app.service.ItemService;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;

/**
 * REST controller for managing Item.
 */
@RestController
@RequestMapping("/api")
public class ItemResource {

    private final Logger log = LoggerFactory.getLogger(ItemResource.class);

    @Inject
    private ItemRepository itemRepository;

    @Inject
    private ItemService itemService;
    @Inject
    private FeedRepository feedRepository;

    /**
     * GET  /items -> get all the items.
     */
    @RequestMapping(value = "/items",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Item> getAll(Principal principal) {
        log.debug("REST request to get all Items");
       return itemService.userItems(principal.getName());
    }

    /**
     * GET  /items/:id -> get the "id" item.
     */
    @RequestMapping(value = "/items/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Item> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Item : {}", id);
        Item item = itemRepository.findOne(id);
        if (item == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(item, HttpStatus.OK);
    }

}
