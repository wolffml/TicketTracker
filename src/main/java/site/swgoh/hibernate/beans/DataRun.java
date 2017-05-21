package site.swgoh.hibernate.beans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.Session;
import org.hibernate.query.Query;

@Entity
@Table(name="DATA_RUN")
public class DataRun {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "RUN_NAME", nullable = false, length=50)
	private String runName;
	
	public DataRun() {
	}

	public DataRun(Long id, String runName) {
		super();
		this.id = id;
		this.runName = runName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRunName() {
		return runName;
	}

	public void setRunName(String runName) {
		this.runName = runName;
	}
	
	public static DataRun getNewDataRun(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-kk-mm-ssS");
		String runName = sdf.format(new Date());
		
		DataRun dr = new DataRun();
		dr.setRunName(runName);
		return dr;
	}
	
	public static DataRun findDataRun(Session session, long id){
		String hql = "FROM DataRun dr WHERE dr.id = :id";
    	Query query = session.createQuery(hql)
    						.setParameter("id",id);
    	List results = query.list();
    	
    	if (results.isEmpty()){
    		return null;
    	}else {
    		return (DataRun)results.get(0);
    	}
	}
	
	
}
