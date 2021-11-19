package fr.lessagasmp3.core.pdf.parser;

import fr.lessagasmp3.core.anecdote.entity.Anecdote;
import fr.lessagasmp3.core.pdf.extractor.LinesExtractor;
import fr.lessagasmp3.core.anecdote.service.AnecdoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class AnecdoteParser {

    @Autowired
    private AnecdoteService anecdoteService;

    public Set<Anecdote> parse(String anecdotes, Long sagaId) {
        String[] paragraphs = anecdotes.split("\\.\n");
        Set<Anecdote> anecdotesSet = new LinkedHashSet<>();

        if(paragraphs.length == 1) {
            Anecdote anecdote = Anecdote.fromModel(anecdoteService.findOrCreate(LinesExtractor.removeLastSpaces(anecdotes.replace("\n", "")), sagaId));
            anecdotesSet.add(anecdote);
        } else {
            for (String paragraph : paragraphs) {
                Anecdote anecdote = Anecdote.fromModel(anecdoteService.findOrCreate(paragraph.replace("\n", "") + ".\n", sagaId));
                anecdotesSet.add(anecdote);
            }
        }

        return anecdotesSet;
    }


}
