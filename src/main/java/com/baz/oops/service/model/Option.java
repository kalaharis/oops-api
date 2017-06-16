package com.baz.oops.service.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.dom4j.rule.NullAction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by arahis on 6/11/17.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude = {"id","poll"})
@JsonIgnoreProperties({"id","poll"})
@Entity
@Table(name = "options")
public class Option {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "votes_count")
    private int votesCount;

    @ManyToOne
    @JsonBackReference
    private Poll poll;

    public Option(String name) {
        this.name = name;
    }
}
