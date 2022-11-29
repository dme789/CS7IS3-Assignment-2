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

public class LA {

    /*File Structure:
     * <DOC>
    <DOCNO> FR940104-0-00001 </DOCNO>
    <PARENT> FR940104-0-00001 </PARENT>
    <TEXT>blah blah blah </TEXT>
    </DOC>
     */

    private final static File LA_DIR = new File("data/Assignment Two/Documents/latimes");

    public void parse_LA(IndexWriter iwriter) throws IOException {
        System.out.println("Starting indexing - LA Times");
        access_directory_LA(LA_DIR.getAbsolutePath(), iwriter);
        System.out.println("Indexed Successfully - LA Times");
    }

    public void parse_file_LA(String file, IndexWriter iwriter) throws IOException {
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

            /* 

            String parent = element.getElementsByTag("PARENT").text();
            document.add(new StringField("parent", parent, Field.Store.YES));

            String profile = element.getElementsByTag("PROFILE").text();
            document.add(new TextField("profile", profile, Field.Store.YES));

            String signer = element.getElementsByTag("SIGNER").text();
            document.add(new TextField("SIGNER", signer, Field.Store.YES));

            String signjob = element.getElementsByTag("signjob").text();
            document.add(new TextField("signjob", signjob , Field.Store.YES)); 

            String FRfiling = element.getElementsByTag("FRFILING").text();
            document.add(new TextField("FRfiling", FRfiling, Field.Store.YES));

            String billing = element.getElementsByTag("BILLING").text();
            document.add(new TextField("billing", billing, Field.Store.YES));
             */

            iwriter.addDocument(document);
        }
    }

    public void access_directory_LA(String filepath, IndexWriter iwriter) throws IOException {

        // creates file at filepath
        File file_dir = new File(filepath);
        // make list of all files in the directory
        File[] dir_list = file_dir.listFiles();

        if (dir_list != null) {
            for (File file : dir_list) {
                //if still in a folder, keep going deeper untit the directory 
                if (file.isDirectory()) {
                    access_directory_LA(file.getAbsolutePath(), iwriter);
                }
                else{
                    if(!file.getName().equals("readchg") && !file.getName().equals("readmela")){
                        //when you have access to file, parse it with parse function
                        parse_file_LA(file.getAbsolutePath(), iwriter);

                    }
                }

            }
        }

    }

}
