package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "com.lowbottgames.agecalculator");
        Entity p = schema.addEntity("PersonModel");
        p.addIdProperty();
        p.addStringProperty("name");
        p.addIntProperty("day");
        p.addIntProperty("month");
        p.addIntProperty("year");
        p.addIntProperty("hour");
        p.addIntProperty("minute");
        new DaoGenerator().generateAll(schema, args[0]);
    }
}
