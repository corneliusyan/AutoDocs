public class Identifier implements Comparable<Identifier> {
	private int digit;
	private String siteId;

	public Identifier(int digit, String siteId) {
		this.digit = digit;
		this.siteId = siteId;
	}

	public int getDigit() {
		return this.digit;
	}

	public String getSiteId() {
		return this.siteId;
	}

	public void setDigit(int digit) {
		this.digit = digit;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public int compareTo(Identifier other) {
		if (this.digit < other.digit) {
			return -1;
		} else if (this.digit > other.digit) {
			return 1;
		} else {
			return this.siteId.compareTo(other.siteId);
		}
	}
}