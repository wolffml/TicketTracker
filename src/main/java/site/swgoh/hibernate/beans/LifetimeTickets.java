package site.swgoh.hibernate.beans;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.Session;
import org.hibernate.query.Query;

@Entity
@Table(name="LIFETIME_TICKETS")
public class LifetimeTickets {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "MEMBER_NAME", nullable = false, length=50)
	private String memberName;
	
	@ManyToOne
    @JoinColumn(name = "guild_id")
	private Guild guild;
	
	
	@Column (name="TICKETS")
	private Long tickets;
	
	@Column (name="M_DATE")
	private Timestamp mDate;
	
	@Column (name="SOURCE_FILE")
	private String sourceFile;
	
	
	public LifetimeTickets() {
	}
	public LifetimeTickets(String memberName, Long tickets, Guild g, long l) {
		this.memberName = memberName;
		this.guild = g;
		this.tickets = tickets;
		this.mDate = new Timestamp(System.currentTimeMillis());
	}
	
	
	
	public Guild getGuild() {
		return guild;
	}
	public void setGuild(Guild guild) {
		this.guild = guild;
	}
	public Long getTickets() {
		return tickets;
	}
	public void setTickets(Long tickets) {
		this.tickets = tickets;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMemberName() {
		return memberName;
	}
	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
	
	public String getSourceFile() {
		return sourceFile;
	}
	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}
	public Timestamp getmDate() {
		return mDate;
	}
	public void setmDate(Timestamp mDate) {
		this.mDate = mDate;
	}
	public static LifetimeTickets getLT(String memberName, Session session) {
		String hql = "FROM LifetimeTickets t WHERE t.memberName = :membername ORDER BY id";
    	Query query = session.createQuery(hql)
    						.setParameter("membername",memberName);
    	List results = query.list();
    	
    	if (results.isEmpty()){
    		return null;
    	}else {
    		return (LifetimeTickets)results.get(results.size()-1);
    	}
		
	}
	
	
}
