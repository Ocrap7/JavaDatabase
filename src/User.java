@Table
public class User {

    @PrimaryColumn
    public int id;

    @Column
    public String name;

    @Column
    public int age;

    public String toString() {
        return String.format("User '%s' has id '%d' and is %d years old.", name, id, age);
    }
}
