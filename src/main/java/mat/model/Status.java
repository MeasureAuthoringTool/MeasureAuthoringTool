package mat.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "STATUS")
public class Status implements java.io.Serializable {

    private static final long serialVersionUID = 8333823202893417670L;

    public static final String STATUS_ACTIVE = "1";
    public static final String STATUS_TERMINATED = "2";

    private String statusId;
    private String description;
    private Set<User> users = new HashSet<>(0);

    public Status() {
    }

    public Status(String statusId, String description) {
        this.statusId = statusId;
        this.description = description;
    }

    public Status(String statusId, String description, Set<User> users) {
        this.statusId = statusId;
        this.description = description;
        this.users = users;
    }

    @Id
    @Column(name = "STATUS_ID", unique = true, nullable = false, length = 32)
    public String getStatusId() {
        return this.statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
    }

    @Column(name = "DESCRIPTION", nullable = false, length = 50)
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "status")
    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

}
