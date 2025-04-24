package com.amn.entity;


import com.amn.entity.enums.MedicationType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String size; // e.g. "500mg"
    private double price;

    @Enumerated(EnumType.STRING)
    private MedicationType medicationType;

    @ManyToOne
    @JoinColumn(name = "added_by_pharmacist_id")
    private Pharmacist addedBy;
    @ManyToOne
    private Prescription prescription;

// --- Getters ---

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public double getPrice() {
        return price;
    }

    public MedicationType getMedicationType() {
        return medicationType;
    }

    public Pharmacist getAddedBy() {
        return addedBy;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    // --- Setters (optional, but useful if building objects) ---

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setMedicationType(MedicationType medicationType) {
        this.medicationType = medicationType;
    }

    public void setAddedBy(Pharmacist addedBy) {
        this.addedBy = addedBy;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

}
