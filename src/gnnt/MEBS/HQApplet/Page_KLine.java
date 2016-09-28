// NMI's Java Code Viewer 6.0a
// www.trinnion.com/javacodeviewer

// Registered to Evaluation Copy                                      
// Generated PGFZKD AyTB 14 2007 15:44:19 

//source File Name:   Page_KLine.java

package gnnt.MEBS.HQApplet;

import gnnt.MEBS.HQApplet.Indicator.IndicatorBase;
import gnnt.MEBS.hq.BillDataVO;
import gnnt.MEBS.hq.ProductDataVO;
import gnnt.util.service.HQVO.CMDBillVO;
import gnnt.util.service.HQVO.CMDMinVO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Vector;

// Referenced classes of package gnnt.MEBS.HQApplet:
//            Page_Main, MenuListener, HQApplet, Draw_KLine, 
//            Draw_MinLine, ProductData, SendThread, Packet_HttpRequest, 
//            HttpThread, Draw_Quote

public class Page_KLine extends Page_Main {

    static int m_iCurKLineType = 0;
    int iProductType;
    Draw_KLine draw_KLine;
    Draw_MinLine draw_MinLine;
    Rectangle rcKLine;
    Menu menuKType;
    MenuItem menuKTypeK;
    MenuItem menuKTypeUSA;
    MenuItem menuKTypePoly;
    Menu menuIndicator;
    Menu menuCycle;
    MenuItem menuQuote;
    MenuItem menuMarket;
    MenuItem menuZoomIn;
    MenuItem menuZoomOut;
    MenuItem menuPrevStock;
    MenuItem menuPostStock;
    MenuItem menuMinLine;
    MenuItem menuBill;
    MenuItem menuCycleDay;
    MenuItem menuCycleWeek;
    MenuItem menuCycleMonth;
    MenuItem menuCycleMin5;
    MenuItem menuCycleMin15;
    MenuItem menuCycleMin30;
    MenuItem menuCycleMin60;

    public Page_KLine(Rectangle _rc, HQApplet applet) {
        super(_rc, applet);
        AskForDataOnce();
        super.m_applet.iCurrentPage = 2;
        draw_KLine = new Draw_KLine(this);
        draw_MinLine = new Draw_MinLine(applet, false);
        makeMenus();
        iProductType = super.m_applet.getProductType(super.m_applet.strCurrentCode);
    }

    void AskForDataOnTimer() {
        ProductData stock = super.m_applet.GetProductData(super.m_applet.strCurrentCode);
        java.util.Date time = null;
        if(stock != null && stock.realData != null)
            time = stock.realData.time;
        SendThread.AskForRealQuote(super.m_applet.strCurrentCode, time, super.m_applet.sendThread);
        CMDBillVO packet = new CMDBillVO();
        packet.type = 0;
        packet.code = super.m_applet.strCurrentCode;
        if(stock != null && stock.vBill != null && stock.vBill.size() > 0)
            packet.time = ((BillDataVO)stock.vBill.lastElement()).time;
        super.m_applet.sendThread.AskForData(packet);
    }

    void AskForDataOnce() {
        ProductData stock = super.m_applet.GetProductData(super.m_applet.strCurrentCode);
        java.util.Date time = null;
        if(stock != null && stock.realData != null)
            time = stock.realData.time;
        SendThread.AskForRealQuote(super.m_applet.strCurrentCode, time, super.m_applet.sendThread);
        AskForKLine();
        CMDMinVO packet1 = new CMDMinVO();
        packet1.code = super.m_applet.strCurrentCode;
        packet1.type = 0;
        packet1.time = 0;
        super.m_applet.sendThread.AskForData(packet1);
        CMDBillVO packet = new CMDBillVO();
        packet.type = 0;
        packet.code = super.m_applet.strCurrentCode;
        packet.time = 0;
        super.m_applet.sendThread.AskForData(packet);
    }

    void AskForKLine() {
        ProductData stock = super.m_applet.GetProductData(super.m_applet.strCurrentCode);
        if(stock != null)
            if(1 == super.m_applet.m_iKLineCycle || 2 == super.m_applet.m_iKLineCycle || 3 == super.m_applet.m_iKLineCycle) {
                if(stock.dayKLine != null)
                    return;
            } else
            if(stock.min5KLine != null)
                return;
        Packet_HttpRequest request = new Packet_HttpRequest();
        request.sCode = super.m_applet.strCurrentCode;
        if(1 == super.m_applet.m_iKLineCycle || 2 == super.m_applet.m_iKLineCycle || 3 == super.m_applet.m_iKLineCycle)
            request.type = 0;
        else
            request.type = 1;
        super.m_applet.httpThread.AskForData(request);
    }

