package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    private static Entity scan;

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(3, "greendao");

        /* --------------------- TABLES -------------------- */
        //Scan
        scan = schema.addEntity("Scan");
        addScan(scan);

        new DaoGenerator().generateAll(schema, args[0]);
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
