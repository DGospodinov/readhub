package app.repository;

import app.domain.Feed;
import app.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the Item entity.
 */
public interface ItemRepository extends JpaRepository<Item,Long> {

    Item findByFeedAndLink(Feed feed, String link);

    List<Item> findByFeed(Feed one);
}
