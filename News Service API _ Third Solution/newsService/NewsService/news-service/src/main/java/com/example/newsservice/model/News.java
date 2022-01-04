package com.example.newsservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.example.newsservice.constants.ColumnName.*;
import static com.example.newsservice.constants.TableName.NEWS;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = NEWS)
public class News {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = TITLE )
    private String title;

    @Column(name = TEXT )
    private String text;

    @Column(name = PICTURE_ID, nullable = true)
    private Long pictureId;

    @Column(name = USER_ID )
    private Long userId;

    @Column(name = CREATION_DATE )
    private LocalDateTime creationDate;

    @Column(name = VALID_FROM)
    private LocalDate validFrom;

    @Column(name = VALID_TO)
    private LocalDate validTo;

    @Column(name = ROLE_RESTRICTION)
    private Boolean roleRestriction;

}
