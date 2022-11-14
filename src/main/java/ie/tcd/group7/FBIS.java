package ie.tcd.group7;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.nio.file.Paths;
import java.util.Scanner;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class FBIS {

    /*File Structure:
     * <DOC>
<DOCNO> FBIS3-1 </DOCNO>
<HT>  "cr00000011094001" </HT>
<HEADER>
<H2>   March Reports </H2>
<DATE1>  1 March 1994 </DATE1>
Article Type:FBIS 
Document Type:FOREIGN MEDIA NOTE--FB PN 94-028 
<H3> <TI>      FORMER YUGOSLAV REPUBLIC OF MACEDONIA: OPINION POLLS ON </TI></H3>
</HEADER>
<TEXT>blah blah blah </TEXT>
    </DOC>
     */

    private final static File FBIS_DIR = new File("data/Assignment Two/Assignment Two/fbis");
    ArrayList<Document> fbis_data = new ArrayList();

    public ArrayList<Document> parse_FBIS() throws IOException {
        //ArrayList<Document> 
        access_directory_FBIS(FBIS_DIR.getAbsolutePath());

        return fbis_data;
    }

    public void parse_file_FBIS(String file) throws IOException {
        //ArrayList<Document> ft_data = new ArrayList();

        //Path path = Paths.get("data/Assignment Two/Assignment Two/ft/ft911/");
        //byte[] fileBytes = Files.readAllBytes(path);
        //String fileString = new String(fileBytes, StandardCharsets.ISO_8859_1);

        File parse_file = new File(file);

        // using Jsoup parsing package to split into fields by <> tag
        org.jsoup.nodes.Document soup = Jsoup.parse(parse_file, "UTF-8", "http://example.com/");

        // splits the file at each new DOC tag
        List<Element> elements = soup.getElementsByTag("DOC");
        //Document document = new Document();

        // for each DOC, split it into its various fields
        for (Element element : elements) {
            
            Document document = new Document();

            // Creates a document with the fields specified to be written to an index
            String id = element.getElementsByTag("DOCNO").text();
            document.add(new StringField("id", id, Field.Store.YES));

            String HT = element.getElementsByTag("HT").text();
            document.add(new StringField("HT", HT, Field.Store.YES));

            String header = element.getElementsByTag("HEADER").text();
            document.add(new TextField("header", header, Field.Store.YES));

            String text = element.getElementsByTag("TEXT").text();
            document.add(new TextField("text", text, Field.Store.YES));

            String date = element.getElementsByTag("DATE1").text();
            document.add(new TextField("date", date , Field.Store.YES)); 


            fbis_data.add(document);

        }
        //return ft_data;

    }

    public void access_directory_FBIS(String filepath) throws IOException {

        // creates file at filepath
        File file_dir = new File(filepath);
        // make list of all files in the directory
        File[] dir_list = file_dir.listFiles();

        if (dir_list != null) {
            for (File file : dir_list) {
                //if still in a folder, keep going deeper untit the directory 
                if (file.isDirectory()) {
                    access_directory_FBIS(file.getAbsolutePath());
                }
                else{
                    if(!file.getName().equals("readchg") && !file.getName().equals("readmefb")){
                        //when you have access to file, parse it with parse function
                        parse_file_FBIS(file.getAbsolutePath());
                    }
                }

            }
        }

    }

}


