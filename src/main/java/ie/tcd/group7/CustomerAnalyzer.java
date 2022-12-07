package ie.tcd.group7;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.FlattenGraphFilter;
import org.apache.lucene.analysis.core.LetterTokenizer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.en.EnglishMinimalStemFilter;
import org.apache.lucene.analysis.en.EnglishPossessiveFilter;
import org.apache.lucene.analysis.en.KStemFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.miscellaneous.TrimFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.synonym.SynonymGraphFilter;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.analysis.synonym.WordnetSynonymParser;
import org.apache.lucene.util.CharsRef;
import org.apache.lucene.analysis.TokenStream;

public class CustomerAnalyzer extends Analyzer
{
    // Synonym paths
    private static final String COUNTRY_PATH = "./data/synonyms/country.txt";
    private CharArraySet stopwords;
    public CustomerAnalyzer() throws IOException, ParseException {
        this.stopwords = getStopwords();
    }

    private CharArraySet getStopwords() throws IOException
    {
        String path = "data/stopwords.txt";
        CharArraySet stopwords = new CharArraySet(Files.readAllLines(Paths.get(path)), true);
        return stopwords;
    }


    // create a synonymMap about country information
    private SynonymMap getSynonymMapCountry() {
        SynonymMap synMap = new SynonymMap(null, null, 0);
        try {
            BufferedReader countries = new BufferedReader(new FileReader(COUNTRY_PATH));
            // create builder
            final SynonymMap.Builder builder = new SynonymMap.Builder(true);
            String country = countries.readLine();
            // read country information
            while(country != null) {
                builder.add(new CharsRef("country"), new CharsRef(country), true);
                builder.add(new CharsRef("countries"), new CharsRef(country), true);
                country = countries.readLine();
            }

            synMap = builder.build();
        } catch (Exception e) {
            System.out.println("error: " + e.getLocalizedMessage() + "occurred when trying to create synonym map");
        }
        return synMap;
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        StandardTokenizer tokenizer = new StandardTokenizer();
        // Create TokenStream
        TokenStream stream = new LowerCaseFilter(tokenizer);;
        // Add stream filter
        stream = new TrimFilter(stream);
        stream = new EnglishPossessiveFilter(stream);
        stream = new PorterStemFilter(stream);
        stream = new StopFilter(stream, this.stopwords);
        stream = new FlattenGraphFilter(new SynonymGraphFilter(stream, getSynonymMapCountry(), true));

        return new TokenStreamComponents(tokenizer, stream);
    }
}