    void Paint(Graphics g) {
        ProductData stock = super.m_applet.GetProductData(super.m_applet.strCurrentCode);
        g.setFont(new Font("\u6977\u4F53_GB2312", 1, 26));
        FontMetrics fm = g.getFontMetrics();
        int iQuoteH = fm.getHeight();
        g.setFont(new Font("\u5B8B\u4F53", 0, 16));
        fm = g.getFontMetrics();
        iQuoteH += 9 * fm.getHeight() + super.m_applet.iShowBuySellPrice * 2 * fm.getHeight();
        Rectangle rcQuote = new Rectangle((super.m_rc.x + super.m_rc.width) - 200, super.m_rc.y, 200, iQuoteH);
        if(rcQuote.height > super.m_rc.height)
            rcQuote.height = super.m_rc.height;
        Draw_Quote.Paint(g, rcQuote, stock, super.m_applet.strCurrentCode, super.m_applet.iShowBuySellPrice, super.m_applet);
        if(super.m_rc.height > iQuoteH + fm.getHeight() * 3) {
            Rectangle rcMinLine = new Rectangle((super.m_rc.x + super.m_rc.width) - 200, super.m_rc.y + iQuoteH, 200, (super.m_rc.height - iQuoteH) + 1);
            draw_MinLine.Paint(g, rcMinLine, stock);
        }
        rcKLine = new Rectangle(super.m_rc.x, super.m_rc.y, super.m_rc.width - 200, super.m_rc.height);
        draw_KLine.Paint(g, rcKLine, stock);
    }

    boolean KeyPressed(KeyEvent e) {
        int iKeyCode = e.getKeyCode();
        boolean bResult;
        switch(iKeyCode) {
        case 34: // '"'
            super.m_applet.ChangeStock(false, true);
            bResult = true;
            break;

        case 33: // '!'
            super.m_applet.ChangeStock(true, true);
            bResult = true;
            break;

        default:
            bResult = draw_KLine.KeyPressed(e);
            break;
        }
        return bResult;
    }

    boolean MouseLeftClicked(int x, int y) {
        if(rcKLine != null && rcKLine.contains(x, y) && draw_KLine != null)
            return draw_KLine.MouseLeftClicked(x, y);
        else
            return false;
    }

    boolean MouseDragged(int x, int y) {
        if(rcKLine != null && rcKLine.contains(x, y) && draw_KLine != null)
            return draw_KLine.MouseDragged(x, y);
        else
            return false;
    }

