package highlighting.antlr;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.TerminalNode;

public final class PrettyPrinterVisitor extends MiniJavaBaseVisitor<Void> {

    private final StringBuilder out = new StringBuilder();
    private final int indentWidth;
    private int currentIndent = 0;
    private boolean atLineStart = true;
    private Token lastToken = null;

    public PrettyPrinterVisitor(int indentWidth) {
        this.indentWidth = Math.max(0, indentWidth);
    }

    public String result() {
        return out.toString();
    }

    // -------------------------------------------------------
    // Strukturelle Visit-Methoden
    // -------------------------------------------------------

    @Override
    public Void visitCompilationUnit(MiniJavaParser.CompilationUnitContext ctx) {
        // package-Deklaration (falls vorhanden)
        if (ctx.packageDecl() != null) {
            visit(ctx.packageDecl());
            nl();
        }
        // import-Deklarationen – eine pro Zeile
        for (var imp : ctx.importDecl()) {
            visit(imp);
            nl();
        }
        // Leerzeile zwischen imports und Klassen
        if (!ctx.importDecl().isEmpty() && !ctx.typeDecl().isEmpty()) {
            nl();
        }
        // Typdeklarationen
        for (var type : ctx.typeDecl()) {
            visit(type);
            nl();
        }
        return null;
    }

    @Override
    public Void visitClassBody(MiniJavaParser.ClassBodyContext ctx) {
        // öffnende Klammer mit Zeilenumbruch
        write("{");
        nl();
        currentIndent++;

        // jedes Member eingerückt auf eigener Zeile
        for (var decl : ctx.classBodyDeclaration()) {
            if (decl.memberDecl() != null) {
                indent();
                visit(decl.memberDecl());
                nl();
            }
        }

        currentIndent--;
        indent();
        write("}");
        return null;
    }

    @Override
    public Void visitBlock(MiniJavaParser.BlockContext ctx) {
        // öffnende Klammer mit Zeilenumbruch
        write("{");
        nl();
        currentIndent++;

        // jedes blockStatement eingerückt auf eigener Zeile
        for (var stmt : ctx.blockStatement()) {
            indent();
            if (stmt.localVarDecl() != null) {
                visit(stmt.localVarDecl());
            } else if (stmt.statement() != null) {
                visit(stmt.statement());
            }
            nl();
        }

        currentIndent--;
        indent();
        write("}");
        return null;
    }

    @Override
    public Void visitStatement(MiniJavaParser.StatementContext ctx) {
        int startType = ctx.getStart().getType();

        // Block: { ... }
        if (ctx.block() != null) {
            visit(ctx.block());
            return null;
        }

        // return-Statement
        if (startType == MiniJavaLexer.RETURN) {
            write("return");
            lastToken = ctx.getStart();
            if (ctx.expression() != null) {
                write(" ");
                visit(ctx.expression());
            }
            write(";");
            return null;
        }

        // if-Statement (mit optionalem else)
        if (startType == MiniJavaLexer.IF) {
            write("if (");
            lastToken = null;
            visit(ctx.expression());
            write(") ");
            visit(ctx.statement(0));
            if (ctx.statement().size() > 1) {
                write(" else ");
                visit(ctx.statement(1));
            }
            return null;
        }

        // while-Statement
        if (startType == MiniJavaLexer.WHILE) {
            write("while (");
            lastToken = null;
            visit(ctx.expression());
            write(") ");
            visit(ctx.statement(0));
            return null;
        }

        // Ausdrucks-Statement (endet mit ';')
        if (ctx.expression() != null) {
            lastToken = null;
            visit(ctx.expression());
            write(";");
            return null;
        }

        // Fallback
        visitChildren(ctx);
        return null;
    }

    // -------------------------------------------------------
    // Hilfsmethoden
    // -------------------------------------------------------

    private void indent() {
        if (atLineStart) {
            out.repeat(" ", Math.max(0, indentWidth * currentIndent));
            atLineStart = false;
        }
    }

    private void write(String s) {
        if (s == null || s.isEmpty()) return;
        indent();
        out.append(s);
    }

    private void nl() {
        out.append('\n');
        atLineStart = true;
        lastToken = null;
    }

    private void writeln(String s) {
        write(s);
        nl();
    }

    // -------------------------------------------------------
    // Token-Ausgabe mit einfacher Leerzeichen-Heuristik
    // -------------------------------------------------------

    @Override
    public Void visitTerminal(TerminalNode node) {
        Token t = node.getSymbol();
        String text = t.getText();

        if (lastToken != null) {
            if (needsSpaceBetween(lastToken.getType(), t.getType())) {
                write(" ");
            }
        }

        write(text);
        lastToken = t;
        return null;
    }

    private boolean needsSpaceBetween(int prevType, int curType) {
        return isWordLike(prevType) && isWordLike(curType);
    }

    private boolean isWordLike(int type) {
        return type == MiniJavaLexer.IDENTIFIER
            || type == MiniJavaLexer.STRING_LITERAL
            || type == MiniJavaLexer.CHAR_LITERAL
            || type == MiniJavaLexer.NULL
            || type == MiniJavaLexer.PACKAGE
            || type == MiniJavaLexer.IMPORT
            || type == MiniJavaLexer.CLASS
            || type == MiniJavaLexer.PUBLIC
            || type == MiniJavaLexer.PRIVATE
            || type == MiniJavaLexer.FINAL
            || type == MiniJavaLexer.RETURN
            || type == MiniJavaLexer.NEW
            || type == MiniJavaLexer.IF
            || type == MiniJavaLexer.ELSE
            || type == MiniJavaLexer.WHILE
            || type == MiniJavaLexer.EXTENDS
            || type == MiniJavaLexer.IMPLEMENTS;
    }
}
