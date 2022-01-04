package com.example.newsservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

import static com.example.newsservice.constants.ColumnName.*;
import static com.example.newsservice.constants.TableName.READ_STATUS;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = READ_STATUS)
public class ReadStatus {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = ACCOUNT_ID)
    private Long accountID;

    @Column(name = NEWS_ID)
    private Long newsID;


    @Column(name = READ_DATE, nullable = true)
    private LocalDateTime readDate;
}
