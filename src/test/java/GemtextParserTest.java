import me.theentropyshard.growser.gemini.gemtext.GemtextParser;
import me.theentropyshard.growser.gemini.gemtext.document.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GemtextParserTest {
    @Test
    void test_parsing_paragraph() {
        GemtextParser parser = new GemtextParser();
        GemtextPage page = parser.parse("Just a line here\nthis is a second paragraph\n\nthere was an empty paragraph");
        List<GemtextElement> elements = page.getElements();
        assertEquals(4, elements.size());
        GemtextParagraphElement paragraph1 = (GemtextParagraphElement) elements.get(0);
        GemtextParagraphElement paragraph2 = (GemtextParagraphElement) elements.get(1);
        GemtextParagraphElement paragraph3 = (GemtextParagraphElement) elements.get(2);
        GemtextParagraphElement paragraph4 = (GemtextParagraphElement) elements.get(3);
        assertEquals("Just a line here", paragraph1.getText());
        assertEquals("this is a second paragraph", paragraph2.getText());
        assertEquals("", paragraph3.getText());
        assertEquals("there was an empty paragraph", paragraph4.getText());
    }

    @Test
    void test_parsing_heading_l1() {
        GemtextParser parser = new GemtextParser();
        GemtextPage page = parser.parse("#Incorrect heading\n# Correct heading\n#   Weird heading");
        List<GemtextElement> elements = page.getElements();
        assertEquals(3, elements.size());
        GemtextParagraphElement gemtextH1Element1 = (GemtextParagraphElement) elements.get(0);
        GemtextH1Element gemtextH1Element2 = (GemtextH1Element) elements.get(1);
        GemtextH1Element gemtextH1Element3 = (GemtextH1Element) elements.get(2);
        assertEquals("#Incorrect heading", gemtextH1Element1.getText());
        assertEquals("Correct heading", gemtextH1Element2.getText());
        assertEquals("  Weird heading", gemtextH1Element3.getText());
    }

    @Test
    void test_parsing_heading_l2() {
        GemtextParser parser = new GemtextParser();
        GemtextPage page = parser.parse("##Incorrect heading\n## Correct heading\n##   Weird heading");
        List<GemtextElement> elements = page.getElements();
        assertEquals(3, elements.size());
        GemtextParagraphElement gemtextParagraphElement = (GemtextParagraphElement) elements.get(0);
        GemtextH2Element gemtextH2Element2 = (GemtextH2Element) elements.get(1);
        GemtextH2Element gemtextH2Element3 = (GemtextH2Element) elements.get(2);
        assertEquals("##Incorrect heading", gemtextParagraphElement.getText());
        assertEquals("Correct heading", gemtextH2Element2.getText());
        assertEquals("  Weird heading", gemtextH2Element3.getText());
    }

    @Test
    void test_parsing_heading_l3() {
        GemtextParser parser = new GemtextParser();
        GemtextPage page = parser.parse("###Incorrect heading\n### Correct heading\n###   Weird heading");
        List<GemtextElement> elements = page.getElements();
        assertEquals(3, elements.size());
        GemtextParagraphElement gemtextParagraphElement = (GemtextParagraphElement) elements.get(0);
        GemtextH3Element gemtextH3Element2 = (GemtextH3Element) elements.get(1);
        GemtextH3Element gemtextH3Element3 = (GemtextH3Element) elements.get(2);
        assertEquals("###Incorrect heading", gemtextParagraphElement.getText());
        assertEquals("Correct heading", gemtextH3Element2.getText());
        assertEquals("  Weird heading", gemtextH3Element3.getText());
    }

    @Test
    void test_parsing_list() {
        GemtextParser parser = new GemtextParser();
        GemtextPage page = parser.parse("* list item 1\n* list item 2\n* list item 3\na paragraph\n* a single element list");
        List<GemtextElement> elements = page.getElements();
        assertEquals(3, elements.size());
        GemtextListElement firstList = (GemtextListElement) elements.get(0);
        GemtextParagraphElement paragraphElement = (GemtextParagraphElement) elements.get(1);
        GemtextListElement secondList = (GemtextListElement) elements.get(2);
        assertEquals(3, firstList.size());
        assertEquals("list item 1", firstList.get(0));
        assertEquals("list item 2", firstList.get(1));
        assertEquals("list item 3", firstList.get(2));
        assertEquals("a paragraph", paragraphElement.getText());
        assertEquals(1, secondList.size());
        assertEquals("a single element list", secondList.get(0));
    }

    @Test
    void test_parsing_preformatted_no_caption() {
        GemtextParser parser = new GemtextParser();
        GemtextPage page = parser.parse("```\nsome source code\n```");
        List<GemtextElement> elements = page.getElements();
        assertEquals(1, elements.size());
        GemtextPreformattedElement preformatted = (GemtextPreformattedElement) elements.get(0);
        assertNull(preformatted.getCaption());
        assertEquals("some source code", preformatted.getText());
    }

    @Test
    void test_parsing_preformatted_with_caption() {
        GemtextParser parser = new GemtextParser();
        GemtextPage page = parser.parse("```java\nsome source code\n```");
        List<GemtextElement> elements = page.getElements();
        assertEquals(1, elements.size());
        GemtextPreformattedElement preformatted = (GemtextPreformattedElement) elements.get(0);
        assertEquals("java", preformatted.getCaption());
        assertEquals("some source code", preformatted.getText());
    }

    @Test
    void test_parsing_link() {
        GemtextParser parser = new GemtextParser();
        GemtextPage page = parser.parse("""
            =>gemini://geminiprotocol.net/
            => gemini://geminiprotocol.net/docs Read the docs!
            =>https://example.com   This is an example link
            """);
        List<GemtextElement> elements = page.getElements();
        assertEquals(3, elements.size());
        GemtextLinkElement link1 = (GemtextLinkElement) elements.get(0);
        GemtextLinkElement link2 = (GemtextLinkElement) elements.get(1);
        GemtextLinkElement link3 = (GemtextLinkElement) elements.get(2);
        assertNull(link1.getLabel());
        assertEquals("gemini://geminiprotocol.net/", link1.getLink());
        assertEquals("Read the docs!", link2.getLabel());
        assertEquals("gemini://geminiprotocol.net/docs", link2.getLink());
        assertEquals("This is an example link", link3.getLabel());
        assertEquals("https://example.com", link3.getLink());
    }

    @Test
    void test_parsing_blockquote() {
        GemtextParser parser = new GemtextParser();
        GemtextPage page = parser.parse("> Lorem ipsum dolor sit amet");
        List<GemtextElement> elements = page.getElements();
        assertEquals(1, elements.size());
        GemtextBlockquoteElement blockquote = (GemtextBlockquoteElement) elements.get(0);
        assertEquals("Lorem ipsum dolor sit amet", blockquote.getText());
    }
}
