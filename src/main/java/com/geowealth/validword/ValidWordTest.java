package com.geowealth.validword;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is the class from which the program is triggered.
 */
public class ValidWordTest {

	public static void main(String[] args) {

		try {

			ValidWord validWord = new ValidWordTest().new ValidWord();

			String[] allWords = validWord.loadAllWords();

			if (allWords != null && allWords.length != 0) {

				List<String> validWords = validWord.findValidWords(allWords);
				System.out.println(validWords.toString());
				System.out.println(validWords.size());
			} else {

				System.out.println("Nothing is loaded!");
			}

		} catch (IOException e) {
			System.out.println("IO Exception occures!");
		}
	}

	/**
	 * This is the class which contains all methods needed to perform.
	 */
	private class ValidWord {

		/**
		 * The method loads data from a particular file.
		 * 
		 * @return
		 * @throws IOException
		 */
		public String[] loadAllWords() throws IOException {

			URL wordsUrl = new URL(
					"https://raw.githubusercontent.com/nikiiv/JavaCodingTestOne/master/scrabble-words.txt");

			try (BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(wordsUrl.openConnection().getInputStream()))) {

				String[] allWords = bufferedReader.lines().sorted().toArray(String[]::new);

				return allWords;
			}

		}

		/**
		 * The method searches for all the valid words.
		 * 
		 * @param allWords
		 * @return
		 */
		public List<String> findValidWords(String[] allWords) {

			List<String> validWords = new ArrayList<String>();

			String[] nineCharacterWords = Arrays.stream(allWords).filter(word -> word.length() == 9)
					.toArray(String[]::new);

			if (nineCharacterWords != null && nineCharacterWords.length != 0) {

				StringBuilder stringBuilder = new StringBuilder();

				for (String word : nineCharacterWords) {

					if (isValidWord(word, allWords, stringBuilder)) {
						validWords.add(word);
					}
				}
			}

			return validWords;
		}

		/**
		 * The method checks if a particular word is a valid one.
		 * 
		 * @param wordToBeChecked
		 * @param allWords
		 * @param stringBuilder
		 * @return
		 */
		private boolean isValidWord(String wordToBeChecked, String[] allWords, StringBuilder stringBuilder) {

			for (int i = 0; i < wordToBeChecked.length(); i++) {

				stringBuilder.append(wordToBeChecked);
				stringBuilder.deleteCharAt(i);
				String newWord = stringBuilder.toString();
				stringBuilder.setLength(0);

				if (newWord.length() == 1 && (newWord.equals("A") || newWord.equals("I"))) {
					return true;
				}

				int index = Arrays.binarySearch(allWords, newWord);

				if (index < 0) {
					continue;
				} else {
					return isValidWord(newWord, allWords, stringBuilder);
				}
			}

			return false;
		}
	}
}
