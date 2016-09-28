// NMI's Java Code Viewer 6.0a
// www.trinnion.com/javacodeviewer

// Registered to Evaluation Copy                                      
// Generated PGFZKD AyTB 14 2007 15:44:19 

//source File Name:   InputDialog.java

package gnnt.MEBS.HQApplet;

import gnnt.MEBS.HQApplet.Indicator.IndicatorBase;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.Hashtable;
import java.util.Vector;

// Referenced classes of package gnnt.MEBS.HQApplet:
//            HQApplet, CodeTable

class InputDialog extends Dialog {

    private static final long serialVersionUID = 0x416cc53feef4dc8aL;
    private Vector vString;
    public String strCmd;
    private java.awt.List m_list;
    HQApplet m_applet;

    public InputDialog(Frame f, char ch, HQApplet applet) {
        super(f, "", true);
        vString = new Vector();
        m_list = new java.awt.List();
        try {
            jbInit();
        }
        catch(Exception e) {
            if(HQApplet.bDebug != 0)
                e.printStackTrace();
        }
        m_applet = applet;
        AddChar(ch);
    }

    private void jbInit() throws Exception {
        m_list.setFont(new Font("\u5B8B\u4F53", 0, 12));
        m_list.setForeground(Color.blue);
        m_list.addMouseListener(new InputDialog1(this));
        m_list.addKeyListener(new InputDialog2(this));
        addWindowListener(new InputDialog3(this));
        addKeyListener(new InputDialog4(this));
        add(m_list);
        setResizable(false);
    }

    void this_windowClosing(WindowEvent e) {
        hide();
    }

    void this_keyPressed(KeyEvent e) {
        char ch = e.getKeyChar();
        if(Character.isLetterOrDigit(ch) || ch == '%') {
            AddChar(ch);
        } else {
            int iKey = e.getKeyCode();
            switch(iKey) {
            default:
                break;

            case 8: // '\b'
                String str = getTitle();
                if(str.length() <= 1) {
                    hide();
                } else {
                    str = str.substring(0, str.length() - 1);
                    setTitle(str);
                    DealString();
                }
                break;

            case 27: // '\033'
                hide();
                break;

            case 10: // '\n'
                strCmd = "";
                int iSel = m_list.getSelectedIndex();
                if(iSel >= 0 && iSel < vString.size())
                    strCmd = (String)vString.elementAt(iSel);
                hide();
                break;
            }
        }
    }

    void list_keyPressed(KeyEvent e) {
        int iKey = e.getKeyCode();
        if(iKey != 38 && iKey != 40 && iKey != 33 && iKey != 34)
            this_keyPressed(e);
    }

    private void AddChar(char ch) {
        String str = getTitle();
        if(str.length() <= 0 || str.length() < 8 && Character.isDigit(str.charAt(0)) || str.length() < 8 && (Character.isLetter(str.charAt(0)) || ch == '%')) {
            if(str.length() > 1 && m_list.getItemCount() == 0)
                return;
            str = str + Character.toUpperCase(ch);
            setTitle(str);
            DealString();
        }
    }

    private void DealString() {
        m_list.removeAll();
        vString.removeAllElements();
        String str = getTitle();
        if(Character.isDigit(str.charAt(0)) && str.length() <= 2)
            DealAccelerator(str);
        DealProductCode(str);
        DealProductName(str);
        if(m_applet.iCurrentPage == 2)
            DealIndicator(str);
        String strColor = "COLOR";
        if(strColor.startsWith(str)) {
            m_list.add("COLOR0 \u7ECF\u5178");
            m_list.add("COLOR1 \u73B0\u4EE3");
            m_list.add("COLOR2 \u5178\u96C5");
            m_list.add("COLOR3 \u6DE1\u96C5");
            m_list.add("COLOR4 \u9AD8\u8D35");
            vString.addElement("C0");
            vString.addElement("C1");
            vString.addElement("C2");
            vString.addElement("C3");
            vString.addElement("C4");
        }
        m_list.select(0);
    }

