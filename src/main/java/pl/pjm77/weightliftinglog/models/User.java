package pl.pjm77.weightliftinglog.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
 * emailReal - boolean indicating that this is a real email and user wants to be sent a
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

    @NotBlank(message = "This field is mandatory!")
    private String name;

    private String password;

    @Transient
    transient private String confirmPassword;

    @Column(unique=true)
    @NotBlank(message = "This field is mandatory!")
    @Email(message = "Please enter a valid email!")
    private String email;

    private boolean emailReal;

    private String firstName;

    private String lastName;

    private Integer age;

    private Boolean gender;

    @NotBlank
    private String role;

    @OneToMany(mappedBy = "user")
    private List<WorkoutSerialized> workouts = new ArrayList<>();

    private boolean enabled = true;

    public User() {
    }

    public User(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.password = user.getPassword();
        this.email = user.getEmail();
        this.emailReal = user.isEmailReal();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.age = user.getAge();
        this.gender = user.getGender();
        this.role = user.getRole();
        this.workouts = user.getWorkouts();
        this.enabled = user.isEnabled();
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

    public boolean isEmailReal() {
        return emailReal;
    }

    public void setEmailReal(boolean emailReal) {
        this.emailReal = emailReal;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", confirmPassword='" + confirmPassword + '\'' +
                ", email='" + email + '\'' +
                ", emailReal=" + emailReal +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", gender=" + gender +
                ", role='" + role + '\'' +
//                ", workouts=" + workouts +
                ", enabled=" + enabled +
                '}';
    }
}