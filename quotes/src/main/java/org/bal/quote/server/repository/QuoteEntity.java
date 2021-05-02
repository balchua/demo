package org.bal.quote.server.repository;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "QUOTES")
@Getter
@Setter
public class QuoteEntity implements Serializable {
    private static final long serialVersionUID = -2343243243242432341L;

    @Id
    private int id;

    @Column(name = "QUOTE")
    private String quote;

    @Column(name = "NAME")
    private String name;


}
