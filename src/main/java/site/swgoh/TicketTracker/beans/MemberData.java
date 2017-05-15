package site.swgoh.TicketTracker.beans;

import java.util.Date;

public class MemberData {
	private String tickets;
	private String memberName;
	private String sourceFile;
	private Date date;
	
	
	
	public MemberData(String memberName, String tickets, String sourceFile) {
		super();
		this.tickets = tickets;
		this.memberName = memberName;
		this.sourceFile = sourceFile;
		date = new Date();
	}
	
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	
	@Override
	public String toString(){
		return memberName + " : " + tickets;
	}

	public String getTickets() {
		return tickets;
	}

	public void setTickets(String tickets) {
		this.tickets = tickets;
	}

	public String getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
}
