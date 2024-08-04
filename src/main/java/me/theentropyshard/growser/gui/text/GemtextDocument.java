package me.theentropyshard.growser.gui.text;

import me.theentropyshard.growser.gemini.text.document.*;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.List;

public class GemtextDocument extends DefaultStyledDocument {
    private final SimpleAttributeSet centerAttrs;
    private final SimpleAttributeSet leftAttrs;
    private final JTextPane textPane;

    private int position;

    public GemtextDocument(JTextPane textPane) {
        this.textPane = textPane;
        this.centerAttrs = new SimpleAttributeSet();
        StyleConstants.setAlignment(this.centerAttrs, StyleConstants.ALIGN_CENTER);

        this.leftAttrs = new SimpleAttributeSet();
        StyleConstants.setAlignment(this.leftAttrs, StyleConstants.ALIGN_LEFT);
    }

    public void write(GemtextPage page) throws BadLocationException {
        List<GemtextElement> elements = page.getElements();

        for (GemtextElement element : elements) {
            switch (element.getType()) {
                case H1:
                    GemtextH1Element h1 = (GemtextH1Element) element;
                    String text = h1.getText();
                    this.writeHeader(text, 1);
                    break;
                case H2:
                    GemtextH2Element h2 = (GemtextH2Element) element;
                    this.writeHeader(h2.getText(), 2);
                    break;
                case H3:
                    GemtextH3Element h3 = (GemtextH3Element) element;
                    this.writeHeader(h3.getText(), 3);
                    break;
                case TEXT:
                    GemtextParagraphElement paragraph = (GemtextParagraphElement) element;
                    this.writeParagraph(paragraph.getText());
                    break;
                case LINK:
                    GemtextLinkElement link = (GemtextLinkElement) element;
                    this.writeLink(link);
                    break;
                case PREFORMATTED:
                    GemtextPreformattedElement preformatted = (GemtextPreformattedElement) element;
                    this.writePreformatted(preformatted.getText());
                    break;
                case LIST_ITEM:
                    GemtextListItemElement listItem = (GemtextListItemElement) element;
                    this.writeListItem(listItem.getText());
                    break;
            }
        }
    }

    private void writeListItem(String text) throws BadLocationException {
        this.setParagraphAttributes(this.position, text.length(), GemtextDocument.getParagraphAttrs(), false);
        SimpleAttributeSet textAttrs = GemtextDocument.getTextAttrs();
        textAttrs.addAttribute("listItem", text);
        this.writeText(text + "\n", textAttrs);
    }

    private void writeLink(GemtextLinkElement link) throws BadLocationException {
        this.setParagraphAttributes(this.position, link.getLabel().length(), GemtextDocument.getParagraphAttrs(), false);
        SimpleAttributeSet linkAttrs = GemtextDocument.getLinkAttrs();
        String linkLabel = link.getLabel().trim();
        String linkLink = link.getLink().trim();
        linkAttrs.addAttribute("link.label", linkLabel);
        linkAttrs.addAttribute("link.link", linkLink);
        this.writeText(linkLabel + "\n", linkAttrs);
    }

    private void writeParagraph(String text) throws BadLocationException {
        this.setParagraphAttributes(this.position, text.length(), GemtextDocument.getParagraphAttrs(), false);
        this.writeText(text + "\n", GemtextDocument.getTextAttrs());
    }

    private void writePreformatted(String text) throws BadLocationException {
        this.setParagraphAttributes(this.position, text.length(), GemtextDocument.getParagraphAttrs(), false);
        SimpleAttributeSet preformattedAttrs = GemtextDocument.getPreformattedAttrs();
        preformattedAttrs.addAttribute("preformatted", "value");
        this.writeText(text + "\n", preformattedAttrs);
    }

    private void writeHeader(String text, int size) throws BadLocationException {
        if (size == 1) {
            this.setParagraphAttributes(this.position, text.length(), this.centerAttrs, false);
        } else {
            this.setParagraphAttributes(this.position, text.length(), this.leftAttrs, false);
        }
        this.writeText(text + "\n", GemtextDocument.getHeaderAttrs(size));
    }

    private void writeText(String text, AttributeSet attrs) throws BadLocationException {
        this.insertString(this.position, text, attrs);

        this.position += text.length();
    }

    private static SimpleAttributeSet getHeaderAttrs(int level) {
        SimpleAttributeSet attrs = new SimpleAttributeSet();

        StyleConstants.setBold(attrs, Boolean.TRUE);

        switch (level) {
            case 1:
                StyleConstants.setFontSize(attrs, 24);
                break;
            case 2:
                StyleConstants.setFontSize(attrs, 20);
                break;
            case 3:
                StyleConstants.setFontSize(attrs, 18);
                break;
            default:
                throw new IllegalArgumentException("Unexpected header level: " + level + ", must be 1, 2, or 3");
        }

        return attrs;
    }

    private static SimpleAttributeSet getTextAttrs() {
        SimpleAttributeSet attrs = new SimpleAttributeSet();

        StyleConstants.setFontFamily(attrs, "sans-serif");
        StyleConstants.setFontSize(attrs, 14);

        return attrs;
    }

    private static SimpleAttributeSet getLinkAttrs() {
        SimpleAttributeSet textAttrs = GemtextDocument.getTextAttrs();

        StyleConstants.setUnderline(textAttrs, true);
        StyleConstants.setForeground(textAttrs, Color.BLUE);

        return textAttrs;
    }

    public static SimpleAttributeSet getPreformattedAttrs() {
        SimpleAttributeSet textAttrs = GemtextDocument.getTextAttrs();

        StyleConstants.setFontFamily(textAttrs, "monospaced");
        StyleConstants.setBackground(textAttrs, Color.LIGHT_GRAY);

        return textAttrs;
    }

    public static SimpleAttributeSet getParagraphAttrs() {
        SimpleAttributeSet res = new SimpleAttributeSet();

        StyleConstants.setAlignment(res, 0);
        StyleConstants.setSpaceAbove(res, 0.4f);
        StyleConstants.setSpaceBelow(res, 0.4f);
        StyleConstants.setLeftIndent(res, 20.0f);
        StyleConstants.setRightIndent(res, 20.0f);
        StyleConstants.setLineSpacing(res, 0.4f);

        return res;
    }
}
