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

    public static int createIndex(Analyzer analyzer) throws IOException {

        Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));

        // Set up an index writer to add process and save documents to the index
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        int scoringType = setScoring(config);
        IndexWriter iwriter = new IndexWriter(directory, config);

        try {
            System.out.println("Starting index processing...");

// ----------------------Parsing Financial Times data----------------------------

            // FT class to access functions in FT.java
            FT FTdata = new FT();
            // parse_file function in FT.java splits the doc into fields.
            // Each document is indexed once document is complete
            FTdata.parse_FT(iwriter);


// ----------------------Parsing Federal Register Data----------------------------

            FR FRdata = new FR();
            // parse_file function in FT.java splits the doc into fields.
            // Each document is indexed once document is complete
            FRdata.parse_FR(iwriter);

// ----------------------Parsing FBIS data----------------------------

            FBIS FBISdata = new FBIS();
            // parse_file function in FT.java splits the doc into fields.
            // Each document is indexed once document is complete
            FBISdata.parse_FBIS(iwriter);


// ----------------------Parsing LA times data----------------------------

            LA LAdata = new LA();
            // parse_file function in FT.java splits the doc into fields.
            LAdata.parse_LA(iwriter);

            System.out.println("FINISHED: Indexing!");
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
