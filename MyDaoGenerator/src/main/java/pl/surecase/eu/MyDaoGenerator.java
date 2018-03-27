package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    private static Entity student;
    private static Entity attendance;
    private static Entity scan;

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(3, "greendao");

        /* --------------------- TABLES -------------------- */
        //Student
        student = schema.addEntity("Student");
        addStudent(student);
        //Attendance
        attendance = schema.addEntity("Attendance");
        addAttendance(attendance);
        //Scan
        scan = schema.addEntity("Scan");
        addScan(scan);

        new DaoGenerator().generateAll(schema, args[0]);
    }

    public static void addStudent(Entity student){
        //Attributes
        student.addIdProperty().autoincrement().primaryKey();;
        student.addStringProperty("netID");
        student.addStringProperty("name");
    }

    public static void addAttendance(Entity attendance){
        //Attributes
        attendance.addIdProperty().autoincrement().primaryKey();;
        Property student_id = attendance.addLongProperty("studentID").getProperty();
        attendance.addIntProperty("week");
        attendance.addStringProperty("scanDay");
        attendance.addLongProperty("scanTime");

        //Relation
        attendance.addToOne(student, student_id);
    }

    public static void addScan(Entity scan){
        scan.addIdProperty().autoincrement().primaryKey();
        scan.addStringProperty("NetID");
        scan.addIntProperty("Attendance");
        scan.addStringProperty("ScanTime");
        scan.addStringProperty("ScanDate");
        scan.addIntProperty("Week");
    }

}
