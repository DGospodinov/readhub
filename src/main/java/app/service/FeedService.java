package app.service;

import app.domain.Feed;
import app.domain.Item;
import app.domain.User;
import app.repository.FeedRepository;
import app.repository.ItemRepository;
import app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FeedService {

    @Autowired
    private RomeService rome;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FeedRepository feedRepository;

    private final Logger log = LoggerFactory.getLogger(FeedService.class);

    public void saveItems(Feed feed) {
        List<Item> items = rome.exttractItems(feed.getUrl());

        for (Item item : items) {
            Item saved = itemRepository.findByFeedAndLink(feed, item.getLink());
            if (saved == null) {
                item.setFeed(feed);
                itemRepository.save(item);
            }
        }
    }

    @Scheduled(fixedDelay = 50000)
    public void refresh() {
        List<Feed> feeds = feedRepository.findAll();
        for (Feed feed : feeds) {
            saveItems(feed);
        }
    }

    @Transactional
    public void save(Feed feed, String name) {
        User user = userRepository.findOneByLogin(name);
        feedRepository.save(feed);
        feed.setUser(user);
        rome.feedDetails(feed);
        saveItems(feed);
    }

}
