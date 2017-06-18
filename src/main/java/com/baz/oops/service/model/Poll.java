package com.baz.oops.service.model;

import com.baz.oops.service.enums.State;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.annotations.CollectionType;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

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
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(exclude = {"id","options"})
@ToString(exclude = {"id"})
@Entity
@Table(name = "polls")
public class Poll {
    @Column(nullable = false)
    private String name;

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "create_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private Date createDate;

    @Column(name = "expire_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private Date expireDate;

    @Column(name = "tags")
    @ElementCollection(targetClass = String.class)
    private Set<String> tags;

    @Column(name = "multi_options")
    private boolean multiOptions;

    @Column(name = "total_votes")
    private int totalVotes;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private State state;

    @Setter(AccessLevel.PRIVATE)
    @OneToMany(mappedBy = "poll",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    @JsonManagedReference
    private List<Option> options;

    public void addOption(Option option) {
        option.setPoll(this);
        this.options.add(option);
    }

    public void addOptions(List<Option> options) {
        for (Option option : options) {
            addOption(option);
        }
    }

    private void initCreationDate() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC+00:00"));
        calendar.set(Calendar.MILLISECOND, 0);
        this.createDate = calendar.getTime();
    }

    public Poll(String name) {
        this.name = name;

        this.state = State.OPEN;
        this.tags = new HashSet<>();
        this.options = new ArrayList<>();
        initCreationDate();
    }

}
