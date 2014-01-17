package com.indexing.parser;

import com.indexing.SPIMI.SPIMIInvertIndex;
import com.indexing.utility.CharacterSet;
import com.indexing.utility.Page;
import com.indexing.utility.Stemmer;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by rishimittal on 11/1/14.
 */
public class IWParser extends DefaultHandler{

        private String xmlFileName;
        private Page tempPage;
        private StringBuilder tagContent;
        private TextParser textParser;
        private boolean ifPage = false;
        private boolean ifRev = false;
        private boolean ifCon = false;
        private static int pageCount = 0;
        public static int str_count = 0;
        private SPIMIInvertIndex spimi = null;
        private StopStemWordHandle stp;
        private CharacterSet cset = null;
        private Stemmer stem = null;
        public static String INDEX_DIR = "";
        public static String INPUT_FILE_NAME = "";

    public IWParser(String xmlFileName) {
        this.xmlFileName = xmlFileName;
        tagContent = new StringBuilder();
        textParser = new TextParser();
        cset = new CharacterSet();
        spimi = new SPIMIInvertIndex();
        stp = new StopStemWordHandle();
        stem = new Stemmer();
    }

    public CharacterSet getCset() {
        return cset;
    }

    public StopStemWordHandle getStp() {
        return stp;
    }

    public SPIMIInvertIndex getSpimi() {
        return spimi;
    }

    public Stemmer getStem() {
        return stem;
    }

    private void parseDocument(){

        textParser.textParserInvoked(this);
        stp.stopStemWordHandleInvoked(this);

        SAXParserFactory factory = SAXParserFactory.newInstance();
        try{

            SAXParser parser = factory.newSAXParser();
            File xmlFile = new File(xmlFileName);
            //System.out.println(xmlFileName);
            parser.parse(xmlFile, this);

            Comparator<String> comparator = new Comparator<String>() {
                public int compare(String r1, String r2){
                    return r1.compareTo(r2);}};
            spimi.mergeSortedFiles(comparator);
            //System.out.println(spimi.block_Count);

        }catch(ParserConfigurationException e){
            System.out.println("ParserConfig error");
            e.printStackTrace();
        }catch (SAXException e){
            System.out.println("SAX Exception : xml not well formed");
            e.printStackTrace();
        }catch(IOException e){
            System.out.println("IO Error");
            e.printStackTrace();
        }
    }

    @Override
    public void startElement(String uri, String localname, String elementName, Attributes attributes) {

        if(elementName.equalsIgnoreCase("page")){
                tempPage = new Page();
                ifPage = true;
        }

        if(elementName.equalsIgnoreCase("revision")){
                ifRev = true;
        }

        if(elementName.equalsIgnoreCase("contributor")){
                ifCon = true;
        }


    }

    @Override
    public void endElement(String uri, String localname, String elementName) {

            if(elementName.equalsIgnoreCase("page")){
                //Called for every Page
                //storeDataInList(tempPage);
                textParser.categorizeText(tempPage.getId().trim(),tempPage.getTitle().trim().toLowerCase(), tempPage.getRev_text().trim().toLowerCase());
                pageCount++;
                ifPage = false;
            }

            if(elementName.equalsIgnoreCase("revision")){
                ifRev = false;
            }

            if(elementName.equalsIgnoreCase("contributor")){
                ifCon = false;
            }

            //page title Content
            if(elementName.equalsIgnoreCase("title")){
                tempPage.setTitle(tagContent.toString());
            }
            //page id
            if(elementName.equalsIgnoreCase("id") && ifPage && !ifRev && !ifCon){
                //System.out.println(tagContent.toString());
                tempPage.setId(tagContent.toString());
            }

            //contributor username content
            if(elementName.equalsIgnoreCase("username")){
                tempPage.setRev_con_username(tagContent.toString());
            }

            // revision minor content
            if(elementName.equalsIgnoreCase("minor")){
                tempPage.setRev_minor(tagContent.toString());
            }

            //revision comment content
            if(elementName.equalsIgnoreCase("comment")){
                //System.out.println(tagContent);
               tempPage.setRev_comment(tagContent.toString());
            }

            if(elementName.equalsIgnoreCase("text")){
                tempPage.setRev_text(tagContent.toString());
            }
        tagContent.setLength(0);
    }
    /*
    private void storeDataInList(Page tempPage) {

        textParser.categorizeText(tempPage.getId().trim(),tempPage.getTitle().trim().toLowerCase(), tempPage.getRev_text().trim().toLowerCase());

    }*/

    @Override
    public void characters(char[] chars, int start, int length) {

        tagContent.append(new String(chars, start, length));

    }

    public static void main(String arr[]){

        //long start = System.currentTimeMillis();

        //inputfilename is the absolute path of the xaml file to be indexed
        INPUT_FILE_NAME = new String(arr[0]);
        //path of the directory wherr index files will be generated.
        INDEX_DIR = new String(arr[1]);

        IWParser parser = new IWParser(INPUT_FILE_NAME);
        parser.parseDocument();
        //new SPIMIInvertIndex().printMap();
        //System.out.println("PageCount  :" + pageCount);
        //System.out.println(str_count);
        //long end = System.currentTimeMillis();
        //System.out.println("Time taken to write is " + (end - start) + "msecs");

    }
}
