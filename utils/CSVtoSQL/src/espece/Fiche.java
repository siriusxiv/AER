package espece;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Fiche {
	String line_origine;
	String fichier_nom;
	
	static int id_compteur = 0;
	int id = 0;
	String numero;
	String temoin1;
	String temoin2;
	String temoin3;
	String utm;
	String lieudit;
	String commune;
	Calendar date_min;
	Calendar date_max;
	String memo;

	public Fiche(String line, int compteur_line, String fichier) throws ArrayIndexOutOfBoundsException, StringIndexOutOfBoundsException{
		line_origine=fichier+": line"+compteur_line+". "+line;
		fichier_nom=fichier;
		
		id_compteur++;
		id=id_compteur;
		String[] col = line.split("µ");
		numero=col[0];
		temoin1=col[3];
		temoin2=col[4];
		temoin3=col[5];
		utm=col[6];
		date_min=stringToCalendar(col[9]);
		date_max=stringToCalendar(col[10]);
		if(col.length==12)
			memo=col[11];
		else
			memo="";

		if(temoin1.equals("et al.") || temoin2.equals("et al.") || temoin3.equals("et al.")
				|| temoin1.equals("et alii") || temoin2.equals("et alii") || temoin3.equals("et alii"))
			memo+=" ; et alii";
		
		String lieu = col[7];
		if(!lieu.isEmpty()){
			int lastComa = lieu.lastIndexOf(",");
			if(lastComa!=-1)
				lieudit=lieu.substring(0,lastComa);
			else
				lieudit="";
			try{
				if(lastComa==-1)
					commune=lieu.substring(0,lieu.lastIndexOf("(")-1);
				else
					commune=lieu.substring(lastComa+2,lieu.lastIndexOf("(")-1);
			}catch(StringIndexOutOfBoundsException e){
				commune="";
				memo+=" ; "+lieu;
			}
		}else{
			lieudit="";
			commune="";
		}
		if(commune.startsWith("[")){
			memo+=" ; Information sur la commune retrouvée par recoupement : "+commune;
		}
	}

	public Calendar stringToCalendar(String s){
		Calendar c =  Calendar.getInstance();
		SimpleDateFormat f = new SimpleDateFormat("M/d/yyyy H:mm:ss");
		try {
			c.setTime(f.parse(s));
		} catch (ParseException e) {
			if(fichier_nom.equals("FICHES_Zygaenidae.txt"))
				f = new SimpleDateFormat("d/M/yyyy H:mm");
			else
				f = new SimpleDateFormat("M/d/yyyy H:mm");
			try {
				c.setTime(f.parse(s));
			} catch (ParseException e1) {
				c=null;
			}
		}
		return c;
	}

	public String toSQL() throws SQLException, IOException {
		//INSERT INTO `fiche` (`fiche_id`,`fiche_lieudit`,`fiche_date_min`,`fiche_date`,
		//		`fiche_memo`,`fiche_utm_utm`,`fiche_commune_ville_id`,`fiche_date_soumission`
		return "("+id+",'"+lieudit.replace("'", "''")+"',"+getSQLDateMin()+","+getSQLDateMax()+",'"+
		memo.replace("'", "''")+"','"+utm+"',"+getCommune()+","+getSQLDateMax()+"),\n";
	}

	private Integer getCommune() throws SQLException, IOException {
		if(commune.equals(""))
			return null;
		else{
			String communeFormatee = commune;
			if(commune.startsWith("[")){
				try{
					communeFormatee=commune.substring(1,commune.lastIndexOf(']'));
				}catch(StringIndexOutOfBoundsException e){
					FicheReader.errorFile.write("Crochet ouvrant mais pas de crochet fermant:\n"+this.line_origine+"\n");
					e.printStackTrace();
				}
			}
			if(communeFormatee.startsWith("Les ") || communeFormatee.startsWith("les "))
				communeFormatee=communeFormatee.substring(4);
			else if(communeFormatee.startsWith("La ") || communeFormatee.startsWith("Le ") || communeFormatee.startsWith("la ") || communeFormatee.startsWith("le "))
				communeFormatee=communeFormatee.substring(3);
			else if(communeFormatee.startsWith("L'") || communeFormatee.startsWith("l'"))
				communeFormatee=communeFormatee.substring(2);
			switch(communeFormatee){
			case "Montfaucon":
				communeFormatee="Montfaucon-Montigné";break;
			case "Chapelle-Largeau":
				communeFormatee="Mauléon";break;
			case "Bonnoeuvre":
				communeFormatee="Bonnœuvre";break;
			case "Brétignolles":
				communeFormatee="Bretignolles-sur-Mer";break;
			case "Noirmoutier":
				communeFormatee="Noirmoutier-en-l'Île";break;
			case "Paimboeuf":
				communeFormatee="Paimbœuf";break;
			}
			communeFormatee = communeFormatee.replaceAll("'", "''").replaceAll("St-", "Saint-").replaceAll("Ste-", "Sainte-");
			int res = 0;
			Statement statement = FicheReader.connect.createStatement();
			String query = "SELECT ville_id FROM commune WHERE ville_nom_reel='"+communeFormatee+"';";
			ResultSet resultSet = statement.executeQuery(query);
			while(resultSet.next())
				res = resultSet.getInt("ville_id");
			if(res==0){
				query = "SELECT ville_id FROM commune WHERE ville_nom_reel='"+communeFormatee.replaceAll(" ", "-")+"';";
				resultSet = statement.executeQuery(query);
				while(resultSet.next())
					res = resultSet.getInt("ville_id");
			}
			if(res==0){
				query = "SELECT ville_id FROM commune WHERE ville_nom_reel='"+communeFormatee.replaceAll("-", " ")+"';";
				resultSet = statement.executeQuery(query);
				while(resultSet.next())
					res = resultSet.getInt("ville_id");
			}
			if(res==0){
				throw new SQLException("Commune introuvable !");
			}
			return res;
		}
	}

	public String getSQLDateMax() throws IOException{
		if(date_max==null){
			FicheReader.errorFile.write("Date max non spécifiée : "+"\n"+this.line_origine+"\n");
			return null;
		}else{
			SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
			return "'"+f.format(date_max.getTime())+"'";
		}
	}

	private String getSQLDateMin() {
		if(date_min!=null){
			SimpleDateFormat f = new SimpleDateFormat("yyyyMMdd");
			return "'"+f.format(date_min.getTime())+"'";
		}else
			return "null";
	}

}
