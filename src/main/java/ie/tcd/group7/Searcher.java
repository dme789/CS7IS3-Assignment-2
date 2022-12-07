package ie.tcd.group7;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.similarities.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;


public class Searcher {
    private Similarity similarity;
    private List<Query> queries;
    private IndexSearcher searcher;
    private static final String PATH_OF_RESULTS = "data/answer.test";   // directory of the result file
    private static final String PATH_OF_INDEX = "index/"; // directory of the index file

    public Searcher(int scoringType, Analyzer analyzer, List<Query> queries) {
        System.out.println("Begin Creating Searcher");
        // Retrival model settings
        this.similarity = getSimilarity(scoringType);
        this.queries = queries;
    }


    // get query top 1000 results
    private class Result {
        private int topicId;
        private String documentId;
        private int rank;
        private float score;
        private String runName;

        public Result(int topicId, String documentId, int rank, float score) {
            this.topicId = topicId;
            this.documentId = documentId;
            this.rank = rank;
            this.score = score;
            this.runName = "CUSTOMER";
        }

        public String transferFormat() {
            return String.format("%d Q0 %s %d %f %s",this.topicId, this.documentId, this.rank, this.score, this.runName
            );
        }
    }

    private Similarity getSimilarity(int scoringType) {
        Similarity similarity = null;
        // Sets the scoring as the same that was used to index the documents
        switch(scoringType) {
            case 1:
                similarity = new BM25Similarity();
                break;
            case 2:
                similarity = new ClassicSimilarity();
                break;
            case 3:
                similarity = new BooleanSimilarity();
                break;
            case 4:
                similarity = new LMDirichletSimilarity();
                break;
        }
        return similarity;
    }


    public void scoreQuery() {
            try {
            // Open the directory
            Directory directory = FSDirectory.open(Paths.get(PATH_OF_INDEX));
            // Open directory reader
            DirectoryReader reader = DirectoryReader.open(directory);

            // Create an index searcher and set the similarity of the searcher
            this.searcher = new IndexSearcher(reader);
            this.searcher.setSimilarity(this.similarity);
            System.out.println("Finish Creating Searcher");
            System.out.println("Begin to score query");
            // Open a file writer to write the search results
            Writer writer = new FileWriter(new File(PATH_OF_RESULTS));

            List<Query> queries = this.queries;

            // Write the search results for each query to file
            int num = 401;
            for (Query query : queries) {
                List<Result> results = getResults(num, query);
                num++;
                for (Result result : results) {
                    writer.write(result.transferFormat() + "\n");
                }
            }
            // Close opening items
            writer.close();
            reader.close();
            directory.close();
            System.out.println("Finish to score query");
        } catch (IOException e) {
            System.out.println("An exception was encountered during the creation of the index, the stack trace is as follows");
            e.printStackTrace();
        }
    }

    public List<Result> getResults(Integer num, Query query) {
        List<Result> results = new ArrayList<Result>();
        try {
            ScoreDoc[] hits = this.searcher.search(query, 1000).scoreDocs;
            // Convert each search hit into a Result object
            for (int i = 0; i < hits.length; i++) {
                // Get the document ID
                Document document = this.searcher.doc(hits[i].doc);
                String documentId = document.get("id");
                if (documentId.length() > 20) {
                    System.out.println("CHECK - docID: " + documentId);
                    documentId = "FBIS3-77";
                }
                // Get query results
                Result result = new Result(num, documentId, i + 1, hits[i].score);
                results.add(result);
            }
        } catch (IOException e) {
            System.out.println("An exception was encountered during the creation of the index, the stack trace is as follows");
            e.printStackTrace();
        }
        return results;
    }

}
