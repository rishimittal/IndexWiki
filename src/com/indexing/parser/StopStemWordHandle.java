package com.indexing.parser;

import com.indexing.SPIMI.SPIMIInvertIndex;
import com.indexing.utility.Stemmer;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by rishimittal on 13/1/14.
 */
public class StopStemWordHandle {

    private Set stopWords = null;
    private int docCount = 0;
    private int strCount = 0;
    private Stemmer stem = null;
    private SPIMIInvertIndex spimi = null;

    public StopStemWordHandle() {

        stopWords = new HashSet();
        createStopHashSet();
    }

    public Set getStopWords() {
        return stopWords;
    }

    public void setDocCount(int count){
        this.docCount = count;
    }

    public void createStopHashSet(){
        stopWords.add("a");
        stopWords.add("b");
        stopWords.add("c");
        stopWords.add("d");
        stopWords.add("e");
        stopWords.add("f");
        stopWords.add("g");
        stopWords.add("h");
        stopWords.add("i");
        stopWords.add("j");
        stopWords.add("k");
        stopWords.add("l");
        stopWords.add("m");
        stopWords.add("n");
        stopWords.add("o");
        stopWords.add("p");
        stopWords.add("q");
        stopWords.add("r");
        stopWords.add("s");
        stopWords.add("t");
        stopWords.add("u");
        stopWords.add("v");
        stopWords.add("w");
        stopWords.add("x");
        stopWords.add("y");
        stopWords.add("z");
        stopWords.add("able");
        stopWords.add("about");
        stopWords.add("across");
        stopWords.add("after");
        stopWords.add("all");
        stopWords.add("almost");
        stopWords.add("also");
        stopWords.add("am");
        stopWords.add("among");
        stopWords.add("an");
        stopWords.add("and");
        stopWords.add("any");
        stopWords.add("are");
        stopWords.add("as");
        stopWords.add("at");
        stopWords.add("be");
        stopWords.add("because");
        stopWords.add("been");
        stopWords.add("but");
        stopWords.add("by");
        stopWords.add("can");
        stopWords.add("cannot");
        stopWords.add("category");
        stopWords.add("could");
        stopWords.add("dear");
        stopWords.add("did");
        stopWords.add("do");
        stopWords.add("does");
        stopWords.add("either");
        stopWords.add("else");
        stopWords.add("ever");
        stopWords.add("every");
        stopWords.add("for");
        stopWords.add("from");
        stopWords.add("get");
        stopWords.add("got");
        stopWords.add("had");
        stopWords.add("has");
        stopWords.add("have");
        stopWords.add("he");
        stopWords.add("her");
        stopWords.add("hers");
        stopWords.add("him");
        stopWords.add("his");
        stopWords.add("how");
        stopWords.add("however");
        stopWords.add("if");
        stopWords.add("in");
        stopWords.add("into");
        stopWords.add("is");
        stopWords.add("it");
        stopWords.add("its");
        stopWords.add("just");
        stopWords.add("least");
        stopWords.add("let");
        stopWords.add("like");
        stopWords.add("likely");
        stopWords.add("may");
        stopWords.add("me");
        stopWords.add("might");
        stopWords.add("most");
        stopWords.add("must");
        stopWords.add("my");
        stopWords.add("neither");
        stopWords.add("no");
        stopWords.add("nor");
        stopWords.add("not");
        stopWords.add("of");
        stopWords.add("off");
        stopWords.add("often");
        stopWords.add("on");
        stopWords.add("only");
        stopWords.add("or");
        stopWords.add("other");
        stopWords.add("our");
        stopWords.add("own");
        stopWords.add("rather");
        stopWords.add("references");
        stopWords.add("reflist");
        stopWords.add("said");
        stopWords.add("say");
        stopWords.add("says");
        stopWords.add("she");
        stopWords.add("should");
        stopWords.add("since");
        stopWords.add("so");
        stopWords.add("some");
        stopWords.add("than");
        stopWords.add("that");
        stopWords.add("the");
        stopWords.add("their");
        stopWords.add("them");
        stopWords.add("then");
        stopWords.add("there");
        stopWords.add("these");
        stopWords.add("they");
        stopWords.add("this");
        stopWords.add("tis");
        stopWords.add("to");
        stopWords.add("too");
        stopWords.add("twas");
        stopWords.add("us");
        stopWords.add("wants");
        stopWords.add("was");
        stopWords.add("we");
        stopWords.add("were");
        stopWords.add("what");
        stopWords.add("when");
        stopWords.add("where");
        stopWords.add("which");
        stopWords.add("while");
        stopWords.add("who");
        stopWords.add("whom");
        stopWords.add("why");
        stopWords.add("will");
        stopWords.add("with");
        stopWords.add("would");
        stopWords.add("yet");
        stopWords.add("you");
        stopWords.add("your");
        stopWords.add("lt");
        stopWords.add("gt");
        stopWords.add("nbsp");
    }

    public void processIndex(StringBuilder input, int docId){


        int input_len = input.length();

        char [] chars = new char[input_len];

        input.getChars(0, input_len,chars,0);

        //if(docId == 113700) {
        //    System.out.println(input.toString());
        //}
        // Stop Word Removal
        if(!stopWords.contains(input.toString())){

            //Stemming
            stem.add(chars, input_len);

            stem.stem();
            //IWParser.str_count++;
            String u = stem.toString();
            // Creating Index file
            // 1. Term frequency and indexing
            // 2. posting lists
            // 3. Storing on Secondary disk
            //System.out.println(u);


            if(stem.getResultLength() > 1)
                spimi.addTermAndWriteBlock(u, docId , docCount);

        }
    }

    public void stopStemWordHandleInvoked(IWParser iwp){
        stem = iwp.getStem();
        spimi = iwp.getSpimi();
    }

}
