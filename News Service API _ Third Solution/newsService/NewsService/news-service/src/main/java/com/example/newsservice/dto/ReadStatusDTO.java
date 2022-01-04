package com.example.newsservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReadStatusDTO {
    private Long id;
    private Long accountID;
    private Long newsID;
    private LocalDateTime readDate;
}
