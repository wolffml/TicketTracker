package site.swgoh.TicketTracker.beans;

public class ResultRecord {
	private int line;
	private String stringData;
	
	
	
	public ResultRecord(int line, String stringData) {
		super();
		this.line = line;
		this.stringData = stringData;
	}
	public int getLine() {
		return line;
	}
	public void setLine(int line) {
		this.line = line;
	}
	public String getStringData() {
		return stringData;
	}
	public void setStringData(String stringData) {
		this.stringData = stringData;
	}
	
	@Override
	public String toString(){
		return line + ": " + stringData;
	}
	
}
