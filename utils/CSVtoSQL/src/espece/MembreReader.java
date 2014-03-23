package espece;

import java.io.BufferedReader;
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
		FileReader fr = new FileReader("MEMBRES.csv");
		BufferedReader br = new BufferedReader(fr);
		String line;
		br.readLine();
		int c=1;
		ArrayList<Membre> membres = new ArrayList<Membre>();
		while((line=br.readLine())!=null){
			c++;
			Membre m;
			try{
				m = new Membre(line);
				membres.add(m);
			}catch(ArrayIndexOutOfBoundsException e){
				System.out.println(c+". "+line);
				e.printStackTrace();
			}
		}
		br.close();
		FileWriter fw = new FileWriter("out.sql");
		fw.append("INSERT INTO `membres` (`membre_nom`,`membre_adresse`,`membre_adresse_complement`,`membre_code_postal`,`membre_ville`,`membre_pays`,`membre_confidentialite_confidentialite_id`,`membre_abonne`,`membre_temoin_actif`,`membre_journais`,`membre_moisnais`,`membre_annenais`,`membre_jourdece`,`membre_moisdece`,`membre_annedece`,`membre_biographie`,`membre_email`,`membre_sel`,`membre_droits_droits_id`,`membre_inscription_acceptee`) VALUES\n");
		for(Membre m : membres){
			fw.append(m.toString());
		}
		fw.close();
	}
	
}
