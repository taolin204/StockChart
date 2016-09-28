// NMI's Java Code Viewer 6.0a
// www.trinnion.com/javacodeviewer

// Registered to Evaluation Copy                                      
// Generated PGFZKD AyTB 14 2007 15:44:19 

//source File Name:   HQApplet.java

package gnnt.MEBS.HQApplet;

import gnnt.MEBS.hq.TradeTimeVO;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URL;
import java.util.*;

// Referenced classes of package gnnt.MEBS.HQApplet:
//            ProductData, HttpThread, SendThread, ReceiveThread, 
//            RHColor, Page_Bottom, InputDialog, Page_KLine, 
//            Draw_KLine, Page_Main, Common, Page_MarketStatus, 
//            Page_MultiQuote, CodeTable, Page_MinLine, AboutDialog, 
//            Page_Bill, Page_History

public class HQApplet extends Applet
    implements FocusListener {

    private static final long serialVersionUID = 0x5ba23793a3c0f861L;
    private boolean isStandalone;
    static final int PAGE_MULTIQUOTE = 0;
    static final int PAGE_MINLINE = 1;
    static final int PAGE_KLINE = 2;
    static final int PAGE_F10 = 3;
    static final int PAGE_BILL = 4;
    static final int PAGE_MARKETSTATUS = 5;
    static final int PAGE_HISTORY = 6;
    int iCurrentPage;
    public String strCurrentCode;
    String indexMainCode;
    int m_iKLineCycle;
    String m_strIndicator;
    int m_iCodeDate;
    int m_iCodeTime;
    int m_iDate;
    int m_iTime;
    //TradeTimeVO m_timeRange[];
    public TradeTimeVO m_timeRange[];
    int m_iMinLineInterval;
    Vector m_codeList;
    Hashtable m_htProduct;
    Vector vProductData;
    private Rectangle m_rcMain;
    private Rectangle m_rcBottom;
    Page_Main mainGraph;
    Page_Bottom bottomGraph;
    public static RHColor rhColor = null;
    String strSocketIP;
    int iSocketPort;
    String strURLPath;
    Socket socket;
    SendThread sendThread;
    ReceiveThread receiveThread;
    HttpThread httpThread;
    boolean bRunning;
    PopupMenu popupMenu;
    static int bDebug = 0;
    int iShowBuySellPrice;
    String m_strMarketName;
    int m_bShowIndexAtBottom;
    int m_bShowIndexKLine;
    int m_iPrecision;
    int m_iPrecisionIndex;
    ResourceBundle m_resourceBundle;
    String strLanguageName;
    boolean bInputDlgShow;
    boolean bAboutDlgShow;
    private boolean m_bEndPaint;
    private Image m_img;

    public String getParameter(String key, String def) {
        return isStandalone ? System.getProperty(key, def) : getParameter(key) == null ? def : getParameter(key);
    }

    public static void main(String args[]) {
        HQApplet applet = new HQApplet();
        applet.isStandalone = true;
        Frame frame = new HQApplet1(applet);
        frame.setTitle("Applet Frame");
        frame.add(applet, "Center");
        frame.setSize(800, 600);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((d.width - frame.getSize().width) / 2, (d.height - frame.getSize().height) / 2);
        frame.setVisible(true);
        applet.init();
        applet.start();
    }

    ProductData GetProductData(String code) {
        for(int i = 0; i < vProductData.size(); i++)
            if(((ProductData)vProductData.elementAt(i)).sCode.equals(code))
                return (ProductData)vProductData.elementAt(i);

        return null;
    }

    public HQApplet() {
        isStandalone = false;
        iCurrentPage = -1;
        strCurrentCode = "";
        indexMainCode = "";
        m_iKLineCycle = 1;
        m_strIndicator = "ORDER";
        m_iDate = 0;
        m_iTime = 0;
        m_timeRange = null;
        m_iMinLineInterval = 60;
        m_codeList = new Vector();
        m_htProduct = new Hashtable();
        vProductData = new Vector();
        m_rcMain = null;
        m_rcBottom = null;
        mainGraph = null;
        bottomGraph = null;
        socket = null;
        bRunning = true;
        popupMenu = new PopupMenu();
        m_strMarketName = "";
        m_bShowIndexAtBottom = 1;
        m_bShowIndexKLine = 0;
        m_iPrecision = 0;
        m_iPrecisionIndex = 2;
        strLanguageName = "zh";
        bInputDlgShow = false;
        bAboutDlgShow = false;
        System.out.println("new HQApplet ");
    }

    public void init() {
        bRunning = true;
        try {
            jbInit();
        }
        catch(Exception e) {
            if(bDebug != 0)
                e.printStackTrace();
        }
        if(bDebug != 0)
            System.out.println("init HQApplet ");
    }

    private void jbInit() throws Exception {
        if(isStandalone) {
            strSocketIP = "218.25.163.212";
            iSocketPort = 8002;
            strURLPath = "http://" + strSocketIP + ":80/hqApplet/";
            bDebug = 1;
            iShowBuySellPrice = 1;
        } else {
            URL url = getDocumentBase();
            strSocketIP = url.getHost();
            iSocketPort = Integer.parseInt(getParameter("Port", "888"));
            strURLPath = url.toString();
            strURLPath = strURLPath.substring(0, strURLPath.lastIndexOf('/') + 1);
            bDebug = Integer.parseInt(getParameter("Debug", "0"));
            iShowBuySellPrice = Integer.parseInt(getParameter("ShowBuySell", "3"));
            if(iShowBuySellPrice > 5 || iShowBuySellPrice <= 0)
                iShowBuySellPrice = 3;
            m_strMarketName = getParameter("MarketName", "");
            m_bShowIndexAtBottom = Integer.parseInt(getParameter("ShowIndexAtBottom", "1"));
            strLanguageName = getParameter("Language", "zh");
            m_iPrecision = Integer.parseInt(getParameter("Precision", "0"));
            m_iPrecisionIndex = Integer.parseInt(getParameter("IndexPrecision", "2"));
            m_bShowIndexKLine = Integer.parseInt(getParameter("ShowIndexKLine", "0"));
        }
        try {
            m_resourceBundle = ResourceBundle.getBundle("rc/string", new Locale(strLanguageName, ""));
        }
        catch(Exception e) {
            System.out.println("Language resource loaded failed !");
            e.printStackTrace();
        }
        m_rcMain = null;
        (new HttpThread(0, this)).start();
        sendThread = new SendThread(this);
        sendThread.start();
        receiveThread = new ReceiveThread(this);
        receiveThread.start();
        httpThread = new HttpThread(1, this);
        httpThread.start();
        String strColorStyle = getParameter("ColorStyle", "0");
        rhColor = new RHColor(Integer.parseInt(strColorStyle));
        setBackground(rhColor.clBackGround);
        addComponentListener(new HQApplet2(this));
        addKeyListener(new HQApplet3(this));
        addMouseListener(new HQApplet4(this));
        addMouseMotionListener(new HQApplet5(this));
        addFocusListener(this);
        this_componentResized(null);
        bottomGraph = new Page_Bottom(getGraphics(), m_rcBottom, this);
        requestFocus();
        add(popupMenu);
        popupMenu.addActionListener(mainGraph);
        strCurrentCode = getParameter("CurrentCode", "");
        if(strCurrentCode.length() == 0) {
            UserCommand("60");
        } else {
            String strPage = getParameter("CurrentPage", "MinLinePage");
            if(strPage.equalsIgnoreCase("MinLine"))
                showPageMinLine();
            else
            if(strPage.equalsIgnoreCase("KLine")) {
                showPageMinLine();
                try {
                    Thread.sleep(1000L);
                }
                catch(InterruptedException interruptedexception) { }
                showPageKLine();
            } else {
                UserCommand("60");
            }
        }
    }

    public String getAppletInfo() {
        return "Applet Information \u91D1\u7F51\u5B89\u6CF0";
    }

    public String[][] getParameterInfo() {
        return null;
    }

    public Frame getParentFrame(Component c) {
        Frame mainFrame = null;
        Container dad;
        for(dad = c.getParent(); !(dad instanceof Frame) && dad != null; dad = dad.getParent());
        if(dad instanceof Frame)
            mainFrame = (Frame)dad;
        return mainFrame;
    }

    void this_keyPressed(KeyEvent e) {
        if(bInputDlgShow)
            return;
        char ch = e.getKeyChar();
        if(Character.isLetterOrDigit(ch)) {
            Frame f = getParentFrame(this);
            Rectangle rc = getBounds();
            Point p = getLocationOnScreen();
            int x = (p.x + rc.width) - 125;
            int y = (p.y + rc.height) - 160;
            InputDialog dlg = new InputDialog(f, ch, this);
            dlg.setBounds(x, y, 125, 160);
            bInputDlgShow = true;
            dlg.show();
            bInputDlgShow = false;
            String str = dlg.strCmd;
            if(str == null || str.length() == 0)
                return;
            switch(str.charAt(0)) {
            case 65: // 'A'
                UserCommand(str.substring(1));
                break;

            case 84: // 'T'
                m_strIndicator = str.substring(1);
                ((Page_KLine)mainGraph).draw_KLine.CreateIndicator();
                repaint();
                break;

            case 67: // 'C'
                rhColor = new RHColor(str.charAt(1) - 48);
                setBackground(rhColor.clBackGround);
                break;

            case 80: // 'P'
                QueryStock(str.substring(1));
                break;
            }
            repaint();
            return;
        }
        boolean bNeedRepaint = true;
        int iKey = e.getKeyCode();
        switch(iKey) {
        case 27: // '\033'
            if(mainGraph != null) {
                bNeedRepaint = mainGraph.KeyPressed(e);
                if(!bNeedRepaint && iCurrentPage != 0) {
                    byte type = (byte)Common.GetProductType(strCurrentCode);
                    if(iCurrentPage == 5)
                        type = ((Page_MarketStatus)mainGraph).currentStockType;
                    mainGraph = new Page_MultiQuote(m_rcMain, this, type);
                    bNeedRepaint = true;
                }
            }
            break;

        case 112: // 'p'
            if(iCurrentPage == 1 || iCurrentPage == 2)
                UserCommand("01");
            break;

        case 113: // 'q'
            UserCommand("60");
            break;

        case 114: // 'r'
            if(indexMainCode.length() > 0)
                UserCommand("INDEX_" + indexMainCode);
            break;

        case 115: // 's'
            UserCommand("80");
            break;

        case 116: // 't'
            OnF5();
            break;

        case 118: // 'v'
            UserCommand("70");
            break;

        default:
            if(mainGraph != null)
                bNeedRepaint = mainGraph.KeyPressed(e);
            break;
        }
        if(bNeedRepaint)
            repaint();
    }

    void ChangeStock(boolean bUp, boolean bIgnoreStatus) {
        int iIndex = -1;
        for(int i = 0; i < m_codeList.size(); i++) {
            if(!strCurrentCode.equals(m_codeList.elementAt(i)))
                continue;
            iIndex = i;
            break;
        }

        if(iIndex == -1) {
            if(m_codeList.size() > 0)
                strCurrentCode = (String)m_codeList.elementAt(0);
        } else {
            if(bUp) {
                if(--iIndex < 0)
                    iIndex = m_codeList.size() - 1;
            } else
            if(++iIndex >= m_codeList.size())
                iIndex = 0;
            strCurrentCode = (String)m_codeList.elementAt(iIndex);
        }
        if(!bIgnoreStatus) {
            CodeTable s = (CodeTable)m_htProduct.get(strCurrentCode);
            if(s.status == 1 || s.status == 4) {
                ChangeStock(bUp, bIgnoreStatus);
                return;
            }
        }
        if(1 == iCurrentPage)
            mainGraph = new Page_MinLine(m_rcMain, this);
        else
        if(2 == iCurrentPage)
            mainGraph = new Page_KLine(m_rcMain, this);
    }

    void this_mouseLeftPressed(MouseEvent e) {
        if(mainGraph == null)
            return;
        if(mainGraph.MouseLeftClicked(e.getX(), e.getY()))
            repaint();
    }

    void this_mouseRightReleased(MouseEvent e) {
        if(mainGraph == null) {
            return;
        } else {
            mainGraph.processMenuEvent(popupMenu, e.getX(), e.getY());
            return;
        }
    }

    void this_mouseLeftDblClicked(MouseEvent e) {
        if(mainGraph == null)
            return;
        if(mainGraph.MouseLeftDblClicked(e.getX(), e.getY()))
            repaint();
    }

    void this_mouseMoved(MouseEvent e) {
        if(mainGraph == null)
            return;
        if(mainGraph.MouseMoved(e.getX(), e.getY()))
            repaint();
    }

    void this_mouseDragged(MouseEvent e) {
        if(mainGraph == null)
            return;
        if(mainGraph.MouseDragged(e.getX(), e.getY()))
            repaint();
    }

    void this_componentResized(ComponentEvent e) {
        Dimension d = getSize();
        m_rcMain = new Rectangle(d);
        m_rcBottom = new Rectangle(d);
        m_rcMain.height -= 20;
        m_rcBottom.y = m_rcMain.y + m_rcMain.height;
        m_rcBottom.height = 20;
        if(mainGraph != null)
            mainGraph.m_rc = m_rcMain;
        if(bottomGraph != null)
            bottomGraph.rc = m_rcBottom;
    }

    void QueryStock(String sCode) {
        strCurrentCode = sCode;
        if(2 == iCurrentPage)
            mainGraph = new Page_KLine(m_rcMain, this);
        else
            mainGraph = new Page_MinLine(m_rcMain, this);
    }

    void UserCommand(String sCmd) {
        if(sCmd.equals("about") && !bAboutDlgShow) {
            Frame f = getParentFrame(this);
            Rectangle rc = getBounds();
            Point p = getLocationOnScreen();
            int x = (p.x + rc.width / 2) - 110;
            int y = (p.y + rc.height / 2) - 60;
            AboutDialog dlg = new AboutDialog(f, this);
            dlg.setBounds(x, y, 220, 120);
            bAboutDlgShow = true;
            dlg.show();
            bAboutDlgShow = false;
            return;
        }
        if(sCmd.startsWith("INDEX_")) {
            strCurrentCode = sCmd.substring(6);
            mainGraph = new Page_KLine(m_rcMain, this);
            return;
        }
        if(sCmd.startsWith("SERIES_")) {
            strCurrentCode = sCmd.substring(7);
            mainGraph = new Page_KLine(m_rcMain, this);
            return;
        }
        if(sCmd.equals("page_history")) {
            UserCommand("70");
            return;
        }
        int iCmd = Integer.parseInt(sCmd);
        if(bDebug != 0)
            System.out.println("sCmd = ==" + iCmd);
        switch(iCmd) {
        case 1: // '\001'
            mainGraph = new Page_Bill(m_rcMain, this);
            break;

        case 5: // '\005'
            OnF5();
            break;

        case 60: // '<'
            mainGraph = new Page_MultiQuote(m_rcMain, this, (byte)1);
            break;

        case 80: // 'P'
            mainGraph = new Page_MarketStatus(m_rcMain, this, (byte)1);
            break;

        case 70: // 'F'
            mainGraph = new Page_History(m_rcMain, this);
            break;
        }
    }

    void ShowMutilQuote(byte stockType) {
        mainGraph = new Page_MultiQuote(m_rcMain, this, stockType);
        repaint();
    }

    void OnF5() {
        if(strCurrentCode.length() == 0)
            return;
        if(1 == iCurrentPage) {
            mainGraph = new Page_KLine(m_rcMain, this);
            iCurrentPage = 2;
        } else {
            mainGraph = new Page_MinLine(m_rcMain, this);
            iCurrentPage = 1;
        }
    }

    void showPageMinLine(String stockCode) {
        strCurrentCode = stockCode;
        mainGraph = new Page_MinLine(m_rcMain, this);
        iCurrentPage = 1;
        repaint();
    }

    void showPageKLine(String stockCode) {
        strCurrentCode = stockCode;
        mainGraph = new Page_KLine(m_rcMain, this);
        iCurrentPage = 2;
        repaint();
    }

    void showPageMinLine() {
        mainGraph = new Page_MinLine(m_rcMain, this);
        iCurrentPage = 1;
        repaint();
    }

    void showPageKLine() {
        mainGraph = new Page_KLine(m_rcMain, this);
        iCurrentPage = 2;
        repaint();
    }

    void repaintBottom() {
        if(bottomGraph != null)
            bottomGraph.Paint();
    }

    void Repaint(Rectangle rc) {
        repaint(rc.x, rc.y, rc.width + 1, rc.height + 1);
    }

    public void focusLost(FocusEvent focusevent) {
    }

    public void focusGained(FocusEvent event) {
        repaint();
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        if(m_rcMain == null)
            return;
        m_bEndPaint = false;
        try {
            m_img = createImage(m_rcMain.width, m_rcMain.height);
        }
        catch(Exception exception) { }
        if(mainGraph != null) {
            Graphics myG = m_img.getGraphics();
            if(myG != null) {
                myG.clearRect(0, 0, m_rcMain.width, m_rcMain.height);
                mainGraph.Paint(myG);
            }
        }
        EndPaint();
        if(bottomGraph != null)
            bottomGraph.Paint();
    }

    public void EndPaint() {
        if(!m_bEndPaint) {
            getGraphics().setPaintMode();
            getGraphics().drawImage(m_img, m_rcMain.x, m_rcMain.y, this);
        }
        m_bEndPaint = true;
    }

    public void destroy() {
        mainGraph.stopFlag = true;
        mainGraph = null;
        bRunning = false;
        httpThread.AskForData(null);
        sendThread.AskForData(null);
        try {
            if(socket != null)
                socket.close();
        }
        catch(IOException e) {
            System.out.println("eroo111");
            e.printStackTrace();
        }
        socket = null;
        httpThread = null;
        receiveThread = null;
        if(bDebug != 0)
            System.out.println("destroy HQApplet ");
    }

    int GetPrecision(String sCode) {
        int iType = getProductType(sCode);
        switch(iType) {
        case 2: // '\002'
        case 3: // '\003'
            return m_iPrecisionIndex;
        }
        return m_iPrecision;
    }

    int getProductType(String code) {
        CodeTable codeTable = (CodeTable)m_htProduct.get(code);
        if(codeTable == null)
            return -1;
        else
            return codeTable.status;
    }

    boolean isIndex(String code) {
        int iType = getProductType(code);
        return iType == 2 || iType == 3;
    }

    String getShowString(String key) {
        String strShow = "";
        try {
            String s = m_resourceBundle.getString(key);
            strShow = new String(s.getBytes("8859_1"), "GB2312");
        }
        catch(Exception exception) { }
        return strShow;
    }

}
