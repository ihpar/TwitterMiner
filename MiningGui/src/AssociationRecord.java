import java.util.ArrayList;

public class AssociationRecord {
	private ArrayList<String> tagsLHS;
	private ArrayList<String> tagsRHS;
	private ArrayList<String> tagsALL;
	private int support;
	private double confidence;

	public AssociationRecord() {
		tagsLHS = new ArrayList<>();
		tagsRHS = new ArrayList<>();
		tagsALL = new ArrayList<>();
		support = 0;
		confidence = 0;
	}

	public String toString(int recordCount) {
		String ret = String.join(", ", this.tagsLHS) + " ⇒ " + String.join(", ", this.tagsRHS);
		double percentage = ((double) this.support / recordCount) * 100;
		ret += "   #SUP: " + this.support + " (" + String.format("%.2f", percentage) + "%)";
		percentage = this.confidence * 100;
		ret += "   #CONF: " + String.format("%.2f", percentage) + "%";
		return ret;
	}

	public ArrayList<String> getAllTagsLHS() {
		return tagsLHS;
	}

	public ArrayList<String> getAllTagsRHS() {
		return tagsRHS;
	}

	public ArrayList<String> getAllTags() {
		return tagsALL;
	}

	public void setSupport(int supp) {
		support = supp;
	}

	public int getSupport() {
		return support;
	}

	public void setConfidence(double conf) {
		confidence = conf;
	}

	public double getConfidence() {
		return confidence;
	}

	public void pushTagLHS(String tag) {
		tagsLHS.add(tag);
		tagsALL.add(tag);
	}

	public void pushTagRHS(String tag) {
		tagsRHS.add(tag);
		tagsALL.add(tag);
	}

}
