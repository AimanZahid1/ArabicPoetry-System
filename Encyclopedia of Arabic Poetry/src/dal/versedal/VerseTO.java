package dal.versedal;

public class VerseTO {
	private int poemId;
	private String firstVerse;
	private String secondVerse;
	public int getPoemId() {
		return poemId;
	}
	public void setPoemId(int poemId) {
		this.poemId = poemId;
	}
	public void setFirstVerse(String firstVerse) {
		this.firstVerse = firstVerse;
	}
	public VerseTO(int poemId, String firstVerse, String secondVerse) {
		this.poemId = poemId;
		this.firstVerse = firstVerse;
		this.secondVerse = secondVerse;
	}
	public void setSecondVerse(String secondVerse) {
		this.secondVerse = secondVerse;
	}
	public String getFirstVerse() {
		return firstVerse;
	}
	public String getSecondVerse() {
		return secondVerse;
	}

}
