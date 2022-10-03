package com.project.gamification.dto;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;
    @Column
    private String name;
    @Column
    private LocalDateTime datetimestamp;
    @OneToMany(mappedBy = "book",cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    private List<Chapter> chapters;


}
