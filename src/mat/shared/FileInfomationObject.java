package mat.shared;

import org.apache.http.entity.ContentType;

public class FileInfomationObject {
	private byte[] fileContents;
	private ContentType fileType;
	private String fileName;
	
	public FileInfomationObject() {
		
	}
	
	public FileInfomationObject(byte[] fileContents, ContentType fileType, String fileName) {
		this.fileContents = fileContents;
		this.fileType = fileType;
		this.fileName = fileName;
	}
	
	public byte[] getFileContents() {
		return fileContents;
	}
	public void setFileContents(byte[] fileContents) {
		this.fileContents = fileContents;
	}
	public ContentType getFileType() {
		return fileType;
	}
	public void setFileType(ContentType fileType) {
		this.fileType = fileType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	
}
