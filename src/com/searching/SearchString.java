package com.searching;

import com.indexing.utility.Stemmer;
import com.indexing.parser.StopStemWordHandle;

import java.util.Scanner;

/**
 * Created by rishimittal on 16/1/14.
 */
public class SearchString {

    private StopStemWordHandle stopStemWordHandle;
    private Stemmer stemmer;
    private BinarySearchFile binarySearchFile;
    private StringBuilder sb;
    public static String INDEX_FILE = "";

    public SearchString() {
        this.stopStemWordHandle = new StopStemWordHandle();
        this.stemmer = new Stemmer();
        binarySearchFile = new BinarySearchFile();
    }

    public void processString(String str){

        int p = 0;

        sb = new StringBuilder(str);

        String st[] = str.split(":");

        int stLen = st.length;

        int firstkey = 0;

        if(stLen > 1) {
            firstkey = Integer.parseInt(st[1]);

            System.out.print(firstkey);

            if (stLen > 2 ) System.out.print(",");

            for( p = 2 ; p < stLen ; p++ ){
                System.out.print(Integer.parseInt(st[p]) + firstkey);
                if ( p != st.length - 1 )System.out.print(",");
            }

           System.out.print("\n");
        }else{
            //For single Letter words
            System.out.print("\n");
        }
    }



    public static void main(String arr[]){

        String query;
        boolean allowFlag = false;
        String stemmedWord = null;
        INDEX_FILE = new String(arr[0]);
        Scanner in = new Scanner(System.in);
        int count = in.nextInt();
        in.nextLine();
        for(int m = 0 ; m < count ; m ++ ) {
        query = in.nextLine();
        //long start = System.currentTimeMillis();
        // Step 1. Input and Case Folding
        query = query.toLowerCase();
        //Create Object of this class
        SearchString searchString = new SearchString();

        //Step 2. Stop Word Removal
        if( searchString.stopStemWordHandle.getStopWords().contains(query)) {
            System.out.print("\n");
            allowFlag = false;
        }else {
            allowFlag = true;
        }

        //Step 3. Stemming
        if(allowFlag){
             char[] chars = query.toCharArray();
             searchString.stemmer.add(chars , query.length());
             searchString.stemmer.stem();
             stemmedWord = searchString.stemmer.toString();


        //Step 4. Perform a Binary Search in a File and Return the String
        String output = searchString.binarySearchFile.getSearchResults(stemmedWord);

        if(output.equals("<Empty Line>")){
            System.out.print("\n");

        }else {
            searchString.processString(output);
            //System.out.println();
        }

        //long end = System.currentTimeMillis();

        //System.out.println("Found in  " + (end - start) + "msecs");
        }
    }

    }

}
