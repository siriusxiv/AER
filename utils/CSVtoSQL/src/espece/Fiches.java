package espece;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.NamingException;

public class Fiches {
	String filename;
	ArrayList<Fiche> fiches = new ArrayList<Fiche>();

	public Fiches(String filename) throws IOException{
		this.filename=filename;
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(filename));
		int line_compteur = 1;
		br.readLine();
		String line;
		while((line=br.readLine())!=null){
			line_compteur++;
			try{
				Fiche f = new Fiche(line,line_compteur,filename);
				fiches.add(f);
			}catch(StringIndexOutOfBoundsException | ArrayIndexOutOfBoundsException e){
				FicheReader.errorFile.write(filename+": "+line_compteur+". "+line);
				//e.printStackTrace();
			}
		}
		FicheReader.errorFile.flush();
		br.close();
	}
	
	public Fiches(Fiches... fiches) {
		for(int i = 0 ; i<fiches.length ; i++){
			for(Fiche f : fiches[i].fiches){
				this.fiches.add(f);
			}
		}
	}

	public void writesWith(FileWriter fw) throws IOException, SQLException{
		fw.append("INSERT INTO `fiche` (`fiche_id`,`fiche_lieudit`,`fiche_date_min`,`fiche_date`,`fiche_memo`,`fiche_utm_utm`,`fiche_commune_ville_id`,`fiche_date_soumission`) VALUES\n");
		for(Fiche f : fiches.subList(0, fiches.size()-1)){
			try {
				fw.append(f.toSQL());
			} catch (SQLException e) {
				FicheReader.errorFile.write("Nom de commune introuvable: "+f.commune+"\n"+f.line_origine+"\n");
			}
		}
		Fiche f = fiches.get(fiches.size()-1);
		try {
			String s = f.toSQL();
			fw.append(s.substring(0, s.length()-2)+";");
		} catch (SQLException e) {
			FicheReader.errorFile.write("Nom de commune introuvable: "+f.commune+"\n"+f.line_origine+"\n");
		}
		
		//Témoins
		fw.append("\nINSERT INTO `fiche_has_membre` (`membre_membre_id`,`fiche_fiche_id`) VALUES\n");
		for(Fiche f1 : fiches.subList(0, fiches.size()-1)){
			try {
				int membre_id = getMembreId(f1,f1.temoin1);
				if(membre_id!=0){
					fw.append("("+membre_id+","+f1.id+"),\n");
				}
				if(!f1.temoin2.equals("")){
					membre_id = getMembreId(f1,f1.temoin2);
					if(membre_id!=0){
						fw.append("("+membre_id+","+f1.id+"),\n");
					}
				}
				if(!f1.temoin3.equals("")){
					membre_id = getMembreId(f1,f1.temoin3);
					if(membre_id!=0){
						fw.append("("+membre_id+","+f1.id+"),\n");
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		Fiche f1 = fiches.get(fiches.size()-1);
		int membre_id = getMembreId(f1,f1.temoin1);
		if(membre_id!=0){
			fw.append("("+membre_id+","+f1.id+");\n");
		}
		if(!f1.temoin2.equals("")){
			membre_id = getMembreId(f1,f1.temoin2);
			if(membre_id!=0){
				fw.append("("+membre_id+","+f1.id+");\n");
			}
		}
		if(!f1.temoin3.equals("")){
			membre_id = getMembreId(f1,f1.temoin3);
			if(membre_id!=0){
				fw.append("("+membre_id+","+f1.id+");\n");
			}
		}
	}
	
	private int getMembreId(Fiche f, String temoin) throws IOException, SQLException {
		if(temoin.equals("et alii") || temoin.equals("et al"))
			return getMembreId("et al.");
		int res = 0;
		Statement statement = FicheReader.connect.createStatement();
		String query = "SELECT membre_id FROM membre WHERE membre_nom='"+temoin.replaceAll("'", "''")+"';";
		ResultSet resultSet = statement.executeQuery(query);
		while(resultSet.next())
			res = resultSet.getInt("membre_id");
		if(temoin.equals("anonyme") || temoin.equals("Anonyme") || temoin.equals("ANONYME"))
			res = getMembreId("Témoin anonyme");
		else if(temoin.equals("Anonyme (MHNN)") || temoin.equals("anonyme (MHNN)"))
			res = getMembreId("Témoin anonyme dont la collection est au Muséum d'Histoire Naturelle de Nantes");
		else if(res==0 && temoin.equals("VINCENT"))
			res = getMembreId("VINCENT Colonel");
		else if(res==0 && temoin.equals("Fifi (pseudo)"))
			res = getMembreId("LARUE Philippe");
		else if(res==0 && temoin.equals("LE NEUTHIEC R."))
			res = getMembreId("LE NEUTHIEC Robert");
		else if(res==0 && temoin.equals("PROUTEAU E."))
			res = getMembreId("PROUTEAU Emile");
		else if(res==0 && temoin.equals("GODART"))
			res = getMembreId("GODART A.");
		else if(res==0 && temoin.equals("LEROY P."))
			res = getMembreId("LEROY-PENVEN Elise");
		else if(res==0 && temoin.equals("LEPERTEL N."))
			res = getMembreId("LEPERTEL Nicole");
		else if(res==0 && temoin.equals("Bretagne Vivante"))
			res = getMembreId("Sect° Nantes, Bretagne-Vivante");
		if(res==0)
			FicheReader.errorFile.erreurMembre("Membre introuvable : "+temoin+"\n"+f.line_origine);
		return res;
	}
	
	private int getMembreId(String temoin) throws SQLException{
		int res= 0;
		Statement statement = FicheReader.connect.createStatement();
		String query = "SELECT membre_id FROM membre WHERE membre_nom='"+temoin.replaceAll("'", "''")+"';";
		ResultSet resultSet = statement.executeQuery(query);
		while(resultSet.next())
			res = resultSet.getInt("membre_id");
		return res;
	}

	public Fiche find(String numero) throws NamingException{
		for(Fiche f : fiches){
			if(f.numero.equals(numero))
				return f;
		}
		throw new NamingException("Impossible de trouver la fiche "+numero+" dans "+this.filename);
	}
}
