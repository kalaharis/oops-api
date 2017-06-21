package com.baz.oops.service.impl;

import com.baz.oops.api.spring.PollsFilter;
import com.baz.oops.persistence.PollsRepository;
import com.baz.oops.service.PollService;
import com.baz.oops.service.crypt.Hashids;
import com.baz.oops.service.model.Option;
import com.baz.oops.service.model.Poll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by arahis on 6/11/17.
 */
@Slf4j
@Service
public class PollServiceImpl implements PollService {

    private PollsRepository pollsRepository;

    private final String SALT = System.getenv("SALT");
    private final String MIN_HASH_LENGTH = System.getenv("MIN_HASH_LENGTH");

    @Autowired
    public PollServiceImpl(PollsRepository pollsRepository) {
        this.pollsRepository = pollsRepository;
    }

    @Override
    public Page<Poll> listAllByPage(Pageable pageable, PollsFilter filter) {
        return pollsRepository.findAll(filter, pageable);
    }

    @Override
    @Transactional
    public Poll createPoll(String name, List<Option> options) {
        Poll poll = new Poll(name);
        poll.addOptions(options);

        poll = pollsRepository.save(poll);

        poll.setPublicId(createPublicId(poll.getPrivateId()));

        return pollsRepository.save(poll);
    }

    @Override
    public Poll getById(String id) {
        long privateId = getPrivateIdFromPublic(id);
        Poll poll = pollsRepository.findOne(privateId);
        return poll;
    }

    @Override
    @Transactional
    public Poll vote(String id, int optionIndx) {
        long privateId = getPrivateIdFromPublic(id);
        Poll poll = pollsRepository.findOne(privateId);
        if (poll == null) {
            return null;
        }
        try {
            poll.getOptions().get(optionIndx).vote();
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
        Poll updatedPoll = pollsRepository.save(poll);
        return updatedPoll;
    }


    private String createPublicId(long privateId) {
        log.info("encryption of private id...");
        log.info("result: private id = " + privateId);
        Hashids hashids = new Hashids(SALT, Integer.parseInt(MIN_HASH_LENGTH));
        String publicId = hashids.encrypt(privateId);

        log.info("given: public id = " + publicId);
        return publicId;
    }

    private long getPrivateIdFromPublic(String publicId) {
        log.info("decryption of public id...");
        log.info("given: public id = " + publicId);

        Hashids hashids = new Hashids(SALT, Integer.parseInt(MIN_HASH_LENGTH));
        long[] ids = hashids.decrypt(publicId);
        if (ids.length == 0) {
            return 0;
        }
        long privateId = ids[0];

        log.info("result: private id = " + privateId);
        return privateId;
    }


}
