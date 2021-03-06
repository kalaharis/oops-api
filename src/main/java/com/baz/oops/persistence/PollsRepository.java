package com.baz.oops.persistence;

import com.baz.oops.service.model.Poll;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by arahis on 6/11/17.
 */
@Repository
public interface PollsRepository extends
        PagingAndSortingRepository<Poll, Long>, JpaSpecificationExecutor<Poll> {

}
