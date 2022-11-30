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

public class FR {

    /*File Structure:
     * <DOC>
    <DOCNO> FR940104-0-00001 </DOCNO>
    <PARENT> FR940104-0-00001 </PARENT>
    <TEXT>blah blah blah </TEXT>
    </DOC>
     */

    private final static File FR_DIR = new File("data/Assignment Two/Documents/fr94");

    public void parse_FR(IndexWriter iwriter) throws IOException {
        System.out.println("Starting indexing - Federal Register");
        access_directory_FR(FR_DIR.getAbsolutePath(), iwriter);
        System.out.println("Indexed Successfully - Federal Register");
    }

    public void parse_file_FR(String file, IndexWriter iwriter) throws IOException {
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

            String parent = element.getElementsByTag("PARENT").text();
            document.add(new StringField("parent", parent, Field.Store.YES));

            String text = element.getElementsByTag("TEXT").text();
            document.add(new StringField("text", text, Field.Store.YES));

            String USdept= element.getElementsByTag("USDEPT").text();
            document.add(new TextField("USdept", USdept, Field.Store.YES));

            String agency = element.getElementsByTag("AGENCY").text();
            document.add(new StringField("agency", agency, Field.Store.YES));

            String USBureau = element.getElementsByTag("USBUREAU").text();
            document.add(new TextField("USBureau", USBureau, Field.Store.YES));

            String doctitle = element.getElementsByTag("DOCTITILE").text();
            document.add(new TextField("doctitle", doctitle, Field.Store.YES));

            String address = element.getElementsByTag("ADDRESS").text();
            document.add(new TextField("address", address, Field.Store.YES));

            String further = element.getElementsByTag("FURTHER").text();
            document.add(new TextField("further", further, Field.Store.YES));

            String summary = element.getElementsByTag("SUMMARY").text();
            document.add(new TextField("summary", summary, Field.Store.YES));

            String action = element.getElementsByTag("ACTION").text();
            document.add(new TextField("action", action, Field.Store.YES));

            String signer = element.getElementsByTag("SIGNER").text();
            document.add(new TextField("SIGNER", signer, Field.Store.YES));

            String signjob = element.getElementsByTag("SIGNJOB").text();
            document.add(new TextField("signjob", signjob , Field.Store.YES)); 

            String supplem = element.getElementsByTag("SUPPLEM").text();
            document.add(new TextField("supplem", supplem, Field.Store.YES));

            String FRfiling = element.getElementsByTag("FRFILING").text();
            document.add(new TextField("FRfiling", FRfiling, Field.Store.YES));

            String billing = element.getElementsByTag("BILLING").text();
            document.add(new TextField("billing", billing, Field.Store.YES));

            String date = element.getElementsByTag("DATE").text();
            document.add(new TextField("date", date, Field.Store.YES));

            String CFRNO = element.getElementsByTag("CFRNO").text();
            document.add(new TextField("CFRNO", CFRNO, Field.Store.YES));

            String RINDOCK = element.getElementsByTag("RINDOCK").text();
            document.add(new TextField("RINDOCK", RINDOCK, Field.Store.YES));

            String table = element.getElementsByTag("TABLE").text();
            document.add(new TextField("table", table, Field.Store.YES));

            String footnote = element.getElementsByTag("FOOTNOTE").text();
            document.add(new TextField("footnote", footnote, Field.Store.YES));

            String footcite = element.getElementsByTag("FOOTCITE").text();
            document.add(new TextField("footcite", footcite, Field.Store.YES));

            String footname = element.getElementsByTag("FOOTNAME").text();
            document.add(new TextField("footname", footname, Field.Store.YES));

            iwriter.addDocument(document);
        }
    }

    public void access_directory_FR(String filepath, IndexWriter iwriter) throws IOException {

        // creates file at filepath
        File file_dir = new File(filepath);
        // make list of all files in the directory
        File[] dir_list = file_dir.listFiles();

        if (dir_list != null) {
            for (File file : dir_list) {
                //if still in a folder, keep going deeper untit the directory 
                if (file.isDirectory()) {
                    access_directory_FR(file.getAbsolutePath(), iwriter);
                }
                else{
                    if(!file.getName().equals("readmefr") && !file.getName().equals("readfrcg") && !file.getName().equals(".DS_Store")){
                        //when you have access to file, parse it with parse function
                        parse_file_FR(file.getAbsolutePath(), iwriter);

                    }
                }

            }
        }

    }

}
