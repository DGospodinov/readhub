package app.web.rest;

import app.domain.Readlater;
import app.repository.ReadlaterRepository;
import app.service.ReadlaterService;
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
 * REST controller for managing Readlater.
 */
@RestController
@RequestMapping("/api")
public class ReadlaterResource {

    private final Logger log = LoggerFactory.getLogger(ReadlaterResource.class);

    @Inject
    private ReadlaterRepository readlaterRepository;

    @Inject
    private ReadlaterService readlaterService;

    /**
     * POST  /readlaters -> Create a new readlater.
     */
    @RequestMapping(value = "/readlaters",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> create(@RequestBody Readlater readlater,Principal principal) throws URISyntaxException {
        log.debug("REST request to save Readlater : {}", readlater);
        if (readlater.getId() != null) {
            return ResponseEntity.badRequest().header("Failure", "A new readlater cannot already have an ID").build();
        }
        readlaterService.save(principal.getName(),readlater);
        return ResponseEntity.created(new URI("/api/readlaters/" + readlater.getId())).build();
    }

    /**
     * PUT  /readlaters -> Updates an existing readlater.
     */
    @RequestMapping(value = "/readlaters",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> update(@RequestBody Readlater readlater,Principal principal) throws URISyntaxException {
        log.debug("REST request to update Readlater : {}", readlater);
        if (readlater.getId() == null) {
            return create(readlater,principal);
        }
        readlaterService.save(principal.getName(),readlater);
        return ResponseEntity.ok().build();
    }

    /**
     * GET  /readlaters -> get all the readlaters.
     */
    @RequestMapping(value = "/readlaters",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Readlater> getAll() {
        log.debug("REST request to get all Readlaters");
        return readlaterRepository.findAllForCurrentUser();
    }

    /**
     * GET  /readlaters/:id -> get the "id" readlater.
     */
    @RequestMapping(value = "/readlaters/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Readlater> get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get Readlater : {}", id);
        Readlater readlater = readlaterRepository.findOne(id);
        if (readlater == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(readlater, HttpStatus.OK);
    }

    /**
     * DELETE  /readlaters/:id -> delete the "id" readlater.
     */
    @RequestMapping(value = "/readlaters/{id}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public void delete(@PathVariable Long id) {
        log.debug("REST request to delete Readlater : {}", id);
        readlaterRepository.delete(id);
    }
}
