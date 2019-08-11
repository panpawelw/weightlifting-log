package pl.pjm77.weightliftinglog.models;

import pl.pjm77.weightliftinglog.security.constraint.ValidPassword;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * User entity represents a user who logs his workouts.
 *
 * Mandatory fields are:
 * id - database Id, equals 0 until user has been persisted in database
 * name - username
 * password - user password
 * confirmPassword - password confirmation, not stored in database, needed only when registering
 *                   a new user or modifying user data
 * email - user email, has to be unique
 * realEmail - boolean indicating that this is a real email and user wants to be sent a
 *             registration confirmation email
 * role - user's Spring Security role - either USER or ADMIN
 *
 * Non - mandatory fields:
 * firstName - user's first name
 * lastName - user's last name
 * age - user's age
 * gender - user's gender, can remain null
 */
@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Please enter your user name!")
    private String name;

    @NotBlank(message = "Please enter your password!")
    private String password;

    @Transient
    @NotBlank(message = "Please confirm your password!")
    private String confirmPassword;

    @Column(unique=true)
    @Email(message = "Please enter a valid email!")
    private String email;

    private boolean realEmail;

    private String firstName;

    private String lastName;

    private Integer age;

    private Boolean gender;

    @NotBlank
    private String role;

    @OneToMany(mappedBy = "user")
    private List<WorkoutSerialized> workouts = new ArrayList<>();

    public User() {
    }

    public User(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.realEmail = user.isRealEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.age = user.getAge();
        this.gender = user.getGender();
        this.role = user.getRole();
        this.workouts = user.getWorkouts();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public boolean isRealEmail() {
        return realEmail;
    }

    public void setRealEmail(boolean realEmail) {
        this.realEmail = realEmail;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<WorkoutSerialized> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(List<WorkoutSerialized> workouts) {
        this.workouts = workouts;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", email='" + email + '\'' +
                ", realEmail=" + realEmail +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", role='" + role + '\'' +
                ", workouts=" + workouts +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return isRealEmail() == user.isRealEmail() &&
                Objects.equals(getId(), user.getId()) &&
                Objects.equals(getName(), user.getName()) &&
                Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getConfirmPassword(), user.getConfirmPassword()) &&
                Objects.equals(getEmail(), user.getEmail()) &&
                Objects.equals(getFirstName(), user.getFirstName()) &&
                Objects.equals(getLastName(), user.getLastName()) &&
                Objects.equals(getAge(), user.getAge()) &&
                Objects.equals(getGender(), user.getGender()) &&
                Objects.equals(getRole(), user.getRole()) &&
                Objects.equals(getWorkouts(), user.getWorkouts());
    }

    @Override
    public int hashCode() {
        return Objects
                .hash(getId(), getName(), getPassword(), getConfirmPassword(), getEmail(),
                        isRealEmail(), getFirstName(), getLastName(), getAge(), getGender(), getRole(), getWorkouts());
    }
}