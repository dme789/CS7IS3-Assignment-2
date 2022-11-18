package ie.tcd.group7;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

public class Main
{

    public static void main(String[] args) throws Exception
    {
        // Analyzer that is used to process TextField
        Analyzer analyzer = setAnalyzer();

        // create index, returns int defining scoring type
        int scoringType = CreateIndex.createIndex(analyzer);

        // query index
        Searcher searcher = new Searcher(scoringType, analyzer);
        searcher.scoreQuery();
        
        QueryIndex.search(scoringType, analyzer);

        System.out.println("PROGRAM COMPLETED");
    }

    // Asks the user for preferred analyzer to use and sets the analyzer.
    public static Analyzer setAnalyzer() throws IOException, ParseException {
        Analyzer analyzer = null;
        Scanner analyzerIn = new Scanner(System.in);
        System.out.println("Please select the type of Analyzer to use:\n1. 1 for English Analyzer\n" +
                "2. 2 for Standard Analyzer\n" + "3. 3 for Customer Analyzer\n");
        int analyzerType = analyzerIn.nextInt();

        switch(analyzerType) {
            case 1:
                analyzer = new EnglishAnalyzer();
                System.out.println("Selected English Analyzer.");
                break;
            case 2:
                analyzer = new StandardAnalyzer();
                System.out.println("Selected Standard Analyzer.");
                break;
            case 3:
                analyzer = new CustomerAnalyzer();
                System.out.println("Selected Customer Analyzer");
                break;
            default:
                analyzer = new EnglishAnalyzer();
                System.out.println("Default selected - English Analyzer.");
                break;
        }
        return analyzer;
    }
}
