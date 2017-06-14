package com.baz.oops.api.filters;

import com.baz.oops.api.util.ParametersHandler;
import com.baz.oops.service.enums.State;
import com.baz.oops.service.model.Poll;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
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

    private List<String> tags;
    private State state;
    private String start;
    private String end;

    @Override
    public Predicate toPredicate(Root<Poll> root, CriteriaQuery<?> criteriaQuery,
                                 CriteriaBuilder criteriaBuilder) {
        ArrayList<Predicate> predicates = new ArrayList<>();

        if (ParametersHandler.isTagsValid(tags)) {
            //Expression<List<String>> pollsTags = root.get("tags");
            predicates.add(criteriaBuilder.isMember(tags.get(0), root.get("tags")));
        }
        if (state != null) {
            predicates.add(criteriaBuilder.equal(root.get("state"), state));
        }
        /*if (ParametersHandler.isDateValid(start)) {
            predicates.add(criteriaBuilder.equal(root.get("start"), start));
        }*/
        /*if (ParametersHandler.isDateValid(end)) {
            predicates.add(criteriaBuilder.equal(root.get("end"), end));
        }*/

        return predicates.size() <= 0 ? null :
                criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}
