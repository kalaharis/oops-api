package com.baz.oops.api.filters;

import com.baz.oops.service.enums.State;
import com.baz.oops.service.model.Poll;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by arahis on 6/13/17.
 */
@Getter
@Setter
public class PollsFiler implements Specification<Poll> {

    private String[] tags;
    private State state;
    private String start;
    private String end;

    @Override
    public Predicate toPredicate(Root<Poll> root, CriteriaQuery<?> criteriaQuery,
                                 CriteriaBuilder criteriaBuilder) {
        ArrayList<Predicate> predicates = new ArrayList<>();

        return predicates.size() <= 0 ? null :
                criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
