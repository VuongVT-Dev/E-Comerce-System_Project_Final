package vn.edu.fpt.comic.Entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    @Column(name = "title", length = 100)
    private String title;

    @Column(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date_publication;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "image", length = 255)
    private String image;

    @Column(name = "price")
    private Double price;

    @Column(name = "weight_kg", precision = 10, scale = 2)
    private Double weight_kg;

    @Column(name = "number_page")
    private Integer number_page;

    @Column(name = "number_sold")
    private Integer number_sold;

    @Column(name = "number_stock")
    private Integer number_in_stock;

    @Column(name = "size", length = 50)
    private String size;

    @Column(name = "volume_number")
    private Integer volumeNumber;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date created_at;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated_at;
    @Transient
    private MultipartFile fileData;

    @Transient
    private boolean newBook;
}