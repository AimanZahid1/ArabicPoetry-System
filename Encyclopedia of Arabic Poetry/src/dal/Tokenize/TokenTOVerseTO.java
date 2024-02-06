package dal.Tokenize;

public class TokenTOVerseTO {
int tokenid;
int verseid;
public TokenTOVerseTO(int tokenid, int verseid) {
	super();
	this.tokenid = tokenid;
	this.verseid = verseid;
}
public int getTokenid() {
	return tokenid;
}
public void setTokenid(int tokenid) {
	this.tokenid = tokenid;
}
public int getVerseid() {
	return verseid;
}
public void setVerseid(int verseid) {
	this.verseid = verseid;
}


}
