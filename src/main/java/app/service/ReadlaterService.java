package app.service;

import app.domain.Readlater;
import app.domain.User;
import app.repository.ReadlaterRepository;
import app.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReadlaterService {

    private final Logger log = LoggerFactory.getLogger(ReadlaterService.class);

    @Autowired
    private ReadlaterRepository readlaterRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void save(String name, Readlater readlater){
        User user = userRepository.findOneByLogin(name);
        readlater.setUser(user);
        readlaterRepository.save(readlater);
    }

}
