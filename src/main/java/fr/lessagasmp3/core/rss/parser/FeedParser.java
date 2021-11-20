package fr.lessagasmp3.core.rss.parser;

import fr.lessagasmp3.core.common.constant.HttpProxy;
import fr.lessagasmp3.core.common.constant.Strings;
import fr.lessagasmp3.core.rss.entity.RssMessage;
import fr.lessagasmp3.core.rss.model.Feed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FeedParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeedParser.class);

    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String LANGUAGE = "language";
    private static final String COPYRIGHT = "copyright";
    private static final String LINK = "link";
    private static final String AUTHOR = "author";
    private static final String ITEM = "item";
    private static final String PUB_DATE = "pubDate";
    private static final String GUID = "guid";

    private static final DateFormat DF = new SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

    private final URL url;

    public FeedParser(String feedUrl) {
        LOGGER.debug(DF.format(new Date()));
        try {
            this.url = new URL(feedUrl);
        } catch (MalformedURLException e) {
            LOGGER.error(e.getLocalizedMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public Feed readFeed() {
        Feed feed = null;
        try {
            boolean isFeedHeader = true;
            String description = Strings.EMPTY;
            String title = Strings.EMPTY;
            String link = Strings.EMPTY;
            String language = Strings.EMPTY;
            String copyright = Strings.EMPTY;
            String author = Strings.EMPTY;
            String pubdate = Strings.EMPTY;
            String guid = Strings.EMPTY;

            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            inputFactory.setProperty(XMLInputFactory.IS_COALESCING, true);
            URLConnection openConnection;
            InputStream in;
            try {
                if(HttpProxy.HTTPS_HOST.equals(Strings.EMPTY)) {
                    openConnection = url.openConnection();
                } else {
                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(HttpProxy.HTTPS_HOST, HttpProxy.HTTPS_PORT));
                    openConnection = url.openConnection(proxy);
                }
                in = openConnection.getInputStream();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in, openConnection.getContentEncoding());
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName().getLocalPart();
                    switch (localPart) {
                        case ITEM -> {
                            if (isFeedHeader) {
                                isFeedHeader = false;
                                feed = new Feed(title, link, description, language, copyright, pubdate);
                            }
                            eventReader.nextEvent();
                        }
                        case TITLE -> title = getCharacterData(eventReader);
                        case DESCRIPTION -> description = getCharacterData(eventReader);
                        case LINK -> link = getCharacterData(eventReader);
                        case GUID -> guid = getCharacterData(eventReader);
                        case LANGUAGE -> language = getCharacterData(eventReader);
                        case AUTHOR -> author = getCharacterData(eventReader);
                        case PUB_DATE -> pubdate = getCharacterData(eventReader);
                        case COPYRIGHT -> copyright = getCharacterData(eventReader);
                    }
                } else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart().equals(ITEM)) {
                        RssMessage message = new RssMessage();
                        message.setAuthor(author);
                        message.setDescription(description);
                        message.setGuid(guid);
                        message.setLink(link);
                        message.setTitle(title);
                        message.setPubdate(DF.parse(pubdate));
                        if (feed != null) {
                            feed.getEntries().add(message);
                        }
                        eventReader.nextEvent();
                    }
                }
            }
        } catch (XMLStreamException | ParseException e) {
            throw new RuntimeException(e);
        }
        return feed;
    }

    private String getCharacterData(XMLEventReader eventReader)
            throws XMLStreamException {
        String result = Strings.EMPTY;
        XMLEvent event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }

}
