package app.repository;

import app.domain.Readlater;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Readlater entity.
 */
public interface ReadlaterRepository extends JpaRepository<Readlater,Long> {

    @Query("select readlater from Readlater readlater where readlater.user.login = ?#{principal.username}")
    List<Readlater> findAllForCurrentUser();

}
