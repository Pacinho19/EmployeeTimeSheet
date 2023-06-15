package pl.raportsa.timesheet.model.entity;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.nio.charset.Charset;
import java.util.*;

@Entity
@Table(name = "EMPLOYEES")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Employee implements UserDetails {


    @Id
    @GeneratedValue(generator = "GEN_EMPLOYEE_ID")
    @GenericGenerator(name = "GEN_EMPLOYEE_ID", strategy = "increment")
    private Integer id;

    private String firstName;
    private String lastName;

    private String login;

    @Setter
    private String password;

    @Transient
    private boolean isCredentialsNonExpired;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getName() {
        return this.getFirstName() + ' ' + this.getLastName();
    }

}
