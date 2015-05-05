package app.service;

import app.domain.Category;
import app.domain.User;
import app.repository.CategoryRepository;
import app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoryService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    private final Logger log = LoggerFactory.getLogger(CategoryService.class);

    public void save(Category category, String login){
        User user = userRepository.findOneByLogin(login);
        categoryRepository.save(category);
        category.setUser(user);
    }

}
