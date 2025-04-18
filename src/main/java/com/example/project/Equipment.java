package com.example.project;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvCustomBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "equipment") // Имя таблицы в БД
@Getter
@Setter
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @CsvBindByName(column = "id")
    @CsvCustomBindByName(column = "id", converter = UuidConverter.class)
    private UUID id;

    @CreationTimestamp
    @Column(name = "created_on", updatable = false)
    @CsvCustomBindByName(column = "created_on", converter = LocalDateTimeConverter.class)
    private LocalDateTime created_on;

    @UpdateTimestamp
    @Column(name = "updated_on")
    @CsvCustomBindByName(column = "updated_on", converter = LocalDateTimeConverter.class)
    private LocalDateTime updated_on;

    @Column(name = "name", length = 255)
    @CsvBindByName(column = "name")
    private String name;

    @Column(name = "ci_code", length = 50)
    @CsvBindByName(column = "ci_code")
    private String ci_code;

    @Column(name = "short_name", length = 100)
    @CsvBindByName(column = "short_name")
    private String short_name;

    @Column(name = "full_name", length = 255)
    @CsvBindByName(column = "full_name")
    private String full_name;

    @Column(name = "description", columnDefinition = "TEXT")
    @CsvBindByName(column = "description")
    private String description;

    @Column(name = "notes", columnDefinition = "TEXT")
    @CsvBindByName(column = "notes")
    private String notes;

    @Column(name = "status", length = 50)
    @CsvBindByName(column = "status")
    private String status;

    @Column(name = "manufacturer", length = 100)
    @CsvBindByName(column = "manufacturer")
    private String manufacturer;

    @Column(name = "serial", length = 100)
    @CsvBindByName(column = "serial")
    private String serial;

    @Column(name = "model", length = 100)
    @CsvBindByName(column = "model")
    private String model;

    @Column(name = "location", length = 255)
    @CsvBindByName(column = "location")
    private String location;

    @Column(name = "mount", length = 255)
    @CsvBindByName(column = "mount")
    private String mount;

    @Column(name = "hostname", length = 100)
    @CsvBindByName(column = "hostname")
    private String hostname;

    @Column(name = "dns", length = 255)
    @CsvBindByName(column = "dns")
    private String dns;

    @Column(name = "ip", length = 50)
    @CsvBindByName(column = "ip")
    private String ip;

    @Column(name = "cpu_cores")
    @CsvBindByName(column = "cpu_cores")
    private Integer cpu_cores;

    @Column(name = "cpu_freq")
    @CsvBindByName(column = "cpu_freq")
    private Double cpu_freq;

    @Column(name = "ram")
    @CsvBindByName(column = "ram")
    private Integer ram;

    @Column(name = "total_volume")
    @CsvBindByName(column = "total_volume")
    private Double total_volume;

    @Column(name = "type", length = 50)
    @CsvBindByName(column = "type")
    private String type;

    @Column(name = "category", length = 50)
    @CsvBindByName(column = "category")
    private String category;

    @Column(name = "user_org", length = 100)
    @CsvBindByName(column = "user_org")
    private String user_org;

    @Column(name = "owner_org", length = 100)
    @CsvBindByName(column = "owner_org")
    private String owner_org;

    @Column(name = "code_mon", length = 50)
    @CsvBindByName(column = "code_mon")
    private String code_mon;
}