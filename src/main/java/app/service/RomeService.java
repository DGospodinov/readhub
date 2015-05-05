package app.service;

import app.domain.Feed;
import app.domain.Item;
import com.sun.syndication.feed.synd.*;
import com.sun.syndication.fetcher.FeedFetcher;
import com.sun.syndication.fetcher.FetcherException;
import com.sun.syndication.fetcher.impl.FeedFetcherCache;
import com.sun.syndication.fetcher.impl.HashMapFeedInfoCache;
import com.sun.syndication.fetcher.impl.HttpURLFeedFetcher;
import com.sun.syndication.io.FeedException;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class RomeService {

    private final Logger log = LoggerFactory.getLogger(RomeService.class);

    FeedFetcherCache feedCache = new HashMapFeedInfoCache();
    FeedFetcher fetcher = new HttpURLFeedFetcher(feedCache);

    public void feedDetails(Feed feed){
        try {
            URL link = new URL(feed.getUrl());

            SyndFeed feedcontetnt = fetcher.retrieveFeed(link);
            SyndImage feedimg = feedcontetnt.getImage();


            if(feedimg != null){
                feed.setImageUrl(feedimg.getUrl());
            }

            feed.setDescription(feedcontetnt.getDescription());
            feed.setLink(feedcontetnt.getLink());
            feed.setName(feedcontetnt.getTitle());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FetcherException e) {
            e.printStackTrace();
        } catch (FeedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public List<Item> exttractItems(String url) {
        ArrayList<Item> list = new ArrayList<Item>();

        try {
            URL link = new URL(url);

            SyndFeed feed = fetcher.retrieveFeed(link);
            for(SyndEntry entry :(List<SyndEntry>) feed.getEntries()) {
                Item item = new Item();
                item.setTitle(entry.getTitle());
                item.setLink(entry.getLink());

                Date date = entry.getPublishedDate();
                DateTime dateTime = new DateTime(date);
                item.setPubDate(dateTime);

                item.setAuthor(entry.getAuthor());

                SyndContent content = entry.getDescription();
                item.setDesription(content.getValue());
                list.add(item);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FetcherException e) {
            e.printStackTrace();
        } catch (FeedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  list;

    }

}
