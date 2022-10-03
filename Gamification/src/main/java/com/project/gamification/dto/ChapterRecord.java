package com.project.gamification.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ChapterRecord {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private LocalDateTime datetimestamp;
    @Column
    private int wordcount;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "chapter_id")
    @JsonIgnore
    private Chapter chapter;


}
