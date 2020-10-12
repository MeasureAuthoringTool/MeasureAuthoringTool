package mat.vsacmodel;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Container for holding Group information.
 *
 * **/
public class MatGroup  implements Serializable {
/**
* Group ID.
*
* **/
private String Id;
/**
* Group Source Organization.
*
* **/

private String sourceOrganization;
/**
* Group Display Name.
*
* **/

private String displayName;
/**
* Array List of Key Words.
*
* **/

private ArrayList<String> keywordList;
/**
 * Getter -ID.
 * @return ID.
 *
 * **/
public final String getId() {
return Id;
}
/**
 *Setter for ID.
 *@param Ids - Group ID.
 * **/
public final void setId(final String Ids) {
this.Id = Ids;
}
/**
 * Getter -sourceOrganization.
 * @return sourceOrganization.
 *
 * **/
public final String getSourceOrganization() {
return sourceOrganization;
}
/**
 *Setter for sourceOrganization.
 *@param sourceOrganizations - sourceOrganization.
 * **/
public final void setSourceOrganization(final String sourceOrganizations) {
this.sourceOrganization = sourceOrganizations;
}
/**
 * Getter -displayName.
 * @return displayName.
 *
 * **/
public final String getDisplayName() {
return displayName;
}
/**
 *Setter for displayName.
 *@param displayNames - displayName.
 * **/
public final void setDisplayName(final String displayNames) {
this.displayName = displayNames;
}
/**
 *Setter for keywordList.
 *@param keywordLists - keywordList.
 * **/
public final void setKeywordList(final ArrayList<String> keywordLists) {
this.keywordList = keywordLists;
}
/**
 * Getter -keywordList.
 * @return keywordList.
 *
 * **/
public final ArrayList<String> getKeywordList() {
return keywordList;
}
}
