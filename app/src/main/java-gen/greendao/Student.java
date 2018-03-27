package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table STUDENT.
 */
public class Student {

    private Long id;
    private String netID;
    private String name;

    public Student() {
    }

    public Student(Long id) {
        this.id = id;
    }

    public Student(Long id, String netID, String name) {
        this.id = id;
        this.netID = netID;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNetID() {
        return netID;
    }

    public void setNetID(String netID) {
        this.netID = netID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