    void makeMenus() {
        menuKType = new Menu(super.m_applet.getShowString("LineType"));
        menuKTypeK = new MenuItem(super.m_applet.getShowString("KLine"));
        menuKTypeUSA = new MenuItem(super.m_applet.getShowString("USALine"));
        menuKTypePoly = new MenuItem(super.m_applet.getShowString("PolyLine"));
        menuIndicator = new Menu(super.m_applet.getShowString("Indicator"));
        menuCycle = new Menu(super.m_applet.getShowString("AnalysisCycle") + "  F8");
        menuQuote = new MenuItem(super.m_applet.getShowString("MultiQuote") + "  F2");
        menuMarket = new MenuItem(super.m_applet.getShowString("ClassedList") + "  F4");
        menuZoomIn = new MenuItem(super.m_applet.getShowString("ZooMIn"));
        menuZoomOut = new MenuItem(super.m_applet.getShowString("ZoomOut"));
        menuPrevStock = new MenuItem(super.m_applet.getShowString("PrevCommodity") + "  PageUp");
        menuPostStock = new MenuItem(super.m_applet.getShowString("NextCommodity") + "  PageDown");
        menuMinLine = new MenuItem(super.m_applet.getShowString("MinLine") + "  F5");
        menuBill = new MenuItem(super.m_applet.getShowString("TradeList") + "  F1");
        menuCycleDay = new MenuItem(super.m_applet.getShowString("DayLine"));
        menuCycleWeek = new MenuItem(super.m_applet.getShowString("WeekLine"));
        menuCycleMonth = new MenuItem(super.m_applet.getShowString("MonthLine"));
        menuCycleMin5 = new MenuItem(super.m_applet.getShowString("5MinLine"));
        menuCycleMin15 = new MenuItem(super.m_applet.getShowString("15MinLine"));
        menuCycleMin30 = new MenuItem(super.m_applet.getShowString("30MinLine"));
        menuCycleMin60 = new MenuItem(super.m_applet.getShowString("60MinLine"));
        int rows = IndicatorBase.INDICATOR_NAME.length;
        for(int i = 0; i < rows; i++) {
            MenuItem mi = new MenuItem();
            StringBuffer label = new StringBuffer();
            String strName = IndicatorBase.INDICATOR_NAME[i][0];
            String strFullName = super.m_applet.getShowString("T_" + IndicatorBase.INDICATOR_NAME[i][0]);
            label.append(strName);
            for(int len = strName.length(); len < 6; len++)
                label.append(" ");

            label.append(strFullName);
            mi.setLabel(label.toString());
            mi.setActionCommand("Indicator_" + strName);
            mi.addActionListener(this);
            menuIndicator.add(mi);
        }

        menuKTypeK.setActionCommand("KLine");
        menuKTypeK.addActionListener(this);
        menuKTypeUSA.setActionCommand("USA");
        menuKTypeUSA.addActionListener(this);
        menuKTypePoly.setActionCommand("Poly");
        menuKTypePoly.addActionListener(this);
        menuKType.add(menuKTypeK);
        menuKType.add(menuKTypeUSA);
        menuKType.add(menuKTypePoly);
        menuQuote.setActionCommand("cmd_quote");
        menuQuote.addActionListener(this);
        menuMarket.setActionCommand("cmd_market");
        menuMarket.addActionListener(this);
        menuZoomIn.setActionCommand("zoomin");
        menuZoomIn.addActionListener(this);
        menuZoomOut.setActionCommand("zoomout");
        menuZoomOut.addActionListener(this);
        menuPrevStock.setActionCommand("prevstock");
        menuPrevStock.addActionListener(this);
        menuPostStock.setActionCommand("poststock");
        menuPostStock.addActionListener(this);
        menuCycle.setActionCommand("cycle");
        menuCycleDay.setActionCommand("day");
        menuCycleDay.addActionListener(this);
        menuCycleWeek.setActionCommand("week");
        menuCycleWeek.addActionListener(this);
        menuCycleMonth.setActionCommand("month");
        menuCycleMonth.addActionListener(this);
        menuCycleMin5.setActionCommand("min5");
        menuCycleMin5.addActionListener(this);
        menuCycleMin15.setActionCommand("min15");
        menuCycleMin15.addActionListener(this);
        menuCycleMin30.setActionCommand("min30");
        menuCycleMin30.addActionListener(this);
        menuCycleMin60.setActionCommand("min60");
        menuCycleMin60.addActionListener(this);
        menuMinLine.setActionCommand("minline");
        menuMinLine.addActionListener(this);
        menuBill.setActionCommand("bill");
        menuBill.addActionListener(this);
        menuCycle.add(menuCycleDay);
        menuCycle.add(menuCycleWeek);
        menuCycle.add(menuCycleMonth);
        menuCycle.add(menuCycleMin5);
        menuCycle.add(menuCycleMin15);
        menuCycle.add(menuCycleMin30);
        menuCycle.add(menuCycleMin60);
    }

    void processMenuEvent(PopupMenu popupMenu, int x, int y) {
        popupMenu.removeAll();
        popupMenu.add(menuKType);
        popupMenu.add(menuIndicator);
        popupMenu.add(menuCycle);
        popupMenu.addSeparator();
        if(iProductType != 4)
            popupMenu.add(menuMinLine);
        if(iProductType != 2 && iProductType != 3 && iProductType != 4)
            popupMenu.add(menuBill);
        if(iProductType != 4)
            popupMenu.addSeparator();
        popupMenu.add(menuZoomIn);
        popupMenu.add(menuZoomOut);
        popupMenu.addSeparator();
        popupMenu.add(menuPrevStock);
        popupMenu.add(menuPostStock);
        popupMenu.addSeparator();
        popupMenu.add(menuQuote);
        popupMenu.add(menuMarket);
        setAllMenusEnable();
        processCommonMenuEvent(popupMenu, this);
        popupMenu.show(super.m_applet, x, y);
    }

    void setMenuEnable(String label, boolean b) {
        if(label.equals("zoomout")) {
            menuZoomOut.setEnabled(b);
            return;
        }
        if(label.equals("zoomin")) {
            menuZoomIn.setEnabled(b);
            return;
        } else {
            return;
        }
    }

