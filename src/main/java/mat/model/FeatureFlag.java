package mat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "FEATURE_FLAGS")
public class FeatureFlag implements java.io.Serializable {
    /**
	 *
	 */
	private static final long serialVersionUID = -2614498951651483110L;

	private int id;

    private String flagName;

    private boolean flagOn;

    public FeatureFlag() {
    }

    public FeatureFlag(int id, String flagName, boolean flagOn) {
    	this.id = id;
    	this.flagName = flagName;
    	this.flagOn = flagOn;
    }

    @Id
    @Column(name = "ID", unique = true, nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "FLAG_NAME")
    public String getFlagName(){
        return flagName;
    }

    public void setFlagName(String flagName){
        this.flagName = flagName;
    }

    @Column(name = "FLAG_ON")
    public boolean isFlagOn() {
        return flagOn;
    }

    public void setFlagOn(boolean flagOn){
        this.flagOn = flagOn;
    }
}
