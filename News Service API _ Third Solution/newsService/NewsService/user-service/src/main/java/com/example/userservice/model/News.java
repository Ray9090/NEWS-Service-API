package com.example.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class News {

    private Long id;

    private String title;

    private String text;

    private Long userId;

    private Date creationDate;

    private LocalDate validFrom;

    private LocalDate validTo;

    private Boolean roleRestriction;


}
