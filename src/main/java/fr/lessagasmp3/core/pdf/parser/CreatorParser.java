package fr.lessagasmp3.core.pdf.parser;

import fr.lessagasmp3.core.creator.model.CreatorModel;
import fr.lessagasmp3.core.creator.service.CreatorService;
import fr.lessagasmp3.core.pdf.extractor.LinesExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class CreatorParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreatorParser.class);

    @Autowired
    private CreatorService creatorService;

    public Set<CreatorModel> parse(String creatorsString) {
        Set<CreatorModel> creators = new LinkedHashSet<>();
        creatorsString = creatorsString.replace(" & ", "|")
                .replace(" et ", "|")
                .replace(", ", "|")
                .replace("| ", "|")
                .replace(" |", "|");
        String[] splitAuthors = creatorsString.split("\\|");
        for (String authorStr : splitAuthors) {
            authorStr = LinesExtractor.removeLastSpaces(authorStr);
            CreatorModel creator = creatorService.findByName(authorStr);
            if (creator == null) {
                creator = new CreatorModel();
                creator.setName(authorStr);
                creator = creatorService.create(creator);
                LOGGER.debug("Creator {} created", creator.getName());
            } else {
                LOGGER.debug("Creator already exists : ID={} NAME={}", creator.getId(), creator.getName());
            }
            creators.add(creator);
        }
        return creators;
    }


}
