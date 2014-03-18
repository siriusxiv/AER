package models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.avaje.ebean.Expr;

import play.db.ebean.Model;

/**
 * Source des données :
 * http://sql.sh/1879-base-donnees-departements-francais
 * @author malik
 *
 */
@SuppressWarnings("serial")
@Entity
public class Departement extends Model {
	@Id
	@Column(columnDefinition="INT(11)")
	public Integer departement_id;
	@Column(columnDefinition="VARCHAR(3)")
	public String departement_code;
	public String departement_nom;
	public String departement_nom_uppercase;
	public String departement_slug;
	@Column(columnDefinition="VARCHAR(20)")
	public String departement_nom_soundex;
	
	public static Finder<Integer,Departement> find = new Finder<Integer,Departement>(Integer.class, Departement.class);

	public static List<Departement> findDepartementsAER(){
		return find.where().or(
					Expr.eq("departement_code","44"),
					Expr.or(
						Expr.eq("departement_code","85"),
						Expr.or(
							Expr.eq("departement_code","56"),
							Expr.or(
								Expr.eq("departement_code","35"),
								Expr.or(
									Expr.eq("departement_code","79"),
									Expr.eq("departement_code","17")
								)
							)
						)
					)
				).findList();
	}
	
	@Override
	public String toString(){
		return departement_code+" - "+departement_nom;
	}
}