    void setIndicatorSubMenusAllTrue() {
        int count = menuIndicator.getItemCount();
        for(int i = 0; i < count; i++) {
            MenuItem mi = menuIndicator.getItem(i);
            mi.setEnabled(true);
        }

    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if("USA".equals(cmd)) {
            m_iCurKLineType = 1;
            draw_KLine.ChangeKLineType(1);
            return;
        }
        if("KLine".equals(cmd)) {
            m_iCurKLineType = 0;
            draw_KLine.ChangeKLineType(0);
            return;
        }
        if("Poly".equals(cmd)) {
            m_iCurKLineType = 2;
            draw_KLine.ChangeKLineType(2);
            return;
        }
        if(cmd.indexOf("cmd_") >= 0) {
            String requestType = cmd.substring(4);
            if(requestType.equals("quote")) {
                executeQuoteCommand();
                return;
            }
            if(requestType.equals("market")) {
                executeMarketCommand();
                return;
            }
        } else {
            if(cmd.equals("zoomin")) {
                zoomInKLineGraph();
                return;
            }
            if(cmd.equals("zoomout")) {
                zoomOutKLineGraph();
                return;
            }
            if(cmd.equals("prevstock")) {
                super.m_applet.ChangeStock(true, true);
                super.m_applet.repaint();
                return;
            }
            if(cmd.equals("poststock")) {
                super.m_applet.ChangeStock(false, true);
                super.m_applet.repaint();
                return;
            }
            if(cmd.equals("day")) {
                super.m_applet.m_iKLineCycle = 1;
                AskForKLine();
                super.m_applet.repaint();
                return;
            }
            if(cmd.equals("week")) {
                super.m_applet.m_iKLineCycle = 2;
                AskForKLine();
                super.m_applet.repaint();
                return;
            }
            if(cmd.equals("month")) {
                super.m_applet.m_iKLineCycle = 3;
                AskForKLine();
                super.m_applet.repaint();
                return;
            }
            if(cmd.equals("min5")) {
                super.m_applet.m_iKLineCycle = 4;
                AskForKLine();
                super.m_applet.repaint();
                return;
            }
            if(cmd.equals("min15")) {
                super.m_applet.m_iKLineCycle = 5;
                AskForKLine();
                super.m_applet.repaint();
                return;
            }
            if(cmd.equals("min30")) {
                super.m_applet.m_iKLineCycle = 6;
                AskForKLine();
                super.m_applet.repaint();
                return;
            }
            if(cmd.equals("min60")) {
                super.m_applet.m_iKLineCycle = 7;
                AskForKLine();
                super.m_applet.repaint();
                return;
            }
            if(cmd.equals("minline")) {
                super.m_applet.showPageMinLine();
                return;
            }
            if(cmd.equals("bill")) {
                super.m_applet.UserCommand("01");
                return;
            }
            if(cmd.startsWith("Indicator_")) {
                super.m_applet.m_strIndicator = cmd.substring(10);
                draw_KLine.CreateIndicator();
                super.m_applet.repaint();
            } else {
                super.actionPerformed(e);
            }
        }
    }

    private void zoomOutKLineGraph() {
        boolean bNeedRepaint = draw_KLine.ChangeRatio(false);
        if(bNeedRepaint) {
            if(!draw_KLine.ChangeRatio(false))
                menuZoomOut.setEnabled(false);
            else
                draw_KLine.ChangeRatio(true);
            if(!menuZoomIn.isEnabled())
                menuZoomIn.setEnabled(true);
            super.m_applet.repaint();
        }
    }

    private void zoomInKLineGraph() {
        boolean bNeedRepaint = draw_KLine.ChangeRatio(true);
        if(bNeedRepaint) {
            if(!draw_KLine.ChangeRatio(true))
                menuZoomIn.setEnabled(false);
            else
                draw_KLine.ChangeRatio(false);
            if(!menuZoomOut.isEnabled())
                menuZoomOut.setEnabled(true);
            super.m_applet.repaint();
        }
    }

    void setCycleSubMenusAllTure() {
        menuCycleDay.setEnabled(true);
        menuCycleWeek.setEnabled(true);
        menuCycleMonth.setEnabled(true);
        menuCycleMin5.setEnabled(true);
        menuCycleMin15.setEnabled(true);
        menuCycleMin30.setEnabled(true);
        menuCycleMin60.setEnabled(true);
    }

    void setAllMenusEnable() {
        int iCount = menuIndicator.getItemCount();
        for(int i = 0; i < iCount; i++) {
            MenuItem mi = menuIndicator.getItem(i);
            String indicator = mi.getActionCommand();
            if(super.m_applet.m_strIndicator.equals(indicator))
                mi.setEnabled(false);
            else
                mi.setEnabled(true);
        }

        menuKTypeK.setEnabled(m_iCurKLineType != 0);
        menuKTypeUSA.setEnabled(m_iCurKLineType != 1);
        menuKTypePoly.setEnabled(m_iCurKLineType != 2);
        setCycleSubMenusAllTure();
        switch(super.m_applet.m_iKLineCycle) {
        case 1: // '\001'
            menuCycleDay.setEnabled(false);
            break;

        case 2: // '\002'
            menuCycleWeek.setEnabled(false);
            break;

        case 3: // '\003'
            menuCycleMonth.setEnabled(false);
            break;

        case 4: // '\004'
            menuCycleMin5.setEnabled(false);
            break;

        case 5: // '\005'
            menuCycleMin15.setEnabled(false);
            break;

        case 6: // '\006'
            menuCycleMin30.setEnabled(false);
            break;

        case 7: // '\007'
            menuCycleMin60.setEnabled(false);
            break;
        }
    }

    private void executeQuoteCommand() {
        super.m_applet.UserCommand("60");
    }

    private void executeMarketCommand() {
        super.m_applet.UserCommand("80");
    }

}
