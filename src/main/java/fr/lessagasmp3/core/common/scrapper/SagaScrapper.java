package fr.lessagasmp3.core.common.scrapper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import fr.lessagasmp3.core.category.entity.Category;
import fr.lessagasmp3.core.category.repository.CategoryRepository;
import fr.lessagasmp3.core.common.constant.EventLogName;
import fr.lessagasmp3.core.common.constant.HttpProxy;
import fr.lessagasmp3.core.creator.entity.Creator;
import fr.lessagasmp3.core.creator.repository.CreatorRepository;
import fr.lessagasmp3.core.eventlog.entity.EventLog;
import fr.lessagasmp3.core.eventlog.service.EventLogService;
import fr.lessagasmp3.core.saga.entity.Saga;
import fr.lessagasmp3.core.saga.repository.SagaRepository;
import fr.lessagasmp3.core.saga.service.SagaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.UrlValidator;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class SagaScrapper {

    private static final String[] SCHEMES = {"http","https"};
    private static final UrlValidator URL_VALIDATOR = new UrlValidator(SCHEMES);

    private static final short COLUMN_TITLE_INDEX = 1;
    private static final short COLUMN_AUTHORS_INDEX = 2;
    private static final short COLUMN_CATEGORIES_INDEX = 3;
    private static final short COLUMN_LEVELS_INDEX = 4;
    private static final short COLUMN_REVIEWS_INDEX = 5;
    private static final short COLUMN_BRAVOS_INDEX = 6;

    @Autowired
    private EventLogService eventLogService;

    @Autowired
    private CreatorRepository creatorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SagaRepository sagaRepository;

    @Autowired
    private SagaService sagaService;

    public void scrap() {
        log.info("Scrap sagas started");
        eventLogService.insert(new EventLog(EventLogName.SYNC_SAGAS_START));
        eventLogService.rotate(EventLogName.SYNC_SAGAS_START);
        int nbRows = 50;
        short nbPage = 0;

        String searchUrl;
        List<?> elements;
        HtmlPage page;
        HtmlTableRow row;
        HtmlTableCell cell;
        HtmlAnchor anchor;
        HtmlSpan span;
        HtmlImage image;
        Saga saga;
        Creator creator;
        Category category;

        WebClient client = new WebClient();
        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);
        if(HttpProxy.proxyConfig != null) {
            client.getOptions().setProxyConfig(HttpProxy.proxyConfig);
        }

        while (nbRows > 0) {
            searchUrl = "https://forum.netophonix.com/sagaslist.php?mode=titres&order=ASC&start=" + nbPage;
            try {
                log.debug("Scrap url : {}", searchUrl);
                page = client.getPage(searchUrl);

                // Get HTML rows
                List<?> rows = page.getByXPath("//tr[@class='row1'or @class='row2']");

                nbRows = rows.size();
                for (int k = 0; k < nbRows; k++) {
                    row = (HtmlTableRow) rows.get(k);
                    saga = new Saga();

                    // First column
                    cell = row.getCell(COLUMN_TITLE_INDEX);
                    if (cell.getChildElementCount() > 1) {
                        elements = cell.getByXPath("a[@class='topictitle']");
                        if (elements.size() == 1) {
                            anchor = (HtmlAnchor) elements.get(0);
                            saga.setTitle(cleanString(anchor.getFirstChild().getTextContent()));
                            saga = Saga.fromModel(sagaService.findOrCreate(saga.getTitle()));

                            RestTemplate restTemplate = new RestTemplate();
                            String result = null;
                            try {
                                log.debug("URL check : {}", anchor.getHrefAttribute());
                                if(!URL_VALIDATOR.isValid(anchor.getHrefAttribute())) {
                                    throw new HttpClientErrorException(HttpStatus.NOT_FOUND);
                                }
                                result = restTemplate.getForObject(anchor.getHrefAttribute(), String.class);
                            } catch(Exception e) {
                                log.error("URL check failed : {}", anchor.getHrefAttribute());
                            }
                            if(result != null) {
                                saga.setUrl(anchor.getHrefAttribute());
                            }
                        }
                    } else {
                        saga.setTitle(cleanString(cell.getTextContent()));
                    }
                    elements = cell.getByXPath("a[not(@class='topictitle') and not(@class='createnewpage')]");
                    if (elements.size() == 1) {
                        anchor = (HtmlAnchor) elements.get(0);
                        saga.setUrlWiki(anchor.getHrefAttribute());
                    }

                    // Second column
                    cell = row.getCell(COLUMN_AUTHORS_INDEX);
                    String authorsString = cell.getFirstChild().getTextContent();
                    String[] authorsTab = authorsString.split("(,\\s*)|(\\set\\s)|(\\s&\\s)");
                    for (String s : authorsTab) {
                        creator = creatorRepository.findByNameIgnoreCase(s);
                        if (creator == null) {
                            creator = new Creator();
                            creator.setName(s);
                        }
                        creator.setNbSagas(creator.getNbSagas() + 1);
                        creator = creatorRepository.save(creator);
                        saga.getAuthors().add(creator);
                    }

                    // Third column
                    cell = row.getCell(COLUMN_CATEGORIES_INDEX);
                    String categoriesString = cell.getFirstChild().getTextContent();
                    String[] categoriesTab = categoriesString.split(",\\s");
                    for (String s : categoriesTab) {
                        category = categoryRepository.findByNameIgnoreCase(s);
                        if (category == null) {
                            category = new Category();
                            category.setName(s);
                        }
                        category.setNbSagas(category.getNbSagas() + 1);
                        category = categoryRepository.save(category);
                        saga.getCategories().add(category);
                    }

                    // Fourth column
                    cell = row.getCell(COLUMN_LEVELS_INDEX);
                    elements = cell.getFirstChild().getByXPath("img");
                    if (elements.size() == 1) {
                        image = (HtmlImage) elements.get(0);
                        List<NameValuePair> paramUrlPicLevel = URLEncodedUtils.parse(new URI(image.getSrcAttribute()), StandardCharsets.UTF_8);
                        if (!image.getSrcAttribute().equals("images/sagaliste-novote.gif")) {
                            saga.setLevelArt(Integer.parseInt(paramUrlPicLevel.stream().filter(it -> it.getName().equals("art")).findFirst().get().getValue()));
                            saga.setLevelTech(Integer.parseInt(paramUrlPicLevel.stream().filter(it -> it.getName().equals("tech")).findFirst().get().getValue()));
                        }
                    }

                    // Fifth column
                    cell = row.getCell(COLUMN_REVIEWS_INDEX);
                    span = (HtmlSpan) cell.getFirstChild();
                    DomNode node = span;
                    if (span.getChildElementCount() == 1) {
                        node = span.getFirstChild();
                        anchor = (HtmlAnchor) span.getFirstChild();
                        saga.setUrlReviews(anchor.getHrefAttribute());
                    }
                    saga.setNbReviews(Integer.valueOf(node.getTextContent().replaceAll(" avis", "")));

                    cell = row.getCell(COLUMN_BRAVOS_INDEX);
                    String bravosString = cell.getFirstChild().getTextContent();
                    saga.setNbBravos(Integer.valueOf(bravosString));

                    Set<Saga> sagasInDb = sagaRepository.findAllByTitle(saga.getTitle());
                    if (sagasInDb.size() == 1) {
                        saga.setId(((Saga) (sagasInDb.toArray()[0])).getId());
                    } else if (sagasInDb.size() > 1) {
                        throw new IOException("More than one saga with following title in DB : " + saga.getTitle());
                    }

                    saga = sagaRepository.save(saga);
                    log.debug("Save : " + saga);
                }
            } catch (IOException | URISyntaxException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
            //nbRows = 0;
            nbPage+= nbRows;
        }
        log.info("Scrap sagas ended");
        eventLogService.insert(new EventLog(EventLogName.SYNC_SAGAS_STOP));
        eventLogService.rotate(EventLogName.SYNC_SAGAS_STOP);
    }

    private String cleanString(String str) {
        return str.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
    }
}
