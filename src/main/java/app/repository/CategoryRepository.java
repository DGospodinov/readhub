package app.repository;

import app.domain.Category;
import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Category entity.
 */
public interface CategoryRepository extends JpaRepository<Category,Long> {

    @Query("select category from Category category where category.user.login = ?#{principal.username}")
    List<Category> findAllForCurrentUser();

}
