package com.baz.oops.service.model;

import com.baz.oops.service.enums.State;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Created by arahis on 6/9/17.
 */
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(exclude = {"options", "id"})
@Entity
@Table(name = "polls")
public class Poll {
    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "create_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd'T'hh:mm:ssXXX")
    private Date createDate;

    @Column(name = "expire_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-mm-dd'T'hh:mm:ssXXX")
    private Date expireDate;

    @Column(name = "tags")
    @ElementCollection(targetClass = String.class)
    private List<String> tags;

    @Column(name = "multi_options")
    private boolean multiOptions;

    @Column(name = "total_votes")
    private int totalVotes;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "poll_id")
    @JsonManagedReference
    private List<Option> options;

    public Poll(String name, Date createDate, State state) {
        this.name = name;
        this.createDate = createDate;
        this.state = state;
        this.options = options;
    }

}
