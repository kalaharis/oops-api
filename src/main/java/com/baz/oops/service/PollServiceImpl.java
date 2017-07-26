package com.baz.oops.service;

import com.baz.oops.api.JSON.CreatePollRequest;
import com.baz.oops.api.spring.PollsFilter;
import com.baz.oops.persistence.PollsRepository;
import com.baz.oops.service.PollService;
import com.baz.oops.service.crypt.Hashids;
import com.baz.oops.service.enums.State;
import com.baz.oops.service.exceptions.PollCreationException;
import com.baz.oops.service.exceptions.PollNotFoundException;
import com.baz.oops.service.exceptions.PollVotingException;
import com.baz.oops.service.exceptions.ServiceException;
import com.baz.oops.service.model.Poll;
import com.baz.oops.service.reqhandlers.CreatePollRequestHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

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
    @Transactional
    public Page<Poll> listAllByPage(Pageable pageable, PollsFilter filter) {
        Page<Poll> pollsPage = pollsRepository.findAll(filter, pageable);
        updatePolls(pollsPage);
        return pollsRepository.findAll(filter, pageable);
    }

    @Override
    @Transactional
    public Poll createPoll(CreatePollRequest request) throws ServiceException {

        Poll poll = new Poll(CreatePollRequestHandler.getNameFromRequest(request));
        poll.addOptions(CreatePollRequestHandler.getOptionsFromRequest(request));
        poll.setTags(CreatePollRequestHandler.getTagsFromRequest(request));
        poll.setExpireDate(CreatePollRequestHandler.getExpireDateFromRequest(request));
        poll.setMultiOptions(request.isMultiOptions());
        poll.setHidden(request.isHidden());

        poll = pollsRepository.save(poll);
        if (poll == null) {
            throw new ServiceException("Cannot create poll: something went wrong");
        }

        poll.setPublicId(createPublicId(poll.getPrivateId()));
        return pollsRepository.save(poll);
    }

    @Override
    @Transactional
    public Poll getById(String id) throws ServiceException {
        long privateId = getPrivateIdFromPublic(id);
        Poll poll = pollsRepository.findOne(privateId);
        if (poll == null) {
            throw new PollNotFoundException();
        }
        if (isPollOutDated(poll)) {
            poll = closePoll(poll);
        }
        return poll;
    }

    @Override
    @Transactional
    public Poll vote(String id, Set<Integer> indexes) throws ServiceException {
        if (indexes.size() == 0) {
            throw new PollVotingException("Cannot vote: no options selected");
        }

        Poll poll = getById(id);

        if (poll == null) {
            throw new PollNotFoundException();
        }

        if (poll.getState() == State.CLOSED) {
            throw new PollVotingException("Cannot vote: poll is closed");
        }

        if (!poll.isMultiOptions() && indexes.size() > 1) {
            throw new PollVotingException("Cannot vote: poll doesn't support multiple choices");
        }

        return updatePollVotes(poll, indexes);
    }

    private Poll updatePollVotes(Poll poll, Set<Integer> indexes) throws ServiceException {
        try {
            for (int indx : indexes) {
                if (indx < 0) {
                    throw new PollVotingException("Cannot vote: option index cannot be negative");
                }
                int updatedOptionVotes = poll.getOptions().get(indx).getVotesCount() + 1;
                poll.getOptions().get(indx).setVotesCount(updatedOptionVotes);
            }

            int updatedTotalVotes = poll.getTotalVotes() + 1;
            poll.setTotalVotes(updatedTotalVotes);
        } catch (IndexOutOfBoundsException ex) {
            throw new PollVotingException("Poll doesn't have such option(s)", ex);
        }
        return pollsRepository.save(poll);
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

    private boolean isPollOutDated(Poll poll) {
        if (poll.getState() == State.CLOSED) {
            return false;
        }
        Date expiringDate = poll.getExpireDate();
        if (expiringDate == null) {
            return false;
        }
        Date currentDate = new Date();
        if (!currentDate.after(expiringDate)) {
            return false;
        }
        log.warn("poll is outdated: " + poll);
        return true;
    }

    private Poll closePoll(Poll poll) {
        poll.setState(State.CLOSED);
        return pollsRepository.save(poll);
    }

    private void updatePolls(Page<Poll> pollsPage) {
        List<Poll> pollsList = pollsPage.getContent();
        for (Poll poll : pollsList) {
            if (isPollOutDated(poll)) {
                closePoll(poll);
            }
        }
    }
}
