package app.service;

import app.domain.Feed;
import app.domain.Item;
import app.domain.User;
import app.repository.FeedRepository;
import app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ItemService {

    private final Logger log = LoggerFactory.getLogger(ItemService.class);

    @Autowired private UserRepository userRepository;
    @Autowired private FeedRepository feedRepository;

    @Transactional
    public List<Item> userItems(String login){

        List<Item> items = new ArrayList<Item>();
        User user = userRepository.findOneByLogin(login);
        for (Feed feed : feedRepository.findByUser(user)) {
            for (Item item : feed.getItems()) {
                items.add(item);
            }
        }
        return items;
    }
}
