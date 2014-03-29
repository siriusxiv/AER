package espece;

import java.security.SecureRandom;
import java.util.ArrayList;

public class Membre {
	String nom;
	String adresse;
	String complement;
	String commune;
	String cp;
	String pays;
	
	boolean temoin;
	boolean abonne;
	Integer journais;
	Integer moisnais;
	Integer annenais;
	Integer jourdece;
	Integer moisdece;
	Integer annedece;
	String biographie;

	public Membre(String line) throws ArrayIndexOutOfBoundsException{
		String[] col = line.split("µ");
		nom=col[0].replace("'", "''");
		adresse=col[1].replace("'", "''");
		complement=col[2].replace("'", "''");
		commune=col[3].replace("'", "''");
		cp=col[4].replace("'", "''");
		pays=col[5].replace("'", "''");
		temoin=col[7].equals("TRUE");
		abonne=col[8].equals("TRUE");
		try{
			journais=Integer.parseInt(col[9]);
		}catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
			journais=null;
		}
		try{
			moisnais=Integer.parseInt(col[10]);
		}catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
			moisnais=null;
		}
		try{
			annenais=Integer.parseInt(col[11]);
		}catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
			annenais=null;
		}
		try{
			jourdece=Integer.parseInt(col[12]);
		}catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
			journais=null;
		}
		try{
			moisdece=Integer.parseInt(col[13]);
		}catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
			moisnais=null;
		}
		try{
			annedece=Integer.parseInt(col[14]);
		}catch(NumberFormatException | ArrayIndexOutOfBoundsException e){
			annenais=null;
		}
		if(col.length==16)
			biographie=col[15].replace("'", "''");
		else
			biographie="";
	}
	//INSERT INTO `membres` (`membre_nom`,`membre_adresse`,`membre_adresse_complement`,`membre_code_postal`,`membre_ville`,`membre_pays`,`membre_confidentialite_confidentialite_id`,`membre_abonne`,`membre_temoin_actif`,`membre_journais`,`membre_moisnais`,`membre_annenais`,`membre_jourdece`,`membre_moisdece`,`membre_annedece`,`membre_biographie`,`membre_email`,`membre_sel`,`membre_droits_droits_id`,`membre_inscription_acceptee`) VALUES
	@Override
	public String toString(){
		String abonne1=abonne ? "1" : "0";
		String temoin1=temoin ? "1" : "0";
		return "('"+nom+"','"+adresse+"','"+complement+"','"+cp+"','"+commune+"','"+pays+"',3,"+abonne1+","+temoin1+","+journais+","+moisnais+","+annenais+","+jourdece+","+moisdece+","+annedece+",'"+biographie+"','','"+genererSel()+"',1,0),\n";
	}
	
	private final String elements="1234567890azertyuiopqsdfghjklmwxcvbnAZERTYUIOPQSDFGHJKLMWXCVBN<>,;:!?./§*%=+&'(-_)#{[|`^@]}";
	private final SecureRandom rnd = new SecureRandom();
	private final int size_sel = 32;
	
	private String genererSel() {
		StringBuilder sb = new StringBuilder(size_sel);
		for( int i = 0; i < size_sel; i++ ) 
		      sb.append(elements.charAt(rnd.nextInt(elements.length())));
		return sb.toString().replace("'", "''");
	}
	
	public boolean isNotIn(ArrayList<Membre> membres){
		for(Membre m : membres){
			if(m.nom.equals(this.nom)){
				return false;
			}
		}
		return true;
	}
}
