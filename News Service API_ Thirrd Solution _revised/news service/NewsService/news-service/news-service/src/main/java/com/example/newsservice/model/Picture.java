package com.example.newsservice.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


import static com.example.newsservice.constants.TableName.PICTURE;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = PICTURE)
public class Picture {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "type")
    private String type;

    @Column(name = "imageData", length = 1000000)
    private byte[] file;


}
