// NMI's Java Code Viewer 6.0a
// www.trinnion.com/javacodeviewer

// Registered to Evaluation Copy                                      
// Generated PGFZKD AyTB 14 2007 15:44:19 

//source File Name:   AboutDialog.java

package gnnt.MEBS.HQApplet;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextComponent;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.Vector;

// Referenced classes of package gnnt.MEBS.HQApplet:
//            HQApplet

class AboutDialog extends Dialog {

    public static final String VER = "1.2.22";
    private static final long serialVersionUID = 0x6d97c7b19fc60a97L;
    private Vector vString;
    public String strCmd;
    private java.awt.List m_list;
    private TextArea m_text;
    private HQApplet m_applet;

    public AboutDialog(Frame f, HQApplet applet) {
        super(f, applet.getShowString("About"), true);
        vString = new Vector();
        m_list = new java.awt.List();
        m_text = new TextArea("", 2, 2, 3);
        m_applet = applet;
        try {
            jbInit();
        }
        catch(Exception e) {
            if(HQApplet.bDebug != 0)
                e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        m_list.setForeground(Color.blue);
        m_list.addMouseListener(new AboutDialog1(this));
        m_list.addKeyListener(new AboutDialog2(this));
        addWindowListener(new AboutDialog3(this));
        addKeyListener(new AboutDialog4(this));
        m_text.setBackground(Color.white);
        m_text.setForeground(Color.blue);
        m_text.setEditable(false);
        m_text.append(m_applet.getShowString("AppletName"));
        m_text.append("\n" + m_applet.getShowString("Version") + " " + "1.2.22");
        m_text.append("\n\n" + m_applet.getShowString("Company") + " (C) Copyright 2005~2006");
        add(m_text);
        setResizable(false);
    }

    void this_windowClosing(WindowEvent e) {
        hide();
    }

    void m_list_mouseClicked(MouseEvent e) {
        if(e.getModifiers() != 4 && e.getClickCount() > 1) {
            strCmd = "";
            int iSel = m_list.getSelectedIndex();
            if(iSel >= 0 && iSel < vString.size())
                strCmd = (String)vString.elementAt(iSel);
            hide();
        }
    }
}
