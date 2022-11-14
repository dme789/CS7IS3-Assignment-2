package ie.tcd.group7;

import ie.tcd.group7.FT;
import ie.tcd.group7.FR;
import ie.tcd.group7.FBIS;
import ie.tcd.group7.LA;

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

public class CreateIndex {

    // Directory where the search index will be saved
    private static String INDEX_DIRECTORY = "index";
    private static String CRAN_DATA = "data/Assignment Two/Assignment Two/ft/ft911/ft911_1";

    public static int createIndex(Analyzer analyzer) throws IOException {

        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));

        // Set up an index writer to add process and save documents to the index
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        int scoringType = setScoring(config);
        IndexWriter iwriter = new IndexWriter(directory, config);

        try {
            System.out.println("Starting index processing...");
            BufferedReader cranReader = new BufferedReader(new FileReader(CRAN_DATA));
            String currLine = cranReader.readLine();
            int docNumbers = 0;
            /*
             * while(currLine != null) {
             * String title = "";
             * String author = "";
             * String bib = "";
             * String words = "";
             * Document doc = new Document();
             * 
             * // checking for new document
             * if (currLine.startsWith(".I")) {
             * // adding doc id to document
             * String id_val = currLine.substring(3).trim();
             * doc.add(new StringField("id", id_val, Field.Store.YES));
             * currLine = cranReader.readLine();
             * String currAtr = ""; // used to keep track of what doc element is being read
             * while (currLine != null) {
             * if (currLine.startsWith(".T") || currLine.startsWith(".A") ||
             * currLine.startsWith(".B") || currLine.startsWith(".W")) {
             * currAtr = currLine.substring(0,2);
             * currLine = cranReader.readLine();
             * } else if (currLine.startsWith(".I")) { // end of doc
             * break;
             * }
             * // add space as there may be two words joined otherwise
             * if (!currLine.substring(0,1).equals(" ")) {
             * currLine = " " + currLine;
             * }
             * if (currAtr.equals(".T")) {
             * title = title + currLine;
             * } else if (currAtr.equals(".A")) {
             * author = author + currLine;
             * } else if (currAtr.equals(".B")) {
             * bib = bib + currLine;
             * } else if (currAtr.equals(".W")) {
             * words = words + currLine;
             * }
             * currLine = cranReader.readLine();
             * }
             * doc.add(new TextField("title", title.trim(), Field.Store.YES));
             * doc.add(new TextField("author", author.trim(), Field.Store.YES));
             * doc.add(new TextField("bib", bib.trim(), Field.Store.YES));
             * doc.add(new TextField("content", words.trim(), Field.Store.YES));
             * iwriter.addDocument(doc);
             * }
             * 
             * docNumbers++;
             * }
             * 
             * 
             * Path path = Paths.get("data/Assignment Two/Assignment Two/ft/ft911/ft911_1");
             * byte[] fileBytes = Files.readAllBytes(path);
             * String fileString = new String(fileBytes, StandardCharsets.ISO_8859_1);
             * 
             * 
             * 
             * 
             * // final File FT_DIR = new File("data/Assignment Two/Assignment Two/ft");
             * //String get_path = FT_DIR.getAbsolutePath();
             * 
             * //public static void access_directory(String get_path){
             * // String file4;
             * //}
             * 
             * access_directory(FT_DIR.getAbsolutePath());
             * //get abs path of ft folder
             * //File get_path = new File(FT_DIR.getAbsolutePath());
             * //returns and stores as list the list of files in that folder
             * File[] file_list = get_path.listFiles();
             * 
             * while(file_list != null){
             * for(File file : file_list){
             * if(file.isDirectory()){
             * 
             * }
             * }
             * }
             * 
             * // currLine = cranReader.readLine();
             * 
             * 
             * 
             

// ----------------------Parsing Financial Times data----------------------------

            // FT class to access functions in FT.java
            FT data = new FT();
            // parse_file function in FT.java splits the doc into fields.
            ArrayList<Document> documents = data.parse_FT();
            // for loop to parse every occurence of DOC tag, not just one.
            for (Document document : documents) {
                iwriter.addDocument(document);
                docNumbers++;
                // System.out.println(document);
            }
            

// ----------------------Parsing Federal Register Data----------------------------

            FR FRdata = new FR();
            // parse_file function in FT.java splits the doc into fields.
            ArrayList<Document> FRdocuments = FRdata.parse_FR();
            // for loop to parse every occurence of DOC tag, not just one.
            for (Document FRdocument : FRdocuments) {
                iwriter.addDocument(FRdocument);
                docNumbers++;
                System.out.println(FRdocument);
            }

// ----------------------Parsing FBIS data----------------------------

            FBIS FBISdata = new FBIS();
            // parse_file function in FT.java splits the doc into fields.
            ArrayList<Document> FBISdocuments = FBISdata.parse_FBIS();
            // for loop to parse every occurence of DOC tag, not just one.
            for (Document FBISdocument : FBISdocuments) {
                iwriter.addDocument(FBISdocument);
                docNumbers++;
                //System.out.println(FBISdocument);
            }
            */

// ----------------------Parsing LA times data----------------------------

            LA LAdata = new LA();
            // parse_file function in FT.java splits the doc into fields.
            ArrayList<Document> LAdocuments = LAdata.parse_LA();
            // for loop to parse every occurence of DOC tag, not just one.
            for (Document LAdocument : LAdocuments) {
                iwriter.addDocument(LAdocument);
                docNumbers++;
                System.out.println(LAdocument);
            }


            cranReader.close();
            System.out.println("FINISHED: Indexing, total docs added is " + docNumbers);

        } catch (Exception e) {
            e.printStackTrace();
        }

        iwriter.close();
        directory.close();
        return scoringType;
    }

    // asks the user for scoring type and sets the scoring for indexing of docs and
    // queries
    public static int setScoring(IndexWriterConfig config) {
        Scanner scoringIn = new Scanner(System.in);
        System.out.println("Please select the type of Scoring:\n1. 1 for BM25\n2. 2 for Classic (VSM)\n3. " +
                "3 for Boolean\n4. 4 for LMDirichlet");
        int scoringType = scoringIn.nextInt();

        switch (scoringType) {
            case 1:
                config.setSimilarity(new BM25Similarity());
                System.out.println("Selected BM25 for scoring.");
                break;
            case 2:
                config.setSimilarity(new ClassicSimilarity());
                System.out.println("Selected Classic (VSM) for scoring.");
                break;
            case 3:
                config.setSimilarity(new BooleanSimilarity());
                System.out.println("Selected Boolean for scoring.");
                break;
            case 4:
                config.setSimilarity(new LMDirichletSimilarity());
                System.out.println("Selected LMDirichlet for scoring.");
                break;
            default:
                config.setSimilarity(new BM25Similarity());
                System.out.println("Default selected - BM25 scoring.");
                scoringType = 1;
                break;
        }
        return scoringType;
    }

}
