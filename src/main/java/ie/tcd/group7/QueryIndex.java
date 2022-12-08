package ie.tcd.group7;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.lucene.analysis.Analyzer;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.*;

import org.apache.lucene.store.FSDirectory;

import org.apache.lucene.index.DirectoryReader;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;

public class QueryIndex {

    private static String INDEX_DIRECTORY = "index";
    private static String QUERY_DATA = "data/Assignment Two/Queries/topics";

    public static List<Query> search(Analyzer analyzer) throws Exception
    {
        MultiFieldQueryParser queryParser = new MultiFieldQueryParser(new String[]{"title", "text"}, analyzer);

        List<Query> queries = new ArrayList<Query>();
        try {
            System.out.println("Starting index querying...");
            BufferedReader queryReader = new BufferedReader(new FileReader(QUERY_DATA));
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


                // Removing un-worthwhile sections of narrative from topic
                String[] splitNar = narrative.split("(?<=[a-z])\\.\\s+");
                String usefulNar = "";
                for(int i = 0; i < splitNar.length; i++) {
                    boolean check1 = splitNar[i].toLowerCase().contains("not relevant");
                    boolean check2 = splitNar[i].toLowerCase().contains("irrelevant");
                    if (check1 || check2) {
                        String content = splitNar[i].toLowerCase();
                        if(content.contains("unless")) {
                            usefulNar = usefulNar + content.split("unless")[1];
                        }
                    }
                    else {
                        usefulNar = usefulNar + splitNar[i];
                    }
                }
                // query composition from topic fields
                query = (title+ "\n" + usefulNar  + "\n" + description).trim();
                query = query.replace("?", "");
                Query queryQ = queryParser.parse(QueryParser.escape(query));
                queries.add(queryQ);
            }
            queryReader.close();
            System.out.println("FINISHED: Total queries created is " + queryNumber);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return queries;
    }
}
