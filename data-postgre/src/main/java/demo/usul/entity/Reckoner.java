package demo.usul.entity;

import jakarta.persistence.*;

import java.util.UUID;

//@Entity
//@Table(name = "accounts")
public class Reckoner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "title")
    private String accountName;




}
