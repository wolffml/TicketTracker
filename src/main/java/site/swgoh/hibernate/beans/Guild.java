package site.swgoh.hibernate.beans;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Session;
import org.hibernate.query.Query;


@Entity
@Table(name="GUILDS")
//@Embeddable
//@Access(AccessType.PROPERTY)
public class Guild {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "GUILD_NAME", nullable = false, length=50)
	private String guildname;
	
	public Guild(){}
	
	public Guild(Long id, String guildname) {
		super();
		this.id = id;
		this.guildname = guildname;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getGuildname() {
		return guildname;
	}
	public void setGuildname(String guildname) {
		this.guildname = guildname;
	}
	
	public static Guild findGuild(Session session, String guildname){
		String hql = "FROM Guild G WHERE G.guildname = :guildname";
    	Query query = session.createQuery(hql)
    						.setParameter("guildname",guildname);
    	List results = query.list();
    	
    	if (results.isEmpty()){
    		return null;
    	}else {
    		return (Guild)results.get(0);
    	}
	}
	
}
