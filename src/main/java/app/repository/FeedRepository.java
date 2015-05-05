package app.repository;

import app.domain.Category;
import app.domain.Feed;
import app.domain.User;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Feed entity.
 */
public interface FeedRepository extends JpaRepository<Feed,Long> {

    @Query("select feed from Feed feed where feed.user.login = ?#{principal.username}")
    List<Feed> findAllForCurrentUser();

    List<Feed> findByUser(User user);

    List<Feed> findByCategory(Category category);
}
