package dal.rootdal;
public class RootOfTokensTO{
	
	int tokenId;
	
	
	public int getTokenId() {
		return tokenId;
	}
	public void setTokenId(int tokenId) {
		this.tokenId = tokenId;
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
	public RootOfTokensTO( int tokenId, int rootId, int id) {
		super();
	
		this.tokenId = tokenId;
		this.rootId = rootId;
		this.id = id;
	}
	int rootId;
	int id;
}
