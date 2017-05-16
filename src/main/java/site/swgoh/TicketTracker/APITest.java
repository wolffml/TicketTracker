package site.swgoh.TicketTracker;

import java.sql.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import site.swgoh.hibernate.beans.Guild;
import site.swgoh.hibernate.beans.LifetimeTickets;

public class APITest {

	//Set up the Logger and ResourceBundle
	private static Logger logger = Logger.getLogger(App.class);
	private static final ResourceBundle bundle = ResourceBundle.getBundle("app");
	
	
	public static void main(String[] args) {
		
		SessionFactory sf = new Configuration().configure().buildSessionFactory();
    	Session session = sf.openSession();
    	java.util.List guilds = session.createQuery("from Guild").list();
    	
    	//for (Guild guild: guilds){
    	for (int i=0; i < guilds.size(); i++){
    		Guild guild = (Guild) guilds.get(i);
    		logger.info("Guild: " + guild.getGuildname());
    	}
    	
    	String hql = "FROM Guild G WHERE G.guildname = :guildname";
    	Query query = session.createQuery(hql)
    						.setParameter("guildname","Event HorizOn");
    	query.setParameter("guildname", "Event Horizon");//"Event Horizon");
    	List results = query.list();
    	
    	logger.info("Results: " + results.size());
    	if (results.size()>0) {
    		Guild g = (Guild)results.get(0);
    		logger.info("Guild 0:" + g.getGuildname());
    	} else {
    		logger.info("No Matching Guilds");
    	}
    	
    	
    	//session.beginTransaction();
    	//LifetimeTickets lt = new LifetimeTickets("Matt", 600, new java.util.Date().getTime());
    	
    	
    	
    	
		session.close();
		sf.close();
    	logger.debug("Execution Complete");
	}

}
