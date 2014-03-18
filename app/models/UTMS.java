package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@SuppressWarnings("serial")
@Entity
public class UTMS extends Model {
	@Id
	@Column(columnDefinition="VARCHAR(4)")
	public String utm;
	
	@Column(columnDefinition="VARCHAR(4)")
	public String maille20x20;
	
	@Column(columnDefinition="VARCHAR(4)")
	public String maille50x50;
	
	@Column(columnDefinition="VARCHAR(4)")
	public String maille100x100;
	
	public static Finder<String,UTMS> find = new Finder<String,UTMS>(String.class, UTMS.class);

	@Override
	public String toString(){
		return utm;
	}
}