    private void DealAccelerator(String str) {
        for(int i = 0; i < 89; i++) {
            String strTmp = String.valueOf(i);
            if(strTmp.length() == 1)
                strTmp = "0" + strTmp;
            if(str.length() == 1 && str.charAt(0) == strTmp.charAt(0) || str.length() == 2 && str.equals(strTmp)) {
                int iNum = m_list.getItemCount();
                switch(i) {
                case 1: // '\001'
                    if(m_applet.iCurrentPage == 1 || m_applet.iCurrentPage == 2)
                        m_list.add("01 \u6210\u4EA4\u660E\u7EC6");
                    break;

                case 5: // '\005'
                    m_list.add("05 \u5206\u65F6\\\u65E5\u7EBF");
                    break;

                case 60: // '<'
                    m_list.add("60 \u6DA8\u5E45\u6392\u540D");
                    break;

                case 70: // 'F'
                    m_list.add("70 \u5386\u53F2\u5546\u54C1");
                    break;

                case 80: // 'P'
                    m_list.add("80 \u7EFC\u5408\u6392\u540D");
                    break;
                }
                if(iNum < m_list.getItemCount())
                    vString.addElement("A" + strTmp);
            }
        }

    }

    private void DealProductCode(String str) {
        int iCount = m_applet.m_codeList.size();
        for(int i = 0; i < iCount; i++) {
            if(m_list.getItemCount() > 20)
                break;
            if(((String)m_applet.m_codeList.elementAt(i)).toUpperCase().indexOf(str) < 0)
                continue;
            CodeTable s = (CodeTable)m_applet.m_htProduct.get(m_applet.m_codeList.elementAt(i));
            HQApplet _tmp = m_applet;
            if(m_applet.iCurrentPage != 2) {
                HQApplet _tmp1 = m_applet;
                if(m_applet.iCurrentPage != 6 && (s.status == 1 || s.status == 4))
                    continue;
            }
            m_list.add(m_applet.m_codeList.elementAt(i) + " " + s.sName);
            vString.addElement("P" + m_applet.m_codeList.elementAt(i));
        }

    }

    private void DealProductName(String str) {
        int iCount = m_applet.m_codeList.size();
        for(int i = 0; i < iCount; i++) {
            if(m_list.getItemCount() > 20)
                break;
            CodeTable s = (CodeTable)m_applet.m_htProduct.get(m_applet.m_codeList.elementAt(i));
            HQApplet _tmp = m_applet;
            if(m_applet.iCurrentPage != 2) {
                HQApplet _tmp1 = m_applet;
                if(m_applet.iCurrentPage != 6 && (s.status == 1 || s.status == 4))
                    continue;
            }
            for(int j = 0; j < s.sPinyin.length; j++)
                if(s.sPinyin[j].startsWith(str)) {
                    boolean bFound = false;
                    for(int k = 0; k < vString.size(); k++)
                        if(vString.elementAt(k).equals(m_applet.m_codeList.elementAt(i)))
                            bFound = true;

                    if(!bFound) {
                        m_list.add(s.sPinyin[j] + " " + s.sName);
                        vString.addElement("P" + m_applet.m_codeList.elementAt(i));
                    }
                }

        }

    }

    private void DealIndicator(String str) {
        for(int i = 0; i < IndicatorBase.INDICATOR_NAME.length; i++)
            if(IndicatorBase.INDICATOR_NAME[i][0].startsWith(str)) {
                String strFullName = m_applet.getShowString("T_" + IndicatorBase.INDICATOR_NAME[i][0]);
                m_list.add(IndicatorBase.INDICATOR_NAME[i][0] + " " + strFullName.substring(0, Math.min(6, strFullName.length())));
                vString.addElement("T" + IndicatorBase.INDICATOR_NAME[i][0]);
            }

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
