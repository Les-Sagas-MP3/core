package fr.lessagasmp3.core.pdf.extractor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Service
public class LinesExtractor {

    public String extractLines(String lineParsed, String enterringTag, String[] nextTags, String[] lines, int lineNumber) {
        String line = lines[lineNumber];
        if (lineParsed == null && line.toUpperCase().startsWith(enterringTag)) {
            log.debug("{} recognized", enterringTag);
            StringBuilder separatedEntities = new StringBuilder();
            String[] lineSplitColon = line.split(": ");
            separatedEntities.append(lineSplitColon[1]);
            String nextLine = lines[lineNumber + 1].toUpperCase();
            boolean reachedNextTag = false;
            for (String nextTag : nextTags) {
                reachedNextTag |= nextLine.startsWith(nextTag);
            }
            while (!reachedNextTag) {
                lineNumber++;
                separatedEntities.append(lines[lineNumber]);
                log.debug(lines[lineNumber]);
                if (lineNumber + 1 < lines.length) {
                    nextLine = lines[lineNumber + 1].toUpperCase();
                    for (String nextTag : nextTags) {
                        reachedNextTag |= nextLine.startsWith(nextTag);
                    }
                } else {
                    reachedNextTag = true;
                }
            }
            lineParsed = separatedEntities.toString();
            log.debug("{} : {}", enterringTag, lineParsed);
        }
        return lineParsed;
    }

    public String extractMultilines(String lineParsed, String enterringTag, String[] nextTags, String[] lines, int lineNumber) {
        if (lineParsed == null && lines[lineNumber].toUpperCase().startsWith(enterringTag)) {
            log.debug("{} recognized", enterringTag);
            StringBuilder separatedEntities = new StringBuilder();
            String nextLine = lines[lineNumber + 1].toUpperCase();
            boolean reachedNextTag = lineNumber + 1 >= lines.length;
            for (String nextTag : nextTags) {
                reachedNextTag |= nextLine.startsWith(nextTag);
            }
            while (!reachedNextTag) {
                lineNumber++;
                separatedEntities.append(lines[lineNumber]);
                separatedEntities.append("\n");
                log.debug(lines[lineNumber]);
                if (lineNumber + 1 < lines.length) {
                    nextLine = lines[lineNumber + 1].toUpperCase();
                    for (String nextTag : nextTags) {
                        reachedNextTag |= nextLine.startsWith(nextTag);
                        log.debug("nextLine : {} starts with {} ? {}", nextLine, nextTag, reachedNextTag);
                    }
                } else {
                    reachedNextTag = true;
                }
            }
            lineParsed = separatedEntities.toString();
            log.debug("{} : {}", enterringTag, lineParsed);
        }
        return lineParsed;
    }

    public static String convertToUtf8(String str) {
        if(str == null || str.isEmpty()) {
            return "";
        }
        log.debug("Convert {} to UTF-8", str);
        ByteBuffer byteBuff = ByteBuffer.wrap(str.getBytes());
        CharBuffer cb = Charset.forName("windows-1252").decode(byteBuff);
        ByteBuffer bb = StandardCharsets.UTF_8.encode(cb);
        List<Byte> bytesWithoutZeroes = new ArrayList<>();
        int byteNumber = 0;
        byte currentByte = bb.array()[byteNumber];
        while(currentByte != 0 && byteNumber < bb.limit()) {
            currentByte = bb.array()[byteNumber];
            bytesWithoutZeroes.add(currentByte);
            byteNumber++;
        }
        byte[] finalBytes = new byte[bytesWithoutZeroes.size()];
        StringBuilder byteStr = new StringBuilder();
        IntStream.range(0, bytesWithoutZeroes.size())
                .forEach(index -> {
                    finalBytes[index] = bytesWithoutZeroes.get(index);
                    byteStr.append(finalBytes[index]).append(" ");
                });
        log.debug("Bytes : {}", byteStr);
        String utf8Str = new String(finalBytes);
        log.debug("Result : {}", utf8Str);
        //return utf8Str;
        return str;
    }

    public static String removeLastSpaces(String str) {
        return str.replaceAll("\\s+$", "");
    }
}
