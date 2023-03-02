package org.fym.fulltextsearch.utility;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.fym.fulltextsearch.utility.CommandType;

@Getter
public class UserCommand {
    private String command;
    private String docId;
    private StringBuilder tokenStream = new StringBuilder();
    private CommandType commandType;

    private String expressionQuery;

    public UserCommand(String[] parts) {
        if (parts == null || parts.length < 2) {
            throw new IllegalArgumentException("Command or arguments missing.");
        }
        this.command = parts[0];

        if ("index".equals(this.command)) {
            this.commandType = CommandType.INDEX;
            this.docId = parts[1];

            try {
                Integer.parseInt(docId);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Document Id can only be a digit.");
            }

            if (parts.length < 3) {
                throw new IllegalArgumentException("Arguments missing for INDEX command.");
            }

            for (int i = 2; i < parts.length; i++) {
                String token = parts[i];
                if (!StringUtils.isAlphanumeric(token)) {
                    throw new IllegalArgumentException("Tokens cannot contain non-alphanumeric characters.");
                }
                this.tokenStream.append(token);
                this.tokenStream.append(" ");
            }

        } else if ("query".equals(this.command)) {
            this.commandType = CommandType.QUERY;
            this.expressionQuery = parts[1];
        } else {
            throw new IllegalArgumentException("Allowed command values: INDEX | QUERY");
        }
    }
}
