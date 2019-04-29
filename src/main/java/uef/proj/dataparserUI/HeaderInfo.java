package uef.proj.dataparserUI;

/**
 * @author UEF Projektityö 2019 - Tony Heikkilä, Ilari Malinen, Mikko Nygård, Toni Takkinen
 * @version 1.0
 */
public class HeaderInfo {

    /**
     *
     */
    String heading;

    /**
     *
     */
    String alias;

    /**
     *
     */
    boolean normal;

    /**
     *
     */
    boolean avg;   
    
    /**
     *
     */
    public HeaderInfo() {
    }

    /**
     *
     * @param heading the string value of heading
     * @param alias the string value of alias heading, which can be customized by user in tableview
     * @param normal
     * @param avg
     */
    public HeaderInfo(String heading, String alias, boolean normal, boolean avg) {
        this.heading = heading;
        this.alias = alias;
        this.normal = normal;
        this.avg = avg;
    }    

    /**
     *
     * @return
     */
    public String getHeading() {
        return heading;
    }

    /**
     *
     * @param heading
     */
    public void setHeading(String heading) {
        this.heading = heading;
    }

    /**
     *
     * @return
     */
    public String getAlias() {
        return alias;
    } 

    /**
     *
     * @param alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     *
     * @return
     */
    public boolean isNormal() {
        return normal;
    }

    /**
     *
     * @param normal
     */
    public void setNormal(boolean normal) {
        this.normal = normal;
    }

    /**
     *
     * @return
     */
    public boolean isAvg() {
        return avg;
    }

    /**
     *
     * @param avg
     */
    public void setAvg(boolean avg) {
        this.avg = avg;
    }
    
    
}
