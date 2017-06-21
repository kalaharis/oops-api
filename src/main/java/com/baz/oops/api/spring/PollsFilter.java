package com.baz.oops.api.spring;

import com.baz.oops.api.util.ParametersHandler;
import com.baz.oops.service.enums.State;
import com.baz.oops.service.model.Poll;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by arahis on 6/13/17.
 */
@Slf4j
@Getter
@Setter
public class PollsFilter implements Specification<Poll> {

    private Set<String> tags;
    private String state;
    private String start;
    private String end;

    @Override
    public Predicate toPredicate(Root<Poll> root, CriteriaQuery<?> criteriaQuery,
                                 CriteriaBuilder criteriaBuilder) {
        ArrayList<Predicate> predicates = new ArrayList<>();

        if (ParametersHandler.isTagsValid(tags)) {
            log.debug("tags: " + tags.toString());
            Expression<Collection<String>> tagsIds = root.get("tags");
            Iterator<String> it = tags.iterator();
            while (it.hasNext()) {
                predicates.add(criteriaBuilder.isMember(it.next(), tagsIds));
            }
        }

        State pollState = ParametersHandler.getStateFromString(state);
        if (pollState != null) {
            log.debug("state string: " + state);
            log.debug("state: " + pollState);
            predicates.add(criteriaBuilder.equal(root.get("state"), pollState));
        }

        Date startDate = ParametersHandler.getDateFromString(start);
        if (startDate != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createDate"), startDate));
        }

        Date endDate = ParametersHandler.getDateFromString(end);
        if (endDate != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), endDate));
        }

        //return only public polls
        predicates.add(criteriaBuilder.equal(root.get("hidden"),false));

        return predicates.size() <= 0 ? null :
                criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
