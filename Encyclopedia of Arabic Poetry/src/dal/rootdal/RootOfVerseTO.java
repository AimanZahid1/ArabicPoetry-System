package dal.rootdal;

public class RootOfVerseTO {

	int verseId;
	int rootId;
	
	
	public int getVerseId() {
		return verseId;
	}
	public void setVerseId(int verseId) {
		this.verseId = verseId;
	}
	public int getRootId() {
		return rootId;
	}
	public void setRootId(int rootId) {
		this.rootId = rootId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
	public RootOfVerseTO( int verseId, int rootId, int id, String status) {
		super();

		this.verseId = verseId;
		this.rootId = rootId;
		this.id = id;
		Status = status;
	}
	int id;
	String Status;
}
