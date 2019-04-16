package uef.proj.dataparserUI;

/**
 *
 * @author Ilamal
 */
public class HeaderInfo {
    String heading;
    String alias;
    boolean normal;
    boolean avg;   
    
    public HeaderInfo() {
    }

    public HeaderInfo(String heading, String alias, boolean normal, boolean avg) {
        this.heading = heading;
        this.alias = alias;
        this.normal = normal;
        this.avg = avg;
    }    

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getAlias() {
        return alias;
    } 

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isNormal() {
        return normal;
    }

    public void setNormal(boolean normal) {
        this.normal = normal;
    }

    public boolean isAvg() {
        return avg;
    }

    public void setAvg(boolean avg) {
        this.avg = avg;
    }
    
    
}
