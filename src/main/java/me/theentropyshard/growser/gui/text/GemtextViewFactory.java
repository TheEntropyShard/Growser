package me.theentropyshard.growser.gui.text;

import javax.swing.text.*;

public class GemtextViewFactory implements ViewFactory {
    @Override
    public View create(Element element) {
        String kind = element.getName();

        if (kind != null) {
            switch (kind) {
                case AbstractDocument.ContentElementName:
                    return new LabelView(element);
                case AbstractDocument.ParagraphElementName:
                    return new ParagraphView(element);
                case AbstractDocument.SectionElementName:
                    return new BoxView(element, View.Y_AXIS);
                case StyleConstants.ComponentElementName:
                    return new ComponentView(element);
                case StyleConstants.IconElementName:
                    return new IconView(element);
            }
        }

        return new LabelView(element);
    }
}
