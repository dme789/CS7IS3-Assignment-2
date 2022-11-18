package ie.tcd.group7;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;

import org.apache.lucene.search.similarities.*;

import org.apache.lucene.store.FSDirectory;

import org.apache.lucene.index.DirectoryReader;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.IndexSearcher;

public class QueryIndex {

    private static String INDEX_DIRECTORY = "index";
    private static String QUERY_DATA = "data/Assignment Two/Queries/topics";
    private static String RESULT_DIRECTORY = "results/query-results.txt";

    public static void search(int scoringType, Analyzer analyzer) throws Exception

    {
        DirectoryReader ireader = DirectoryReader.open(FSDirectory.open(Paths.get(INDEX_DIRECTORY)));

        // Use IndexSearcher to retrieve some arbitrary document from the index
        IndexSearcher isearcher = new IndexSearcher(ireader);

        // Sets the scoring as the same that was used to index the documents
        switch(scoringType) {
            case 1:
                isearcher.setSimilarity(new BM25Similarity());
                break;
            case 2:
                isearcher.setSimilarity(new ClassicSimilarity());
                break;
            case 3:
                isearcher.setSimilarity(new BooleanSimilarity());
                break;
            case 4:
                isearcher.setSimilarity(new LMDirichletSimilarity());
                break;
        }


        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[]{"title", "text", "pub", "profile", "header"}, analyzer);

        try {
            System.out.println("Starting index querying...");
            BufferedReader queryReader = new BufferedReader(new FileReader(QUERY_DATA));
            BufferedWriter queryWriter = new BufferedWriter(new FileWriter(RESULT_DIRECTORY));
            String currLine = queryReader.readLine();
            int queryNumber = 0;
            // loops through entire topic file generating and querying from topics
            while(currLine != null) {
                queryNumber++;
                String num = "";
                String title = "";
                String description = "";
                String narrative = "";
                String query = "";

                // creates query from topic
                if (currLine.startsWith("<top>")) {
                    currLine = queryReader.readLine();
                    String currAtr = "";
                    while (!currLine.startsWith("</top>")) {
                        if(currLine.equals("")) {
                            currAtr = "";
                            currLine = queryReader.readLine();
                        }
                        if (currLine.startsWith("</top")) {
                            break;
                        }
                        if (currLine.startsWith("<num>")) {
                            num = currLine.substring(currLine.length()-4).trim();
                        } else if (currLine.startsWith("<title>")) {
                            title = currLine.substring(8).trim();
                        } else if (currLine.startsWith("<desc>") || currLine.startsWith("<narr")) {
                            currAtr = currLine.substring(0,2);
                            currLine = queryReader.readLine();
                        }
                        if (currAtr.equals("<d")) {
                            description = description + " " + currLine;
                        } else if (currAtr.equals("<n")) {
                            narrative = narrative + " " + currLine;
                        }
                        currLine = queryReader.readLine();
                    }
                    description = description.trim();
                    narrative = narrative.trim();
                }
                // do this twice to skip blank line between topics
                currLine = queryReader.readLine();
                currLine = queryReader.readLine();

                // query composition from topic fields
                query = (title+ "\n" + narrative + "\n" + description).trim();
                query = query.replace("?", "");
                Query queryQ = queryParser.parse(QueryParser.escape(query));
                ScoreDoc[] hits = isearcher.search(queryQ, 1000).scoreDocs;

                // writing results of query
                for (int i =0; i < hits.length; i++) {
                    queryWriter.write(num + " Q0 " + isearcher.doc(hits[i].doc).get("id") + " " + i +
                            " " + hits[i].score + " STANDARD");
                    queryWriter.newLine();
                }

            }
            queryReader.close();
            queryWriter.close();
            System.out.println("FINISHED: Queries, total queries performed is " + queryNumber);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
