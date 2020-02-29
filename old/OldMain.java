import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/*Funciona como uma única classe*/
/*Ajeitei o programa no novo main pra ler o cache de sites da mesma pasta do jar*/
public class OldMain {
	
	private static String path = "D:\\Google Drive\\Synced Folder\\Codigos\\VerificadorConcursos\\Cache\\";
	private static String[] pages = {
			"https://www.uac.pt/pt-pt/emprego-na-uac", //açores
			"https://www.ua.pt/sgrhf/PageText.aspx?id=15031", //aveiro
			"http://www.academicos.ubi.pt/Pagina/recrutamento", //beira interior
			"https://documentos.unila.edu.br/concursos", //unila
			"https://www.uc.pt/drh/rm/pconcursais/pessoal_docente/A_decorrer/fct", //coimbra
			"https://www.uc.pt/drh/rm/pconcursais/Investigadores/A_decorrer/FCT", //coimbra investigador
			"https://www.sadm.uevora.pt/documentos/concursos/(id)/5495/(basenode)/419", //évora
			"https://www.ulisboa.pt/node/19575/docentes?field_professional_category_tid_selective=62", //lisboa
			"http://urh.uma.pt/index.php?option=com_content&view=section&layout=blog&id=11&Itemid=73&lang=pt", //madeira
			"https://www.unl.pt/nova/docentes", //nova
			"https://sigarra.up.pt/up/pt/noticias_geral.lista_noticias?p_grupo_noticias=47", //porto
			"https://www.campus.utad.pt/cdes/Candidatura", //utad
			"https://ciencias.ulisboa.pt/concursos", //ciencias lisboa
			"https://www.ipl.pt/servicos/recursos-humanos/recrutamento", //politécnico lisboa
			"https://www.ipc.pt/pt/o-ipc/recursos-humanos/emprego-publico/procedimentos-concursais/pessoal-docente", //politecnico coimbra
			"https://www.ipleiria.pt/recursos-humanos/concursos/", //politecnico leiria
			"http://www.ipv.pt/rpdocente.htm", //politecnico viseu
			"https://www.ipsantarem.pt/pt/1650-2/concursos-pessoal-docente/", //politecnico santarem
			
	};
	//do minho n da
	// https://intranet.uminho.pt/Pages/Documents.aspx?Area=Procedimentos%20Concursais
	
	private static String removeSpecialChars(String in) {
		return in.replaceAll("[^\\w\\s\\-_]", " ") + ".txt"; 
	}
	
	private static String readFileAsString(String path) throws IOException {
		File f = new File(path);
		
		if (f.exists()) {
			BufferedReader br = new BufferedReader(new FileReader(f));
	         
			String line;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
                //sb.append(System.lineSeparator());
			}
			
			br.close();
			
			return sb.toString();
		}
		return "";
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = null;
		BufferedWriter bw = null;
		
		int itPgs = 0;
		while (itPgs < pages.length) {
			
	        try {
	        	URL url = new URL(pages[itPgs]);
	        	String filePath = path + removeSpecialChars(url.toString());
	            
	            br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
	           
	            String line;
	
	            StringBuilder sb = new StringBuilder();
	
	            while ((line = br.readLine()) != null) {
	
	                sb.append(line);
	                //sb.append(System.lineSeparator());
	                
	            }
	
	            //System.out.println(sb);
	            String fileString = readFileAsString(filePath);
	            
	        	String n1 = fileString, n2 = html2text(sb.toString());
	            
	        	if (compare(n1, n2).length() != 0) {
            		System.out.println(pages[itPgs] + System.lineSeparator() + System.lineSeparator());
            		
            		bw = new BufferedWriter(new PrintWriter(new FileWriter(filePath)));
	            	bw.write(n2);
	            	bw.flush();
	            	bw.close();
            	}
	            
	            
	        } finally {
	            if (br != null) {
	                br.close();
	            }
	        }
	        //fim try
	        
			itPgs++;
		}
		
	}
	
	public static String compare(String n1, String n2) {
		String out = "";
		for (int i=0; i<n2.length(); i++) {
			int c1 = -1;
			if (i < n1.length())
				c1 = Character.getNumericValue(n1.charAt(i));
		
			int c2 = Character.getNumericValue(n2.charAt(i));
			if (c1 != c2) {
				out += n2.charAt(i);
			}
		}
		System.out.println(out);
		return out;
	}
	
	public static String html2text(String html) {
		String noHtml = Jsoup.parse(html).text();
		//Document doc = Jsoup.parse(noHtml);
		//doc.select("a,script,.hidden,style,form,span").remove();
	    return noHtml;
	}
	
}
