package logic.words;

import java.util.HashMap;
import java.util.Map;

public class WordSet {
	private Map<String, Word> synonyms = new HashMap<String, Word>();
	
	public void addWord(Word word){
		synonyms.put(word.value, word);
	}
	public void addSynonym(Word word, String synonym){
		synonyms.put(synonym, word);
	}
	public Word getMeaning(String word){
		return synonyms.get(word);
	}
}
