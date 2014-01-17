package com.searching;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Created by rishimittal on 16/1/14.
 */
public class BinarySearchFile {

    private static String INDEX_FILE_NAME = "/findex.idx";

    public String getSearchResults(String queryString){

        RandomAccessFile randomAccessFile = null;

        if(queryString.length() < 2) {

            return "<Empty Line>";
        }

        try {

            randomAccessFile = new RandomAccessFile(SearchString.INDEX_FILE + INDEX_FILE_NAME,"r");

        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        try {

            randomAccessFile.seek(0);

            String line = randomAccessFile.readLine();

            if(compareStrings(line, queryString) == 0){

                return line;
            }

            long begin = 0;

            long end = randomAccessFile.length();

            while (begin <= end){

                long mid = begin + (end - begin ) / 2;

                randomAccessFile.seek(mid);

                randomAccessFile.readLine();

                line = randomAccessFile.readLine();

                if(compareStrings(line, queryString) == 0){

                    return line;

                }else if(compareStrings(line, queryString) == 1 ) {

                    end = mid - 1;

                }else{

                    begin = mid + 1;
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return "<Empty Line>";
    }

    public int compareStrings(String line, String target){

        String[] keys = null;

        if(line != null) {
            keys = line.split(":");


            if(keys[0] == null || keys[0].compareTo(target) == 0){
                return 0;
            }

            if(keys[0] == null || keys[0].compareTo(target) > 0 ){
                return 1;
            }

            if(keys[0] == null || keys[0].compareTo(target) < 0 ){
                return 2;
            }
        }
        return 3;
    }
}
