package model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement(name = "pending")
@Getter
@Setter
public class Pending {
    public static final String REMOVE = "remove";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String waiterMail;

    @XmlTransient
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private User ownerUser;

    public void setOwnerUser(User ownerUser) {
        this.ownerUser = ownerUser;
    }

}
