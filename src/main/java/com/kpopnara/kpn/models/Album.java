/*
 * package com.kpopnara.kpn.models;
 * 
 * import java.util.UUID;
 * import java.util.Set;
 * 
 * import jakarta.persistence.*;
 * import lombok.Data;
 * import lombok.NoArgsConstructor;
 * import lombok.NonNull;
 * import lombok.RequiredArgsConstructor;
 * 
 * @Data
 * 
 * @NoArgsConstructor
 * 
 * @RequiredArgsConstructor
 * 
 * @Entity
 * 
 * @Table(name = "albums")
 * public class Album {
 * 
 * @Id
 * 
 * @GeneratedValue
 * private UUID id;
 * 
 * @OneToMany
 * private Set<UUID> artist;
 * private String discography;
 * 
 * @NonNull
 * private String name;
 * private String format;
 * private String version;
 * private String color;
 * private String extras;
 * private String released;
 * private double price;
 * 
 * @OneToMany
 * Set<UUID> stock;
 * }
 */