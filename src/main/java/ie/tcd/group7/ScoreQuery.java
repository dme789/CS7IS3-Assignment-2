package ie.tcd.group7;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.Query;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Scanner;

public class ScoreQuery
{

    public static void main(String[] args) throws Exception
    {
        // Analyzer that is used to process TextField
        Analyzer analyzer = new CustomerAnalyzer();

        // create index, returns int defining scoring type
//        int scoringType = CreateIndex.createIndex(analyzer);

        // query index
        List<Query> queries = QueryIndex.search(1, analyzer);
        Searcher searcher = new Searcher(1, queries);
        searcher.scoreQuery();

        System.out.println("PROGRAM COMPLETED");
    }

}
