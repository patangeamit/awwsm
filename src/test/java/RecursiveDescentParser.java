import java.util.Arrays;
import java.util.List;

abstract class ASTNode {
}

class NumberNode extends ASTNode {
    double value;

    NumberNode(double value) {
        this.value = value;
    }
}

class BinaryOpNode extends ASTNode {
    String operator;
    ASTNode left, right;

    BinaryOpNode(String operator, ASTNode left, ASTNode right) {
        this.operator = operator;
        this.left = left;
        this.right = right;
    }
}

class Parser {
    private List<String> tokens;
    private int currentTokenIndex;

    public Parser(List<String> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }

    private String peek() {
        return currentTokenIndex < tokens.size() ? tokens.get(currentTokenIndex) : null;
    }

    private String consume() {
        return tokens.get(currentTokenIndex++);
    }

    // Parse an expression with "+" and "-" precedence
    public ASTNode parseExpression() {
        ASTNode node = parseFactor();
        while (peek() != null && (peek().equals("+") || peek().equals("-"))) {
            String operator = consume();
            ASTNode right = parseFactor();
            node = new BinaryOpNode(operator, node, right);
        }
        return node;
    }

    // Parse a term with "*" and "/" precedence
    public ASTNode parseFactor() {
        ASTNode node = parseTerm();
        while (peek() != null && (peek().equals("*") || peek().equals("/"))) {
            String operator = consume();
            ASTNode right = parseTerm();
            node = new BinaryOpNode(operator, node, right);
        }
        return node;
    }

    // Parse a number or a parenthesized expression
    public ASTNode parseTerm() {
        String token = peek();
        if (token == null)
            return null;

        if (token.equals("(")) {
            consume(); // consume "("
            ASTNode node = parseExpression();
            consume(); // consume ")"
            return node;
        } else {
            consume();
            return new NumberNode(Double.parseDouble(token));
        }
    }
}

public class RecursiveDescentParser {
    public static void main(String[] args) {
        // Tokenize input
        List<String> tokens = Arrays.asList("3");

        // Create parser and parse
        Parser parser = new Parser(tokens);
        ASTNode ast = parser.parseExpression();

        // The resulting AST represents the parsed expression
        System.out.println("Parsed AST: " + ast);
    }
}
