import org.jsoup.Jsoup;
import sun.nio.cs.StandardCharsets;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class Main {

    private static ArrayList<String> pages = new ArrayList<String>();

    /**
     * Remove special chars to save the file name
     * @param in
     * @return
     */
    private static String removeSpecialChars(String in) {
        return "content_" + in.replaceAll("[^\\w\\s\\-_]", " ") + ".txt";
    }

    /**
     * Reads the pages.txt file to get web addresses
     * @throws IOException
     */
    private static void readPages() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(new File("./pages.txt")));
        String line;
        while ((line = br.readLine()) != null){

            //skip if comment
            if (line.startsWith("//") || line.length() == 0) continue;

            //replace the space with no space
            line.replaceAll(" ", "");

            //add page
            pages.add(line);
        }
    }

    /**
     * Read file as string
     * @param path
     * @return
     * @throws IOException
     */
    private static String readFileAsString(String path) throws IOException {
        File f = new File(path);

        if (f.exists()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\r\n");
            }

            br.close();

            return sb.toString();
        }
        return "";
    }

    /**
     * HTML to Text using Jsoup
     * @param html
     * @return
     */
    public static String html2Text(String html) {
        String noHtml = Jsoup.parse(html).text();
        //Document doc = Jsoup.parse(noHtml);
        //doc.select("a,script,.hidden,style,form,span").remove();
        return noHtml;
    }

    /**
     * Compares two strings
     * @param n1
     * @param n2
     * @return
     */
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

        int length = out.length();
        if (length > 200) length = 200;

        if (length > 0) {
            System.out.println("First 200 characters: " + out.substring(0, length));
        }

        return out;
    }


    /**
     * O programa irá printar as diferenças nas páginas uma vez que o cache foi salvo (precisa rodar 2x).
     * Sempre que houver uma mudança na página ele irá printar
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        //initialize and read pages
        readPages();

        BufferedReader br;
        BufferedWriter bw;

        //go through all the pages
        for (int i=0; i<pages.size(); i++){

            URL url = new URL(pages.get(i));
            String filePath = "./cache/" + removeSpecialChars(url.toString());

            br = null;
            try {
                //reads the web page
                br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
            }catch(Exception e){
                System.out.println("Page not found: " + pages.get(i) + "\r\n \r\n");
                continue;
            }

            //reading lines
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\r\n");
            }

            //creating n1 and n2 to compare
            String fileString = readFileAsString(filePath);
            String n1 = fileString, n2 = html2Text(sb.toString());


            if (compare(n1, n2).length() != 0) {
                System.out.println("===> " + pages.get(i) + "\r\n \r\n");

                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"));
                bw.write(n2);
                bw.flush();
                bw.close();
            }

        }




    }


}
