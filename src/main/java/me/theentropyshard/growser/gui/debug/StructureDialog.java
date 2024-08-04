package me.theentropyshard.growser.gui.debug;

import javax.swing.*;

public class StructureDialog extends JDialog {
    public StructureDialog(JFrame parent, JEditorPane source) {
        super(parent, "Structure");

        EditorPaneStructure pnlStructure = new EditorPaneStructure(source);
        pnlStructure.refresh();

        this.getContentPane().add(pnlStructure);
        this.setSize(700, 500);
        this.setLocationRelativeTo(null);

        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    }
}