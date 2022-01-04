package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewsDTO {

    private Long id;
    private String title;
    private String text;
    private LocalDateTime creationDate;
    private LocalDate validFrom;
    private LocalDate validTo;
    private Boolean roleRestriction;
    private Long userId;

}
