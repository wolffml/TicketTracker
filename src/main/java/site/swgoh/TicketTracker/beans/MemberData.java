package site.swgoh.TicketTracker.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

import site.swgoh.TicketTracker.helper.FileHelper;

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
		date = FileHelper.getDateFromFileD(sourceFile);
	}
	
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	
	@Override
	public String toString(){
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY/MM/dd hh:mm:ss");
		return getMemberName() + "," + getTickets() + "," + sdf.format(getDate()) + "," + getSourceFile();
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
