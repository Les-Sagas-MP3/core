package fr.lessagasmp3.core.pdf;

import fr.lessagasmp3.core.anecdote.entity.Anecdote;
import fr.lessagasmp3.core.distribution.entity.DistributionEntry;
import fr.lessagasmp3.core.file.model.FileModel;
import fr.lessagasmp3.core.category.model.CategoryModel;
import fr.lessagasmp3.core.creator.model.CreatorModel;
import fr.lessagasmp3.core.pdf.parser.*;
import fr.lessagasmp3.core.saga.model.SagaModel;
import fr.lessagasmp3.core.pdf.extractor.*;
import fr.lessagasmp3.core.saga.service.SagaService;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Set;

@Slf4j
@Component
public class PdfScrapper {

    private static final String CREATION = LinesExtractor.convertToUtf8("CRÉATION");
    private static final String SERIE = LinesExtractor.convertToUtf8("SÉRIE");
    private static final String EPISODE = LinesExtractor.convertToUtf8("ÉPISODE");
    private static final String GENESE = LinesExtractor.convertToUtf8("GENÈSE");
    private static final String RECOMPENSE = LinesExtractor.convertToUtf8("RÉCOMPENSE");

    @Value("${fr.lessagasmp3.core.storage}")
    private String storageFolder;

    @Autowired
    private SagaService sagaService;

    @Autowired
    private BonusExtractor bonusExtractor;

    @Autowired
    private DistributionExtractor distributionExtractor;

    @Autowired
    private LinesExtractor linesExtractor;

    @Autowired
    private TitleExtractor titleExtractor;

    @Autowired
    private WebsiteExtractor websiteExtractor;

    @Autowired
    private AnecdoteParser anecdoteParser;

    @Autowired
    private CategoryParser categoryParser;

    @Autowired
    private CreationParser creationParser;

    @Autowired
    private CreatorParser creatorParser;

    @Autowired
    private DistributionParser distributionParser;

    @Autowired
    private DurationParser durationParser;

    @Autowired
    private EpisodeParser episodeParser;

    @Autowired
    private StatusParser statusParser;

    @Autowired
    private TextParser textParser;

    public SagaModel scrap(FileModel fileModel) {
        return this.parseFile(storageFolder + File.separator + fileModel.getDirectory(), fileModel.getFullname());
    }

