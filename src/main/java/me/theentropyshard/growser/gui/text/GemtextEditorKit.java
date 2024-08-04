package me.theentropyshard.growser.gui.text;

import me.theentropyshard.growser.gemini.text.GemtextParser;
import me.theentropyshard.growser.gemini.text.document.GemtextPage;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.ViewFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class GemtextEditorKit extends StyledEditorKit {
    private final ViewFactory viewFactory;
    private final JTextPane textPane;

    public GemtextEditorKit(JTextPane textPane) {
        this.textPane = textPane;
        this.viewFactory = new GemtextViewFactory();
    }

    @Override
    public void read(InputStream in, Document doc, int pos) throws IOException, BadLocationException {
        this.read(new InputStreamReader(in, StandardCharsets.UTF_8), doc, pos);
    }

    @Override
    public void read(Reader in, Document doc, int pos) throws IOException, BadLocationException {
        BufferedReader reader = new BufferedReader(in);
        String text = reader.lines().collect(Collectors.joining("\n"));

        GemtextParser parser = new GemtextParser();
        GemtextPage page = parser.parse(text);

        GemtextDocument document = (GemtextDocument) doc;
        document.write(page);
    }

    @Override
    public Document createDefaultDocument() {
        return new GemtextDocument(this.textPane);
    }

    @Override
    public String getContentType() {
        return "text/gemini";
    }

    @Override
    public ViewFactory getViewFactory() {
        return this.viewFactory;
    }
}
