package com.indexing.parser;

import com.indexing.utility.CharacterSet;

/**
 * Created by rishimittal on 12/1/14.
 */
public class TextParser {

    StringBuilder title;
    StringBuilder infobox;
    StringBuilder bodyText;
    StringBuilder references;
    private StringBuilder buf_string;
    private StringBuilder final_string;
    private boolean ref_bool;
    private int ref_count;
    private int docId;
    private static int doc_current_count = 0;
    private StopStemWordHandle stp = null;
    private CharacterSet cset = null;



    public void categorizeText(String pageId, String title, String text){

        //Logic to separate infobox, bodytext, references , category, external links.
        int i;

        ref_bool = false;
        ref_count = 0;
        int textLength = text.length();
        boolean infoboxTag = false ;
        boolean bodyTextTag = false;
        boolean referTextTag = false;
        boolean infoboxtagFound = true;
        boolean extLinkTextTag = false;
        boolean catTag = false;
        boolean flag = false;
        int openCount = 2; // for infobox
        int openSqCount = 2; // for category
        boolean allowTag = true;

        buf_string = new StringBuilder();
        final_string = new StringBuilder();

        try {
            doc_current_count++;
            stp.setDocCount(doc_current_count);
            docId = Integer.parseInt(pageId);
        }catch (NumberFormatException nfe){
            System.out.println("Wrong Format of DocId");
        }

        //Processing title : Add them to buffer method
        processTitle(title);
        // Parsing loop iterated over the entire Characters
        // sequentially.
        for(i = 0 ; i < textLength ; i++ ){

            char ch = text.charAt(i);
            /*
            // infoboxtagFound is set to true initially until
            // closing braces are not found for the infobox
            if ( infoboxtagFound ) {
                //System.out.println("---Infobox begins-----");
                //System.out.println("called");
                if(text.charAt(i - 9) == '{' && text.charAt(i - 8) == '{' ) {

                    if(text.charAt(i - 7) == 'i' && text.charAt(i - 6) == 'n' && text.charAt(i - 5) == 'f' && text.charAt(i - 4) == 'o' && text.charAt(i - 3 ) == 'b' && text.charAt(i - 2) == 'o' && text.charAt(i - 1) == 'x'){

                        //Setting the flag for the infobox to begin.
                        infoboxTag = true;

                        infoboxtagFound = true;
                    }
                }
            }

            if(infoboxTag && openCount > 0){
                if(ch == '{') openCount++;
                if(ch == '}') openCount--;
                //Assumption asssociated 1.1
                //InfoBox Separation and processing
                //System.out.println(ch);
                processInfobox(ch);
            }

            if(!infoboxTag) {

                if( bodyTextTag && !referTextTag){

                    if(text.charAt(i) == '=' && text.charAt(i + 1) == '=' && !referTextTag) {
                        if(i < textLength - 14 && text.charAt(i + 2) == 'r' && text.charAt(i + 3) == 'e' && text.charAt(i + 4) == 'f'  && text.charAt(i + 5) == 'e' && text.charAt(i + 6) == 'r' && text.charAt(i + 7) == 'e' && text.charAt(i + 8) == 'n' && text.charAt(i + 9) == 'c' && text.charAt(i + 10) == 'e' && text.charAt(i + 11) == 's' && text.charAt(i + 12) == '=' && text.charAt(i + 13) == '=' ){
                            //System.out.println("References begins");
                            referTextTag = true;
                            bodyTextTag = false;
                        }
                    }

                    if(bodyTextTag) {
                        //Body Text separation and processing
                        processBodyText(ch);
                    }
                }

                if( referTextTag ){
                    if(text.charAt(i) == '=' && text.charAt(i + 1) == '=' ) {
                        if ( i < textLength - 18 && text.charAt(i + 2) == 'e' && text.charAt(i + 3) == 'x' && text.charAt(i + 4) == 't' && text.charAt(i + 5) == 'e' && text.charAt(i + 6) == 'r' && text.charAt(i + 7) == 'n' && text.charAt(i + 8) == 'a' && text.charAt(i + 9) == 'l' && text.charAt(i + 10) == ' ' && text.charAt(i + 11) == 'l' && text.charAt(i + 12) == 'i' && text.charAt(i + 13) == 'n' && text.charAt(i + 14) == 'k' && text.charAt(i + 15) == 's' && text.charAt(i + 16) == '=' && text.charAt(i + 17) == '='  ) {
                            referTextTag = false;
                            extLinkTextTag = true;
                        }
                    }

                    if(referTextTag) {
                        //References Text seperation and processing
                        processReferencesText(ch);
                    }
                }

                if (!bodyTextTag && !referTextTag){
                    if(text.charAt(i) == '[' && text.charAt(i + 1) == '[' ) {
                        if ( i < textLength - 11 && text.charAt(i + 2) == 'c' && text.charAt(i + 3) == 'a' && text.charAt(i + 4) == 't' && text.charAt(i + 5) == 'e' && text.charAt(i + 6) == 'g' && text.charAt(i + 7) == 'o' && text.charAt(i + 8) == 'r' && text.charAt(i + 9) == 'y' && text.charAt(i + 10) == ':' ) {
                            openSqCount = 2;
                            catTag = true;
                            extLinkTextTag = false;
                        }
                    }

                    if(extLinkTextTag) {
                        //System.out.println(ch);
                        //External Links Text seperation and processing
                        processExternalLinkText(ch);
                    }
                }

                if(catTag ){
                    if(openSqCount == 2){
                        //System.out.println(ch);
                        processCategories(ch);
                    }
                    if (ch == ']') openSqCount--;
                    if(openSqCount == 0 ){
                        catTag = false;
                    }
                }
            }

            if(openCount <= 0 && infoboxTag){
                //System.out.println("---Infobox close-----");
                bodyTextTag = true;
                infoboxTag = false;
                infoboxtagFound = false;
            }*/

            processInfobox(ch);
        }

    }

