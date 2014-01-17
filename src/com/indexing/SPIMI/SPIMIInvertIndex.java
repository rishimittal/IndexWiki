package com.indexing.SPIMI;

import com.indexing.parser.IWParser;


import java.io.*;
import java.util.*;

/**
 * Created by rishimittal on 14/1/14.
 */

public class SPIMIInvertIndex {

    private static final int BLOCK_SIZE = 25000;
    private static final int OUTPUT_BUFFER_LINES = 500;
    private static String INDEX_FILE = "/findex.idx";
    private String index_dir = null;
    private Map<String, ArrayList<Integer>> map= null;
    //private Map<String, ArrayList<Integer>> outputMap = null;
    public int block_Count = 0;
    private int block_Index = 0;
    private String[] listString;
    private int[] listIndex;
    private String DkeyName = null;
    private StringBuilder DValue = new StringBuilder();
    private StringBuilder tempKeyName = new StringBuilder();
    private StringBuilder tempValue = new StringBuilder();
    private boolean dFlag = false;
    private int firstKey = 0;

    public SPIMIInvertIndex() {
        map = new TreeMap<String, ArrayList<Integer>>();
        //outputMap = new TreeMap<String, ArrayList<Integer>>();
    }

    public boolean addTermAndWriteBlock(String token , Integer docId, int docCount) {

        boolean  var = false;

        if(token == null || docId == 0 ) return false;

        ArrayList<Integer> docList = map.get(token);

        if(docList != null){
            //add this document to the list of the documents that has this token
            int idx = docList.indexOf(docId);

            if(idx > -1){

                var = false;

            }else{

                docList.add(docId);

                var = true;
            }

        }else{

            ArrayList<Integer> documentList = new ArrayList<Integer>();

            documentList.add(docId);

            map.put(token, documentList);

            var = true;

        }

        if(var) {

            block_Count++;

        }

        if( block_Count >= BLOCK_SIZE){

            writeBlock();

            return true;
        }

        return false;
    }

    private String getBlockFile(int block_Index_Number){

        return IWParser.INDEX_DIR + "tindex_" + block_Index_Number + ".idx";

    }


    public void  writeBlock(){


        block_Count = 0;

        block_Index ++;

        //System.out.println(block_Index);

        //System.out.println(getBlockFile());
        PrintWriter pow = null;


        try {

            pow = new PrintWriter(new FileOutputStream(getBlockFile(block_Index)));

        }catch(IOException e){

            System.out.println("cannot make the output Stream");

            e.printStackTrace();
        }


        Iterator entry = map.entrySet().iterator();

        while(entry.hasNext()){

            Map.Entry thisEntry = (Map.Entry)entry.next();

            String key = (String)thisEntry.getKey();

            ArrayList<Integer> value= (ArrayList<Integer>)thisEntry.getValue();

            //if(key != "")
                //System.out.println(key);

            pow.print(key);
            for(int k = 0 ; k < value.size() ; k++ ){
                pow.print(":" + value.get(k));
            }
            pow.print("\n");
            //System.out.println(key + ":" + value);
        }


        if (pow != null) {
            pow.close();
        }
        map.clear();
    }


    //Called in the last for the merging of all the files
    // after they have been created
    //block_Index : final number of the blocks
    public void mergeSortedFiles(final Comparator<String> cmp){

        int i = 0;

        BufferedReader[] bufferedReaders = new BufferedReader[block_Index + 1];

        BufferedWriter index_file = null;

        listIndex = new int[block_Index + 1];

        listString = new String[block_Index + 1];

        PriorityQueue<FileBuffer> priorityQueue = new PriorityQueue<FileBuffer>(10,

                new Comparator<FileBuffer>() {

                    @Override

                    public int compare(FileBuffer o1, FileBuffer o2) {
                        System.out.println(o1.peek());
                        System.out.println(o2.peek());

                         return cmp.compare(o1.peek() , o2.peek());

                    }

                });

        try {

            for ( i = 1 ; i <= block_Index ; i++ ) {

                FileBuffer fb = new FileBuffer( new File( getBlockFile(i)));
                priorityQueue.add(fb);
            }

            index_file = new BufferedWriter( new FileWriter(IWParser.INDEX_DIR + INDEX_FILE));

        }catch(IOException iox) {

            System.out.println("IO error");

            iox.printStackTrace();
        }

        int rowCounter = 0;

        try{

            while (priorityQueue.size() > 0 ) {

                FileBuffer fbb = priorityQueue.poll();

                String rv = fbb.pop();

                String fv = addStrings(rv);

                if(!fv.equals("a")) {

                    //System.out.println("Writing : " + fv);
                    index_file.write(fv);

                    index_file.newLine();
                }
                ++rowCounter;

                if(fbb.empty()){

                    fbb.fbr.close();

                    fbb.temp_file.delete();

                }else{

                    priorityQueue.add(fbb);
                }
            }

        }catch(IOException ix){

            System.out.println("IO error");

            ix.printStackTrace();

        }finally {

            try {

                index_file.close();

            } catch (IOException e) {

                e.printStackTrace();

            }
        }
    }

