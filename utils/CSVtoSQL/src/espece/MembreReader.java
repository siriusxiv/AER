package espece;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class MembreReader {

	static Connection connect = null;
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection("jdbc:mysql://localhost/aer?"+"user=root&password=");
		
		doIt();
		
		connect.close();
	}
	
	public static void doIt() throws IOException{
		File[] files = new File(".").listFiles();
		ArrayList<Membre> membres = new ArrayList<Membre>();
		for(int i = 0 ; i<files.length ; i++){
			System.out.println(files[i].getName());
			if(files[i].getName().startsWith("MEMBRES")){
				FileReader fr = new FileReader(files[i].getName());
				BufferedReader br = new BufferedReader(fr);
				String line;
				br.readLine();
				int c=1;
				while((line=br.readLine())!=null){
					c++;
					Membre m;
					try{
						m = new Membre(line);
						if(m.isNotIn(membres))
							membres.add(m);
					}catch(ArrayIndexOutOfBoundsException e){
						System.out.println(files[i].getName()+":"+c+". "+line);
						e.printStackTrace();
					}
				}
				br.close();
			}
		}
		FileWriter fw = new FileWriter("out.sql");
		fw.append("INSERT INTO `membre` (`membre_nom`,`membre_adresse`,`membre_adresse_complement`,`membre_code_postal`,`membre_ville`,`membre_pays`,`membre_confidentialite_confidentialite_id`,`membre_abonne`,`membre_temoin_actif`,`membre_journais`,`membre_moisnais`,`membre_annenais`,`membre_jourdece`,`membre_moisdece`,`membre_annedece`,`membre_biographie`,`membre_email`,`membre_sel`,`membre_droits_droits_id`,`membre_inscription_acceptee`) VALUES\n");
		for(Membre m : membres){
			fw.append(m.toString());
		}
		fw.close();
	}
	
}
