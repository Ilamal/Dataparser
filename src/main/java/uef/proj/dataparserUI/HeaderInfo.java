package uef.proj.dataparserUI;

import java.io.Serializable;

/**
 * @author UEF Projektityö 2019 - Tony Heikkilä, Ilari Malinen, Mikko Nygård,
 * Toni Takkinen
 * @version 1.0
 */
public class HeaderInfo implements Serializable {

    String heading;

    String alias;

    boolean normal;

    boolean avg;

    /**
     * Helps to save data from user files.
     */
    public HeaderInfo() {
    }

    /**
     *
     * @param heading String value of heading
     * @param alias String value of alias heading, which can be customized by
     * user in TableView
     * @param normal Boolean value of tableView "Default"-CheckBox
     * @param avg Boolean value of tableView "Average"-CheckBox
     */
    public HeaderInfo(String heading, String alias, boolean normal, boolean avg) {
        this.heading = heading;
        this.alias = alias;
        this.normal = normal;
        this.avg = avg;
    }

    /**
     *
     * @return File heading
     */
    public String getHeading() {
        return heading;
    }

    /**
     *
     * @param heading File heading
     */
    public void setHeading(String heading) {
        this.heading = heading;
    }

    /**
     *
     * @return File heading based on user input
     */
    public String getAlias() {
        return alias;
    }

    /**
     *
     * @param alias File heading based on user input
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     *
     * @return Boolean CheckBox value for default input data
     */
    public boolean isNormal() {
        return normal;
    }

    /**
     *
     * @param normal Boolean CheckBox value for default input data
     */
    public void setNormal(boolean normal) {
        this.normal = normal;
    }

    /**
     *
     * @return Boolean CheckBox value for average input data
     */
    public boolean isAvg() {
        return avg;
    }

    /**
     *
     * @param avg Boolean CheckBox value for average input data
     */
    public void setAvg(boolean avg) {
        this.avg = avg;
    }

}
