package com.skillbridge.skillbridge_portal.model;

import jakarta.persistence.*;
import lombok.*;
import com.skillbridge.skillbridge_portal.model.Batch;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Batch> batches;


}