    private SagaModel parseFile(String folderPath, String content) {

        // Output data
        String authors;
        String music;
        String origin;
        String kind;
        String style;
        String status;
        String creation;
        String duration;
        String bonus;
        String website;
        String distribution;
        String title;
        String synopsis;
        String episodes;
        String anecdotes;
        String genese;
        String recompenses;
        Boolean[] needsManualCheck;
        SagaModel saga = new SagaModel();

        String pdfPath = folderPath + File.separator + content;
        log.info("File : {}", content);

        try (PDDocument document = PDDocument.load(new File(pdfPath))) {

            content = LinesExtractor.convertToUtf8(content);

            authors = null;
            music = null;
            origin = null;
            kind = null;
            style = null;
            status = null;
            creation = null;
            duration = null;
            bonus = null;
            website = null;
            distribution = null;
            title = null;
            synopsis = null;
            episodes = null;
            genese = null;
            anecdotes = null;
            recompenses = null;
            needsManualCheck = new Boolean[]{Boolean.FALSE};

            if (!document.isEncrypted()) {
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);
                PDFTextStripper tStripper = new PDFTextStripper();
                String pdfFileInText = tStripper.getText(document);

                log.info("Parsing PDF");
                //split by whitespace
                String[] rawLines = pdfFileInText.split("\\r?\\n");
                String[] lines = new String[rawLines.length];
                for (int lineNumber = 0; lineNumber < rawLines.length; lineNumber++) {
                    lines[lineNumber] = LinesExtractor.convertToUtf8(rawLines[lineNumber]);
                }
                for (int lineNumber = 0; lineNumber < lines.length; lineNumber++) {
                    String line = lines[lineNumber];
                    log.debug("Analyzing line {} : {}", lineNumber, line);

                    // Table on left
                    String[] lineSplitColon = line.split(": ");
                    if (lineSplitColon.length >= 2) {

                        // Creator
                        String[] nextTagsAuthor = {"MUSIQUE", "ORIGIN"};
                        authors = linesExtractor.extractLines(authors, "AUTEUR", nextTagsAuthor, lines, lineNumber);

                        // Music
                        String[] nextTagsMusic = {"ORIGIN"};
                        music = linesExtractor.extractLines(music, "MUSIQUE", nextTagsMusic, lines, lineNumber);

                        // Origin
                        String[] nextTagOrigin = {"GENRE"};
                        origin = linesExtractor.extractLines(origin, "ORIGIN", nextTagOrigin, lines, lineNumber);

                        // Kind
                        String[] nextTagKind = {"STYLE"};
                        kind = linesExtractor.extractLines(kind, "GENRE", nextTagKind, lines, lineNumber);

                        // Style
                        String[] nextTagStyle = {CREATION, "STATUT"};
                        style = linesExtractor.extractLines(style, "STYLE", nextTagStyle, lines, lineNumber);

                        // Status
                        String[] nextTagStatus = {CREATION, "SAISON", SERIE, "ARC", "1ER " + SERIE, "BLOC 1", "CYCLE 1", "OPUS 1"};
                        status = linesExtractor.extractLines(status, "STATUT", nextTagStatus, lines, lineNumber);

                        // Creation
                        String[] nextTagCreation = {"STATUT", "SAISON", SERIE, "ARC", "1ER " + SERIE, "BLOC 1", "CYCLE 1", "OPUS 1"};
                        creation = linesExtractor.extractLines(creation, CREATION, nextTagCreation, lines, lineNumber);

                        // Duration
                        String[] nextTagDuration = {"BONUS"};
                        duration = linesExtractor.extractLines(duration, "DURÉE", nextTagDuration, lines, lineNumber);

                        // Bonus
                        bonus = bonusExtractor.extract(bonus, lines, lineNumber);
                    }

                    // Website
                    website = websiteExtractor.extract(website, lines, lineNumber);

                    // Distribution
                    if (bonus != null && title == null && !line.contains("Bonus : ") && (line.split(" - ").length >= 2 || line.split(" : ").length >= 2)) {
                        distribution = distributionExtractor.extract(distribution, lines, lineNumber);
                    }

                    // Title
                    title = titleExtractor.extract(title, lines, lineNumber, content, needsManualCheck);

                    // Synopsis
                    String[] nextTagsSynopsis = {EPISODE, "ANECDOTE", GENESE};
                    synopsis = linesExtractor.extractMultilines(synopsis, "SYNOPSIS", nextTagsSynopsis, lines, lineNumber);

                    // Episodes
                    String[] nextTagsEpisodes = {"ANECDOTE", GENESE};
                    episodes = linesExtractor.extractMultilines(episodes, EPISODE, nextTagsEpisodes, lines, lineNumber);

                    // Genese
                    String[] nextTagsGenese = {"ANECDOTE"};
                    genese = linesExtractor.extractMultilines(genese, GENESE, nextTagsGenese, lines, lineNumber);

                    // Anecdotes
                    String[] nextTagsAnecdotes = {RECOMPENSE};
                    anecdotes = linesExtractor.extractMultilines(anecdotes, "ANECDOTE", nextTagsAnecdotes, lines, lineNumber);

                    // Recompenses
                    String[] nextTagsRecompenses = {};
                    recompenses = linesExtractor.extractMultilines(recompenses, RECOMPENSE, nextTagsRecompenses, lines, lineNumber);
                }

                Set<CreatorModel> authorsSet;
                Set<CreatorModel> composers;
                Set<CategoryModel> styles;
                Set<CategoryModel> kinds;
                Set<DistributionEntry> distributionEntries;
                Set<Anecdote> anecdotesSet;
                log.info("Build model");

                if (title != null && !title.isEmpty() && !LinesExtractor.removeLastSpaces(title).isEmpty()) {
                    saga.setTitle(LinesExtractor.removeLastSpaces(title));
                    log.debug("TITLE : {}", saga.getTitle());

                    saga = sagaService.findOrCreate(saga.getTitle());

                    if (authors != null) {
                        authorsSet = creatorParser.parse(authors);
                        log.debug("AUTHORS : {}", authorsSet);
                        SagaModel finalSaga = saga;
                        authorsSet.forEach(author -> sagaService.addAuthor(finalSaga.getId(), author.getId()));
                    }

                    if (music != null) {
                        composers = creatorParser.parse(music);
                        log.debug("MUSIC : {}", composers);
                        SagaModel finalSaga = saga;
                        composers.forEach(composer -> sagaService.addComposer(finalSaga.getId(), composer.getId()));
                    }

                    if (origin != null && !origin.startsWith("-")) {
                        saga.setOrigin(LinesExtractor.removeLastSpaces(origin));
                        log.debug("ORIGIN : {}", saga.getOrigin());
                    }

                    if (kind != null) {
                        kinds = categoryParser.parse(kind);
                        log.debug("KINDS: {}", kinds);
                        SagaModel finalSaga = saga;
                        kinds.forEach(oneKind -> sagaService.addCategory(finalSaga.getId(), oneKind.getId()));
                    }

                    if (style != null) {
                        styles = categoryParser.parse(style);
                        log.debug("STYLES : {}", styles);
                        SagaModel finalSaga = saga;
                        styles.forEach(oneStyle -> sagaService.addCategory(finalSaga.getId(), oneStyle.getId()));
                    }

                    if (status != null) {
                        saga.setStatus(statusParser.parse(status));
                        log.debug("STATUS : {}", saga.getStatus());
                    }

                    if (creation != null) {
                        saga.setStartDate(creationParser.parse(creation));
                        log.debug("CREATION : {}", saga.getStartDate());
                    }

                    if (duration != null) {
                        saga.setDuration(durationParser.parse(duration));
                        log.debug("DURATION : {}", saga.getDuration());
                    }

                    if (website != null) {
                        saga.setUrl(LinesExtractor.removeLastSpaces(website));
                        log.debug("WEBSITE : {}", saga.getUrl());
                    }

                    if (distribution != null) {
                        distributionEntries = distributionParser.parse(distribution, saga.getId());
                        distributionEntries.forEach(distributionEntry -> log.debug("{} - {}", distributionEntry.getActor(), distributionEntry.getRoles()));
                    }

                    if (synopsis != null) {
                        saga.setSynopsis(textParser.parse(synopsis));
                        log.debug("SYNOPSIS : {}", saga.getSynopsis());
                    }

                    if (episodes != null) {
                        episodeParser.parse(episodes, saga.getId());
                    }

                    if (genese != null) {
                        saga.setGenese(textParser.parse(genese));
                        log.debug("GENESE : {}", saga.getGenese());
                    }

                    if (anecdotes != null) {
                        anecdotesSet = anecdoteParser.parse(anecdotes, saga.getId());
                        log.debug("ANECDOTES :");
                        anecdotesSet.forEach(anecdote -> log.debug("- {}", anecdote));
                    }

                    if (recompenses != null) {
                        saga.setAwards(recompenses);
                        log.debug("AWARDS : {}", saga.getAwards());
                    }

                    sagaService.update(saga);
                }

            }
        } catch (IOException | NumberFormatException | IllegalStateException e) {
            log.error(e.getMessage(), e);
        }

        return saga;
    }

}
