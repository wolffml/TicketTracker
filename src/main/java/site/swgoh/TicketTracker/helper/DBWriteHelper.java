package site.swgoh.TicketTracker.helper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.http.client.utils.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import site.swgoh.TicketTracker.beans.MemberData;
import site.swgoh.TicketTracker.lists.DailyTrackingList;
import site.swgoh.hibernate.beans.DataRun;
import site.swgoh.hibernate.beans.Guild;
import site.swgoh.hibernate.beans.LifetimeTickets;
import site.swgoh.hibernate.beans.TicketRun;

public class DBWriteHelper {
	
	private static Logger logger = Logger.getLogger(DBWriteHelper.class);
	private static final ResourceBundle bundle = ResourceBundle.getBundle("app");
	
	private static final String GUILD_NAME = bundle.getString("db.guildname");
	
	public static void writeToDB(DailyTrackingList dailyList){
		//We were writing to the LifetimeTickets Table, but now writing to the TicketRun Table
		Session session = HibernateUtil.getSessionFactory().openSession();
		
		//First get or create an instance of DataRun
		Transaction drTransaction = session.beginTransaction();//session.getTransaction();
		DataRun dr = DataRun.getNewDataRun();
		session.save(dr);
		drTransaction.commit();
		
		Guild guild = Guild.findGuild(session, GUILD_NAME);
		
		//Build LifeTimeTickets beans
		for (MemberData md: dailyList){
			Transaction tx = session.beginTransaction();
			TicketRun tr = new TicketRun();
			//LifetimeTickets ticketDB = new LifetimeTickets();
			tr.setGuild(guild);
			tr.setRun(dr);
			tr.setmDate(new Timestamp(System.currentTimeMillis()));
			tr.setMemberName(md.getMemberName());
			tr.setSourceFile(md.getSourceFile());
			Long tickets = 0L;
			try {
				tickets = Long.parseLong(md.getTickets());
				
			} catch (NumberFormatException e){
				logger.error("Number format not convertable to Integer: " + md.getTickets());
				tickets = 0L;
			}
			tr.setTickets(tickets);
			session.save(tr);
			tx.commit();
		}
		
		session.close();
		HibernateUtil.shutdown();
	}
	
	public static void update30K(String strBegin, String strNewDate){ //Timestamp tsArchtypeDate){
		//Select all of the records from a prior day and create new records for the subsequent day adding 600 to the dailyTickets
		Session session =HibernateUtil.getSessionFactory().openSession();
			
		
		
		String hql = "SELECT * FROM LIFETIME_TICKETS WHERE DATE_FORMAT(M_DATE, '%c/%d/%Y') = :strBegin";
		
		
		//String strBegin = "5/16/2017";
		
    	Query query = session.createNativeQuery(hql, TicketRun.class)
    						.setParameter("strBegin", strBegin)
    						; //.setParameter("strEnd", dtEnd);
    	List results = query.list();
    	
    	
    	//
    	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); //yyyy-MM-dd
		Date parsedDate;
		try {
			parsedDate = dateFormat.parse(strNewDate);
			Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
	    	
	    	
	    	for (int i=0; i < results.size(); i++){
	    		LifetimeTickets lt = (LifetimeTickets)results.get(i);
	    		logger.info(i + ": " + lt.getMemberName());
	    		
	    		Transaction tx = session.beginTransaction();
				LifetimeTickets newLT = new LifetimeTickets();
				newLT.setGuild(lt.getGuild());
				newLT.setmDate(timestamp);
				newLT.setMemberName(lt.getMemberName());
				newLT.setSourceFile("n/a");
				newLT.setTickets(lt.getTickets() + 600);
				session.save(newLT);
				tx.commit();
	    		
	    	}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		session.close();
		HibernateUtil.shutdown();
	}

	public static void showShort(long l) {
		Session session =HibernateUtil.getSessionFactory().openSession();
		//Get dataRun for i
		logger.info("Opened Hibernate Session.");
		DataRun dr = DataRun.findDataRun(session, l);
		logger.info("Found Datarun instance:" + dr.getRunName());
		
		//String hql = "from LifetimeTickets l where l.mDate between :strBegin and :strEnd";
		String hql = "FROM TicketRun t WHERE t.run = :run ORDER BY tickets DESC";
    	Query query = session.createQuery(hql)
    						.setParameter("run",dr);
    	List results = query.list();
		String report = "";
    	for (int i=0; i< results.size(); i++){
    		TicketRun tr = (TicketRun)results.get(i);
    		//Get Prior Day's Tickets
    		LifetimeTickets lt = LifetimeTickets.getLT(tr.getMemberName(), session);
    		long ticketsProduced = tr.getTickets() - lt.getTickets();
    		report += tr.getMemberName() + ":" + ticketsProduced + "\n";
    		logger.debug(tr.getMemberName() + ":" + ticketsProduced);
    		
    	}
    	System.out.println(report);
    	//
    	//SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); //yyyy-MM-dd
		//Date parsedDate;
		session.close();
		HibernateUtil.shutdown();
	}

	public static void commitRun(long l) {
		// TODO Auto-generated method stub
		Session session =HibernateUtil.getSessionFactory().openSession();
		//Get dataRun for i
		logger.info("Opened Hibernate Session.");
		DataRun dr = DataRun.findDataRun(session, l);
		logger.info("Found Datarun instance:" + dr.getRunName());
		
		//String hql = "from LifetimeTickets l where l.mDate between :strBegin and :strEnd";
		String hql = "FROM TicketRun t WHERE t.run = :run ORDER BY tickets DESC";
    	Query query = session.createQuery(hql)
    						.setParameter("run",dr);
    	List results = query.list();
		String report = "";
    	for (int i=0; i< results.size(); i++){
    		TicketRun tr = (TicketRun)results.get(i);
    		//Get Prior Day's Tickets
    		//Create new LifetimeTickets and save it.
    		Transaction tx = session.beginTransaction();
    		LifetimeTickets newLT = new LifetimeTickets();
			newLT.setGuild(tr.getGuild());
			newLT.setmDate(tr.getmDate());
			newLT.setMemberName(tr.getMemberName());
			newLT.setSourceFile(tr.getSourceFile());
			newLT.setTickets(tr.getTickets() + 600);
			session.save(newLT);
			tx.commit();
    		
    	}
    	System.out.println(report);
    	//
    	//SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); //yyyy-MM-dd
		//Date parsedDate;
		session.close();
		HibernateUtil.shutdown();
	}
	
}
