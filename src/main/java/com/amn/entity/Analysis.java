package com.amn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Analysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String type;
    private Date Date;
    private String filelink ;

    // Correct relationship mapping
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medical_record_id")  // Explicit column name
    private MedicalRecord medicalRecord;

}
