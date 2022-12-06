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

    private final static File FBIS_DIR = new File("data/Assignment Two/Documents/fbis");

    public void parse_FBIS(IndexWriter iwriter) throws IOException {
        System.out.println("Starting indexing - FBIS");
        access_directory_FBIS(FBIS_DIR.getAbsolutePath(), iwriter);
        System.out.println("Indexed Successfully - FBIS");
    }

    public void parse_file_FBIS(String file, IndexWriter iwriter) throws IOException {
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

            // Abstract: Useful
            String abs = element.getElementsByTag("ABS").text();
            document.add(new TextField("abs", abs, Field.Store.YES));

            // Date: Useful
            String date = element.getElementsByTag("DATE1").text();
            document.add(new TextField("date", date, Field.Store.YES));

            // ID: Useful
            String id = element.getElementsByTag("DOCNO").text();
            document.add(new StringField("id", id, Field.Store.YES));

            // Gives detail on location and date: Potentially useful
            String F = element.getElementsByTag("F ...").text();
            document.add(new TextField("F", F, Field.Store.YES));

            // Headers: Useful 
            String H1 = element.getElementsByTag("H1").text();
            document.add(new TextField("H1", H1, Field.Store.YES));

            String H2 = element.getElementsByTag("H2").text();
            document.add(new TextField("H2", H2, Field.Store.YES));

            String H3 = element.getElementsByTag("H3").text();
            document.add(new TextField("H3", H3, Field.Store.YES));
            
            String H4 = element.getElementsByTag("H4").text();
            document.add(new TextField("H4", H4, Field.Store.YES));

            String H5 = element.getElementsByTag("H5").text();
            document.add(new TextField("H5", H5, Field.Store.YES));

            String H6 = element.getElementsByTag("H6").text();
            document.add(new TextField("H6", H6, Field.Store.YES));

            String H7 = element.getElementsByTag("H7").text();
            document.add(new TextField("H7", H7, Field.Store.YES));
        
            String H8 = element.getElementsByTag("H8").text();
            document.add(new TextField("H8", H8, Field.Store.YES));

            String HT = element.getElementsByTag("HT").text();
            document.add(new TextField("HT", HT, Field.Store.YES));

            String text = element.getElementsByTag("TEXT").text();
            document.add(new TextField("text", text, Field.Store.YES));

            // Another ID
            // String AU = element.getElementsByTag("AU").text();
            // document.add(new StringField("AU", AU, Field.Store.YES));
            
            // All the same
            // String header = element.getElementsByTag("HEADER").text();
            // document.add(new StringField("header", header, Field.Store.YES));

            // These don't actually seem to be appear anywhere in the docs 
            // String TR = element.getElementsByTag("TR").text();
            // document.add(new StringField("TR", TR, Field.Store.YES));

            // String TXT5 = element.getElementsByTag("TXT5").text();
            // document.add(new StringField("TXT5", TXT5, Field.Store.YES));

            // Doesn't seem to come up often so not useful
            // String fig = element.getElementsByTag("FIG ...").text();
            // document.add(new TextField("fig", fig, Field.Store.YES));


            iwriter.addDocument(document);
        }
    }

    public void access_directory_FBIS(String filepath, IndexWriter iwriter) throws IOException {

        // creates file at filepath
        File file_dir = new File(filepath);
        // make list of all files in the directory
        File[] dir_list = file_dir.listFiles();

        if (dir_list != null) {
            for (File file : dir_list) {
                //if still in a folder, keep going deeper untit the directory 
                if (file.isDirectory()) {
                    access_directory_FBIS(file.getAbsolutePath(), iwriter);
                }
                else{
                    if(!file.getName().equals("readchg") && !file.getName().equals("readmefb")){
                        //when you have access to file, parse it with parse function
                        parse_file_FBIS(file.getAbsolutePath(), iwriter);
                    }
                }

            }
        }

    }

}


