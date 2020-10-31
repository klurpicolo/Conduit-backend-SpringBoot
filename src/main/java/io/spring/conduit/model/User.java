package io.spring.conduit.model;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is user principal
 */

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="USER")
public class User {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    private String email;
    private String username;
    private String password;
    private String bio;
    private String image;

    public void update(String email, String username, String password, String bio, String image){
        if(!email.equals("")){
            this.email = email;
        }
        if(!username.equals("")){
            this.username = username;
        }
        if(!password.equals("")){
            this.password = password;
        }
        if(!bio.equals("")){
            this.bio = bio;
        }
        if(!image.equals("")){
            this.image = image;
        }
    }
}