    public void processTitle(String title){

        boolean isLetter = false;
        int textLength = title.length();
        for (int i = 0 ; i < textLength ; i++){
            char ch = title.charAt(i);

            if(cset.isChar.contains(ch)){
                isLetter = true;
            }else{
                isLetter = false;
            }

            if(isLetter){
               // System.out.println(buf_string);
                buf_string.append(ch);
            }

            if( (!isLetter &&  buf_string.length() > 0) || ( i == textLength - 1) ){
                //Printing keywords from title properly
                //System.out.println(buf_string);
                stp.processIndex(buf_string, docId);
                buf_string.setLength(0);
            }
        }
    }

    public void processInfobox(char ch){

        boolean isLetter = false;

        if(cset.isChar.contains(ch) ){
            isLetter =  true;
        }

        if(isLetter){
            final_string.append(ch);
        }

        if(final_string.length() > 0 && !isLetter){
            //flag = false;
            //if(final_string.length() != 0) {
            //System.out.println(final_string.toString().toLowerCase());
            stp.processIndex(final_string, docId);
            final_string.setLength(0);
            //}
        }
    }
    /*
    public void processBodyText(char ch){
        boolean isLetter = false;

        if(cset.isChar.contains(ch) ){
            isLetter =  true;
        }

        if(isLetter) {
            final_string.append(ch);
        }

        if(final_string.length() > 0 && !isLetter){
            //flag = false;
            //if(final_string.length() != 0) {
            //System.out.println(final_string.toString().toLowerCase());
            //stp.processIndex(final_string, docId);
            final_string.setLength(0);
            //}
        }
    }

    public void processReferencesText(char ch){
        boolean isLetter = false;

        if(cset.isChar.contains(ch)){
            isLetter =  true;
        }

        if(isLetter){
            final_string.append(ch);
        }

        if(final_string.length() > 0 && !isLetter || ch == '_'){
            //flag = false;
            //if(!final_string.toString().equals("references") && !final_string.toString().equals("reflist")){
                //System.out.println(final_string);
                stp.processIndex(final_string, docId);
            //}
            final_string.setLength(0);
            //}
        }
    }

    public void processExternalLinkText(char ch) {

        boolean isLetter = false;

        if(cset.isChar.contains(ch)){
            isLetter =  true;
        }

        if(isLetter){
            final_string.append(ch);
            //System.out.println(final_string);
        }

        //System.out.println(ch);
        if(final_string.length() > 0 && !isLetter){
            //if(final_string.length() != 0) {
            //System.out.println(final_string.toString().toLowerCase());
            stp.processIndex(final_string, docId);
            final_string.setLength(0);
            //}
        }
    }

    public void processCategories(char ch){
        boolean isLetter = false;

        if(cset.isChar.contains(ch) ) {
            isLetter =  true;
        }

        if(isLetter){
            final_string.append(ch);
            //System.out.println(final_string);
        }

        //System.out.println(ch);
        if(final_string.length() > 0 && !isLetter){
            //if(final_string.length() != 0) {
            //if(!final_string.toString().equals("category")) {
                //System.out.println(final_string);
                stp.processIndex(final_string, docId);
            //}
            final_string.setLength(0);
            //}
        }

    }*/

    public void textParserInvoked( IWParser iwp){
        stp = iwp.getStp();
        cset =  iwp.getCset();
    }
}
