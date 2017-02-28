import java.util.ArrayList;

public class AprioryRecord {
	private ArrayList<String> tags;
	private int support;

	public AprioryRecord() {
		tags = new ArrayList<>();
		support = 0;
	}

	public void pushTag(String tag) {
		tags.add(tag);
	}

	public void setSupport(int support) {
		this.support = support;
	}

	public int getSupport() {
		return this.support;
	}

	public ArrayList<String> getTags() {
		return this.tags;
	}

	public String toString(int recordCount) {
		String ret = String.join(", ", this.tags);
		double percentage = ((double) this.support / recordCount) * 100;
		ret += "   #SUP: " + this.support + " (" + String.format("%.2f", percentage) + "%)";
		return ret;
	}
}
