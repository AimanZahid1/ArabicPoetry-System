package dal.rootdal;

public class RootTO {
int rootid;
String root_word;
	
	public int getRootid() {
		return rootid;
	}
	public String getRoot_word() {
		return root_word;
	}
	public void setRootid(int rootid) {
		this.rootid = rootid;
	}
	public void setRoot_word(String root_word) {
		this.root_word = root_word;
	}
	public RootTO(int rootid,String root) {
		super();
		this.rootid = rootid;
		root_word=root;
	}

}
