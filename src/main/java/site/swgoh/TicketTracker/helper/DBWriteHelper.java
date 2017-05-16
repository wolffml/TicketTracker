package site.swgoh.TicketTracker.helper;

import java.util.Date;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import site.swgoh.TicketTracker.beans.MemberData;
import site.swgoh.TicketTracker.lists.DailyTrackingList;
import site.swgoh.hibernate.beans.Guild;
import site.swgoh.hibernate.beans.LifetimeTickets;

public class DBWriteHelper {
	
	private static Logger logger = Logger.getLogger(DBWriteHelper.class);
	private static final ResourceBundle bundle = ResourceBundle.getBundle("app");
	
	private static final String GUILD_NAME = bundle.getString("db.guildname");
	
	public static void writeToDB(DailyTrackingList dailyList){
		
		Session session =HibernateUtil.getSessionFactory().openSession();
		
		
		Guild guild = Guild.findGuild(session, GUILD_NAME);
		
		//Build LifeTimeTickets beans
		for (MemberData md: dailyList){
			Transaction tx = session.beginTransaction();
			LifetimeTickets ticketDB = new LifetimeTickets();
			ticketDB.setGuild(guild);
			ticketDB.setmDate(new java.sql.Date(System.currentTimeMillis()));
			ticketDB.setMemberName(md.getMemberName());
			ticketDB.setSourceFile(md.getSourceFile());
			Long tickets = 0L;
			try {
				tickets = Long.parseLong(md.getTickets());
				
			} catch (NumberFormatException e){
				logger.error("Number format not convertable to Integer: " + md.getTickets());
				tickets = 0L;
			}
			ticketDB.setTickets(tickets);
			session.save(ticketDB);
			tx.commit();
		}
		
		session.close();
		HibernateUtil.shutdown();
	}
}
