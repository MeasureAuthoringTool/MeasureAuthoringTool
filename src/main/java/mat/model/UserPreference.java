package mat.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "USER_PREFERENCE", uniqueConstraints = @UniqueConstraint(columnNames = "ID"))
public class UserPreference {
	
	private int id;
	
	private User user;
	
	private boolean freeTextEditorEnabled;
	
	
	public UserPreference() {
		
	}
	
	
	@Id
    @GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID", unique = true, nullable = false)
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Column(name = "FREE_TEXT_EDITOR_ENABLED")
	public boolean isFreeTextEditorEnabled() {
		return freeTextEditorEnabled;
	}

	public void setFreeTextEditorEnabled(boolean freeTextEditorEnabled) {
		this.freeTextEditorEnabled = freeTextEditorEnabled;
	}
}
