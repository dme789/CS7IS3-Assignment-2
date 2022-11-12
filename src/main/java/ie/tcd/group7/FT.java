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


public class FT {

    public ArrayList<Document> parse_file() throws IOException{

    ArrayList<Document> ft_data = new ArrayList();

    Path path = Paths.get("data/Assignment Two/Assignment Two/ft/ft911/");
    byte[] fileBytes = Files.readAllBytes(path);
    String fileString = new String(fileBytes, StandardCharsets.ISO_8859_1);


                //using Jsoup parsing package to split into fields by <> tag 
                org.jsoup.nodes.Document soup = Jsoup.parse(fileString);

                //splits the file at each new DOC tag
                List<Element> elements = soup.getElementsByTag("DOC");
                Document document = new Document();

                //for each DOC, split it into its various fields
                for(Element element : elements){

                //Creates a document with the fields specified to be written to an index
                String id = element.getElementsByTag("DOCNO").text();
                document.add(new StringField("id", id, Field.Store.YES));

                String headline = element.getElementsByTag("HEADLINE").text();
                document.add(new StringField("title", headline, Field.Store.YES));

                String profile = element.getElementsByTag("PROFILE").text();
                document.add(new TextField("profile", profile, Field.Store.YES));

                String date = element.getElementsByTag("DATE").text();
                document.add(new TextField("date", date, Field.Store.YES));

                String text = element.getElementsByTag("TEXT").text();
                document.add(new TextField("text", text, Field.Store.YES));

                String pub = element.getElementsByTag("PUB").text();
                document.add(new TextField("pub", pub, Field.Store.YES));

                String page = element.getElementsByTag("PAGE").text();
                document.add(new TextField("page", page, Field.Store.YES));

                ft_data.add(document);

}
                return ft_data;

}

public ArrayList<Document> access_directory() throws IOException{


    ArrayList<Document> ft_data = parse_file();

    return ft_data;
}

}


