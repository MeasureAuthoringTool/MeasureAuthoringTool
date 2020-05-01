package mat.DTO;


import java.io.Serializable;

public class VSACCodeSystemDTO implements Serializable {

    private String oid;
    private String url;
    private String name;
    private String defaultVsacVersion;

    public VSACCodeSystemDTO() {
    }

    public VSACCodeSystemDTO(String oid, String url, String name, String defaultVsacVersion) {
        this.oid = oid;
        this.url = url;
        this.defaultVsacVersion = defaultVsacVersion;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultVsacVersion() {
        return defaultVsacVersion;
    }

    public void setDefaultVsacVersion(String defaultVsacVersion) {
        this.defaultVsacVersion = defaultVsacVersion;
    }

    @Override
    public String toString() {
        return "VSACCodeSystemDTO{" +
                "oid='" + oid + '\'' +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", defaultVsacVersion='" + defaultVsacVersion + '\'' +
                '}';
    }
}
