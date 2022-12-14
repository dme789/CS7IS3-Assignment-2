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
            // Id: Useful
            String id = element.getElementsByTag("DOCNO").text();
            document.add(new StringField("id", id, Field.Store.YES));

            // Text: Useful
            String text = element.getElementsByTag("TEXT").text();
            document.add(new TextField("text", text, Field.Store.YES));

            // Byline: Potentially useful
            String byline = element.getElementsByTag("BYLINE").text();
            document.add(new TextField("byline", byline, Field.Store.YES));

            // Correction: Potentially usseful
            String correction = element.getElementsByTag("CORRECTION").text();
            document.add(new TextField("correction", correction, Field.Store.YES));

            // Correction Date: Probably not useful
            String correction_date = element.getElementsByTag("CORRECTION-DATE").text();
            document.add(new TextField("correction_date", correction_date, Field.Store.YES));

            // Date: Useful
            String date = element.getElementsByTag("DATE").text();
            document.add(new TextField("date", date, Field.Store.YES));

            // Location: Potentially useful
            String dateline = element.getElementsByTag("DATELINE").text();
            document.add(new TextField("dateline", dateline, Field.Store.YES));     

            // Graphic details: Potentially useful
            String graphic = element.getElementsByTag("GRAPHIC").text();
            document.add(new TextField("graphic", graphic, Field.Store.YES));

            // Headline: Useful
            String headline = element.getElementsByTag("HEADLINE").text();
            document.add(new TextField("title", headline, Field.Store.YES));

            // Section of book: Probably not useful
            String section = element.getElementsByTag("SECTION").text();
            document.add(new TextField("section", section, Field.Store.YES));

            // Subject of Doc: Potentially useful
            String subject = element.getElementsByTag("SUBJECT").text();
            document.add(new TextField("summary", subject, Field.Store.YES));

            // Type of Doc: Probably not useful
            String type = element.getElementsByTag("TYPE").text();
            document.add(new TextField("type", type, Field.Store.YES));

            // Table: Proabably not useful
            String table = element.getElementsByTag("TABLE ...").text();
            document.add(new TextField("table", table, Field.Store.YES));

            // Row of Table: Probably not useful
            String tablerow = element.getElementsByTag("TABLEROW").text();
            document.add(new TextField("tablerow", tablerow, Field.Store.YES));

            // Just the length of the Text so doubt we need this
            // String length = element.getElementsByTag("LENGTH").text();
            // document.add(new TextField("length", length, Field.Store.YES));
            
            // Nothing in these tags 
            // String cellrule = element.getElementsByTag("CELLRULE").text();
            // document.add(new TextField("cellrule", cellrule, Field.Store.YES));

            // P tag already get unravelled 
            // String p = element.getElementsByTag("P").text();
            // document.add(new TextField("p", p, Field.Store.YES));

            // Nothing in these tags 
            // String rowrule = element.getElementsByTag("ROWRULE").text();
            // document.add(new TextField("rowrule", rowrule, Field.Store.YES));
            
            // Don't need to get by cell, by table should be fine 
            // String tablecell = element.getElementsByTag("TABLECELL ...").text();
            // document.add(new TextField("tablecell", tablecell, Field.Store.YES));

            // Don't need to get by cell, by table should be fine 
            // String tablerow = element.getElementsByTag("TABLEROW").text();
            // document.add(new TextField("tablerow", tablerow, Field.Store.YES));

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
