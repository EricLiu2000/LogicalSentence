package run;

import sentence.LogicalSentence;

public class Run {

	public static void main(String[] args) {
		LogicalSentence sentence = new LogicalSentence("~(pvq)", 2);
		LogicalSentence sentence2 = new LogicalSentence("~p^~q", 2);
		System.out.println(sentence.equivalent(sentence2));
	}

}