package ispy.corp.moneywzrd.investments;
//ISpy Corp

import android.util.Xml;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;



    public class StackOverflowXmlParser {
        private static final String ns = null;
       //starts by parsing article using XML pull
        public List parse(InputStream in) throws XmlPullParserException, IOException {
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(in, null);
                parser.nextTag();
                return readFeed(parser);
            } finally {
                in.close();
            }
        }
            //scans article
        private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
            List entries = new ArrayList();

            parser.require(XmlPullParser.START_TAG, ns, "rss");//cant extract these strings
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                String name = parser.getName();
                if (name.equals("channel")) {//cant extract these strings
                    entries = readChannel(parser);
                } else {
                    skip(parser);
                }
            }
            return entries;
        }

        public static class Entry {
            public final String title;
            public final String author;
            public final String link;
            public final String date;
//sets the information for specific news feed needs
            private Entry(String title, String author,String link, String date) {
                this.title = title;
                this.author = author;
                this.link = link;
                this.date = date;
            }
        }

        private List<Entry> readChannel(XmlPullParser parser) throws IOException, XmlPullParserException{
            parser.require(XmlPullParser.START_TAG, ns, "channel");//cant extract these strings
            List<Entry> entryItem = new ArrayList<>();
            while (parser.next() != XmlPullParser.END_TAG){
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("item")){
                    entryItem.add(readEntry(parser));
                }
                else{
                    skip(parser);
                }
            }
            return entryItem;
        }

        //grabs the article information and parses it into the project
        private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
            parser.require(XmlPullParser.START_TAG, ns, "item");
            String title = null;
            String author = null;
            String link = null;
            String date = null;

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if (name.equals("title")) {//cant extract these strings
                    title = readTitle(parser);
                }
                else if(name.equals("pubDate")){//cant extract these strings
                    date = readDate(parser);
                }
                else if(name.equals("sa:author_name")){//cant extract these strings
                    author = readAuthor(parser);
                }
                else if(name.equals("link")){//cant extract these strings
                    link = readLink(parser);
                }
                else {
                    skip(parser);
                }
            }
            return new Entry(title, author,link,date);
        }
        //grabs article title
        private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, ns, "title"); //cant extract this string
            String title = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "title");//cant extract this string
            return title;
        }

        //grabs internet link
        private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException{
            parser.require(XmlPullParser.START_TAG, ns, "link");//cant extract this string
            String link = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "link");//cant extract this string
            return link;
        }
        //grabs author
        private String readAuthor(XmlPullParser parser) throws IOException, XmlPullParserException{
            parser.require(XmlPullParser.START_TAG, ns, "sa:author_name");//cant extract this string
            String author = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "sa:author_name");//cant extract this string
            return author;
        }
        //grabs date
        private String readDate(XmlPullParser parser) throws IOException, XmlPullParserException{
            parser.require(XmlPullParser.START_TAG, ns, "pubDate");//cant extract this string
            String date = readText(parser);
            parser.require(XmlPullParser.END_TAG, ns, "pubDate");//cant extract this string
            return date;
        }
        //grabs the text from article
        private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
            String result = "";
            if (parser.next() == XmlPullParser.TEXT) {
                result = parser.getText();
                parser.nextTag();
            }
            return result;
        }
        //if error with getting info from article this will occur
        private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        }

}

