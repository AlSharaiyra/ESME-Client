package com.globitel.ESME.models;

import com.globitel.ESME.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SMS {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text_body", nullable = false)
    private String textBody;

//    @Column(name = "text_encoding", nullable = false)
//    private String textEncoding;

    @Column(name = "source_address", nullable = false)
    private String sourceAddress;

    @Column(name = "destination_address", nullable = false)
    private String destinationAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.PENDING;

//    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
}
