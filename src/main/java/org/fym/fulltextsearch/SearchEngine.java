package org.fym.fulltextsearch;

import org.apache.logging.log4j.util.Strings;
import org.fym.fulltextsearch.dao.DocumentService;
import org.fym.fulltextsearch.dao.SearchResult;
import org.fym.fulltextsearch.utility.CommandType;
import org.fym.fulltextsearch.utility.UserCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import static org.fym.fulltextsearch.utility.CommandType.INDEX;
import static org.fym.fulltextsearch.utility.CommandType.QUERY;

@Component
public class SearchEngine implements CommandLineRunner {
    @Autowired
    private DocumentService documentService;

    @Override
    public void run(String... args) {
        System.out.println("\n");
        System.out.println("  SEARCH ENGINE");
        System.out.println("==================");

        UserCommand userCommand = readInput();
        executeCommand(userCommand);
    }

    private UserCommand readInput() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        UserCommand userCommand = null;
        String input = "";

        try {
            input = reader.readLine();
        } catch (IOException e) {
            System.out.println("index error " + e.getMessage());
            readInput();
        }

        if (Strings.isBlank(input)) {
            System.out.println("index error Please enter command");
            readInput();
        }

        try {
            String[] commandParts = input.split(" ");
            userCommand = new UserCommand(commandParts);
        } catch (Exception e) {
            System.out.println("index error " + e.getMessage());
            readInput();
        }

        return userCommand;
    }

    private void executeCommand(UserCommand userCommand) {
        CommandType commandType = userCommand.getCommandType();
        if (INDEX.equals(commandType)) {
            try {
                Integer docId = Integer.valueOf(userCommand.getDocId());
                StringBuilder tokenStream = userCommand.getTokenStream();
                documentService.persistDocument(docId, tokenStream.toString());
                documentService.updateTokens();
                System.out.println("ok " + docId);
            } catch (Exception e) {
                System.out.println("query error " + e.getMessage());
            }
        } else if (QUERY.equals(commandType)) {
            try {
                String expression = userCommand.getExpressionQuery();
                List<SearchResult> searchResults = documentService.queryForExpression(expression);
                String documentIds = searchResults
                        .stream()
                        .map(result -> result.getDocId().toString())
                        .collect(Collectors.joining(" "));
                if (!documentIds.isEmpty()) {
                    System.out.println("query results: " + documentIds);
                } else {
                    System.out.println("query results: No results were found");
                }
            } catch (Exception e) {
                System.out.println("query error " + e.getMessage());
            }
        }
    }

    private List<String[]> prepareDemoCommands() {
        String[] command0 = new String[]{"index", "1", "In the morning I walked down the Boulevard to the rue Soufflot for coffee and brioche."};
        String[] command1 = new String[]{"index", "2", "I read the papers with the coffee and then smoked a cigarette."};
        String[] command2 = new String[]{"index", "3", "It was a fine morning. The horse-chestnut trees in the Luxembourg gardens were in bloom."};
        String[] command3 = new String[]{"index", "4", "Students went by going up to the law school, or down to the Sorbonne."};
        String[] command4 = new String[]{"index", "5", "The Boulevard was busy with trams and people going to work."};
        String[] command5 = new String[]{"query", "morning"};
        List<String[]> commands = List.of(command0, command1, command2, command3, command4);
        return commands;
    }
}