    public String addStrings(String op){

        int p = 0;

        String[] keyName = op.split(":");

        int keyLength = keyName.length;

        if ( DkeyName == null && DValue.length() == 0 ) {

            DkeyName = new String(keyName[0]);

            DValue = new StringBuilder();

            if (keyName[1] != null) {

                firstKey = Integer.parseInt(keyName[1]);

                DValue.append(":").append(firstKey);
                //if(keyLength != 1 ) DValue.append(",");
            }
            for( p = 2 ; p < keyLength ; p++ ){
                if ( keyName[p] != null && keyName[p].length() > 0 ){
                      //DValue.append(keyName[p]);
                    DValue.append(":").append(Integer.parseInt(keyName[p]) - firstKey);
                    //if ( p < keyLength - 1 ) DValue.append(":");
                }
            }
        }
        else if(DkeyName.equals(keyName[0])){ // if same as old one

            for( p = 1 ; p < keyLength ; p++ ){

                if ( keyName[p] != null && keyName[p].length() > 0 ) {
                      //DValue.append(keyName[p]).append(":");
                    DValue.append(":").append(Integer.parseInt(keyName[p]) - firstKey);
                    //if ( p < keyLength - 1 ) DValue.append(":");
                }
            }
        }else{                              //Different than previous

            //tempKeyName = DkeyName;// temp = old --alternative
            tempKeyName.setLength(0);

            tempKeyName.append(DkeyName);
            //tempValue = new String(DValue);   -alternative
            tempValue.setLength(0);

            tempValue.append(DValue);

            DkeyName = new String(keyName[0]);// old = new
            // return temp

            DValue = new StringBuilder();

            if (keyName[1] != null) {

                firstKey = Integer.parseInt(keyName[1]);    //update first key

                DValue.append(":").append(firstKey);
                //if(keyLength != 1 ) DValue.append(",");
            }
            for( p = 2 ; p < keyLength ; p++ ){
                if ( keyName[p] != null && keyName[p].length() > 0 ) {
                    //DValue.append(keyName[p]).append(":");
                    DValue.append(":").append(Integer.parseInt(keyName[p]) - firstKey);
                    //if ( p < keyLength - 1 ) DValue.append(":");
                }
            }

            return tempKeyName.append(tempValue).toString() ;
        }
        return "a";
    }
}

class FileBuffer {

    public static int CH_BUFFER_SIZE = 2048;
    public BufferedReader fbr = null;
    public File temp_file;
    private String cache = null;
    private boolean empty = false;


    public FileBuffer(File f ) throws IOException {

        temp_file = f;

        fbr = new BufferedReader( new FileReader(f)); //, CH_BUFFER_SIZE);

        reload();
    }

    public boolean empty(){

        return empty;
    }

    private void reload() throws IOException {

        boolean cflag = false;

        try{

            if((this.cache = fbr.readLine()) == null ){
                empty = true;
                cache = null;
            }else{

                empty = false;
            }
        }catch (EOFException ex){
            empty = true;
            cache = null;
        }
    }

    public void close() throws IOException {
        //Closes the File descriptor
        fbr.close();
    }

    public String peek(){


        if(empty) return null;
        //Returns the particular cache , i.e
        // first line of the requested file descriptor
        return cache.toString();
    }

    public String pop() throws IOException {

        String answer = peek();

        //System.out.println(answer);
        int p = 0;

        reload();

        return answer;
    }
}