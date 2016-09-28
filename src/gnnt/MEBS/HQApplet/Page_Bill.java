// NMI's Java Code Viewer 6.0a
// www.trinnion.com/javacodeviewer

// Registered to Evaluation Copy                                      
// Generated PGFZKD AyTB 14 2007 15:44:19 

//source File Name:   Page_Bill.java

package gnnt.MEBS.HQApplet;

import gnnt.MEBS.hq.BillDataVO;
import gnnt.MEBS.hq.ProductDataVO;
import gnnt.util.service.HQVO.CMDBillVO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Hashtable;
import java.util.Vector;

// Referenced classes of package gnnt.MEBS.HQApplet:
//            Page_Main, MenuListener, HQApplet, ProductData, 
//            SendThread, BillFieldInfo, CodeTable, RHColor, 
//            Common

public class Page_Bill extends Page_Main {

    static final int ROW_NUM = 3;
    BillFieldInfo fieldInfo[];
    float zoomRate;
    int fieldWidth;
    int totalPages;
    int curPageNo;
    int iRows;
    ProductData stock;
    static final Font fntTitle = new Font("\u6977\u4F53", 1, 20);
    static final Font fntText = new Font("\u5B8B\u4F53", 0, 16);
    int rowHeight;
    int startY;
    int iProductType;
    MenuItem menuPageMinLine;
    MenuItem menuPageKLine;
    MenuItem menuQuote;
    MenuItem menuMarket;

    void AskForDataOnTimer() {
        ProductData stock = super.m_applet.GetProductData(super.m_applet.strCurrentCode);
        CMDBillVO packet = new CMDBillVO();
        packet.code = super.m_applet.strCurrentCode;
        packet.type = 0;
        if(stock != null && stock.vBill != null && stock.vBill.size() > 0)
            packet.time = ((BillDataVO)stock.vBill.lastElement()).time;
        super.m_applet.sendThread.AskForData(packet);
    }

    void AskForDataOnce() {
        CMDBillVO packet = new CMDBillVO();
        packet.code = super.m_applet.strCurrentCode;
        packet.type = 0;
        packet.time = 0;
        super.m_applet.sendThread.AskForData(packet);
    }

    public Page_Bill(Rectangle _rc, HQApplet applet) {
        super(_rc, applet);
        zoomRate = 1.0F;
        curPageNo = 0;
        iRows = 0;
        super.m_applet.iCurrentPage = 4;
        AskForDataOnce();
        makeMenus();
        iProductType = super.m_applet.getProductType(super.m_applet.strCurrentCode);
        fieldInfo = (new BillFieldInfo[] {
            new BillFieldInfo(super.m_applet.getShowString("Time"), true, 64), new BillFieldInfo(super.m_applet.getShowString("Price"), true, 70), new BillFieldInfo(super.m_applet.getShowString("CurVol"), true, 60), new BillFieldInfo(super.m_applet.getShowString("Dingli"), true, 50), new BillFieldInfo(super.m_applet.getShowString("ZhuanRang"), true, 50)
        });
    }

    void Paint(Graphics g) {
        initVisibleField();
        initPageInfo(g);
        paintTitle(g);
        paintBillData(g);
    }

    void paintTitle(Graphics g) {
        FontMetrics fm = null;
        String sCode = "";
        if(stock != null)
            sCode = stock.sCode;
        CodeTable stockTable = (CodeTable)super.m_applet.m_htProduct.get(sCode);
        String title = "";
        String sName = "";
        if(stockTable != null)
            sName = stockTable.sName;
        if(sName.equals(sCode))
            sName = "";
        title = title + sName + " " + sCode + " " + super.m_applet.getShowString("TradeList");
        int x = super.m_rc.x;
        int y = super.m_rc.y;
        g.setFont(fntTitle);
        fm = g.getFontMetrics();
        g.setColor(HQApplet.rhColor.clProductName);
        x += (super.m_rc.width - fm.stringWidth(title)) / 2;
        if(x < 0)
            x = 0;
        g.drawString(title, x, y + fm.getAscent());
        x = super.m_rc.x;
        y = super.m_rc.y + fm.getHeight();
        g.setColor(HQApplet.rhColor.clGrid);
        g.drawRect(x, y, (x + super.m_rc.width) - 1, super.m_rc.height - fm.getHeight());
        for(int i = 1; i < 3; i++)
            g.drawLine(x + (super.m_rc.width / 3) * i, y, x + (super.m_rc.width / 3) * i, (y + super.m_rc.height) - fm.getHeight());

        g.setFont(fntText);
        fm = g.getFontMetrics();
        g.drawLine(x, y + fm.getHeight() + 2, (x + super.m_rc.width) - 1, y + fm.getHeight() + 2);
        startY = y + fm.getHeight() + 4 + fm.getAscent();
        g.setColor(HQApplet.rhColor.clItem);
        int fieldNum = fieldInfo.length;
        y += fm.getAscent() + 1;
        for(int i = 0; i < 3; i++) {
            x = super.m_rc.x + (super.m_rc.width / 3) * i;
            for(int j = 0; j < fieldNum; j++) {
                if(!fieldInfo[j].visible)
                    break;
                x = (int)((float)x + (float)fieldInfo[j].width * zoomRate);
                String str = fieldInfo[j].name;
                int strWidth = fm.stringWidth(str);
                g.drawString(str, x - strWidth, y);
            }

        }

        String strText = super.m_applet.getShowString("PagePrefix") + (totalPages - curPageNo) + super.m_applet.getShowString("PageSuffix") + " " + super.m_applet.getShowString("TotalPagePrefix") + totalPages + super.m_applet.getShowString("TotalPageSuffix");
        g.setColor(HQApplet.rhColor.clGrid);
        g.drawString(strText, (super.m_rc.x + super.m_rc.width) - fm.stringWidth(strText), (super.m_rc.y + y) - fm.getHeight() - fm.getDescent());
    }

    void paintBillData(Graphics g) {
        if(stock == null || stock.realData == null || stock.vBill == null)
            return;
        int iPrecision = super.m_applet.GetPrecision(stock.sCode);
        g.setFont(fntText);
        FontMetrics fm = g.getFontMetrics();
        int iShow = iRows * 3;
        int iSize = stock.vBill.size();
        if(iSize <= 0)
            return;
        if(iSize < iShow)
            iShow = iSize;
        int x = super.m_rc.x;
        int y = startY;
        int iStart = iSize - iShow * (curPageNo + 1);
        if(iStart < 0)
            iStart = 0;
        int iEnd = iStart + iShow;
        if(iEnd > iSize) {
            iEnd = iSize;
            iStart = iEnd - iShow;
            if(iStart <= 0)
                iStart = 1;
        }
        for(int i = iStart; i < iEnd; i++) {
            x = super.m_rc.x;
            x += (super.m_rc.width / 3) * ((i - iStart) / iRows);
            if((i - iStart) % iRows == 0)
                y = startY;
            BillDataVO billPre = null;
            if(i > 0)
                billPre = (BillDataVO)stock.vBill.elementAt(i - 1);
            BillDataVO bill = (BillDataVO)stock.vBill.elementAt(i);
            if(fieldInfo[0].visible) {
                DecimalFormat df = new DecimalFormat("#,#0");
                String str = df.format(bill.time);
                if(str.length() != 8)
                    str = "0" + str;
                str = str.replace(',', ':');
                g.setColor(HQApplet.rhColor.clNumber);
                x = (int)((float)x + (float)fieldInfo[0].width * zoomRate);
                g.drawString(str, x - fm.stringWidth(str), y);
            }
            if(fieldInfo[1].visible) {
                String str = Common.FloatToString(bill.curPrice, iPrecision);
                if(bill.curPrice > stock.realData.yesterBalancePrice)
                    g.setColor(HQApplet.rhColor.clIncrease);
                else
                if(bill.curPrice < stock.realData.yesterBalancePrice)
                    g.setColor(HQApplet.rhColor.clDecrease);
                else
                    g.setColor(HQApplet.rhColor.clEqual);
                x = (int)((float)x + (float)fieldInfo[1].width * zoomRate);
                g.drawString(str, x - fm.stringWidth(str), y);
            }
            if(fieldInfo[2].visible) {
                String str;
                if(billPre == null)
                    str = String.valueOf(bill.totalAmount);
                else
                    str = String.valueOf((int)(bill.totalAmount - billPre.totalAmount));
                g.setColor(HQApplet.rhColor.clVolume);
                x = (int)((float)x + ((float)fieldInfo[2].width * zoomRate - 16F));
                g.drawString(str, x - fm.stringWidth(str), y);
                if(iProductType != 2 && iProductType != 3) {
                    byte ask;
                    if(billPre == null)
                        ask = 2;
                    else
                    if(billPre.buyPrice <= 0.001F)
                        ask = 1;
                    else
                    if(bill.curPrice >= billPre.sellPrice)
                        ask = 0;
                    else
                    if(bill.curPrice <= billPre.buyPrice) {
                        ask = 1;
                    } else {
                        int sell = (int)((billPre.sellPrice - bill.curPrice) * 1000F);
                        float buy = (int)((bill.curPrice - billPre.buyPrice) * 1000F);
                        if((float)sell < buy)
                            ask = 0;
                        else
                        if((float)sell > buy)
                            ask = 1;
                        else
                            ask = 2;
                    }
                    if(ask == 0) {
                        str = "\u2191";
                        g.setColor(HQApplet.rhColor.clIncrease);
                    } else
                    if(ask == 1) {
                        str = "\u2193";
                        g.setColor(HQApplet.rhColor.clDecrease);
                    } else {
                        str = "\u2013";
                        g.setColor(HQApplet.rhColor.clEqual);
                    }
                    g.drawString(str, x, y);
                }
            }
            if(fieldInfo[3].visible) {
                String str = String.valueOf(bill.openAmount);
                x = (int)((float)x + (float)fieldInfo[3].width * zoomRate);
                g.setColor(HQApplet.rhColor.clNumber);
                g.drawString(str, x - fm.stringWidth(str), y);
            }
            if(fieldInfo[4].visible) {
                String str = String.valueOf(bill.closeAmount);
                x = (int)((float)x + (float)fieldInfo[4].width * zoomRate);
                g.setColor(HQApplet.rhColor.clNumber);
                g.drawString(str, x - fm.stringWidth(str), y);
            }
            y += rowHeight;
        }

    }

    boolean KeyPressed(KeyEvent e) {
        int iKeyCode = e.getKeyCode();
        switch(iKeyCode) {
        default:
            break;

        case 34: // '"'
            if(curPageNo > 0) {
                curPageNo--;
                return true;
            }
            break;

        case 33: // '!'
            if(curPageNo < totalPages - 1) {
                curPageNo++;
                return true;
            }
            break;
        }
        return false;
    }

    void initPageInfo(Graphics g) {
        stock = super.m_applet.GetProductData(super.m_applet.strCurrentCode);
        if(stock == null || stock.realData == null || stock.vBill == null)
            return;
        int iSize = stock.vBill.size();
        if(iSize <= 0)
            return;
        FontMetrics fm = g.getFontMetrics(fntTitle);
        int titleHeight = fm.getHeight();
        fm = g.getFontMetrics(fntText);
        int fontHeight = fm.getHeight();
        rowHeight = fontHeight + 2;
        iRows = (super.m_rc.height - titleHeight - fontHeight - 6) / rowHeight;
        int totalPages = (iSize - 1) / (iRows * 3);
        if((iSize - 1) % (iRows * 3) != 0)
            totalPages++;
        if(totalPages != this.totalPages) {
            this.totalPages = totalPages;
            curPageNo = 0;
        }
    }

    void initVisibleField() {
        int maxWidth = super.m_rc.width / 3 - 4;
        if(maxWidth < 0)
            maxWidth = 0;
        int usedWidth = 0;
        int visibleFieldCount = 0;
        int fieldNum = fieldInfo.length;
        for(int i = 0; i < fieldNum; i++) {
            if(usedWidth + fieldInfo[i].width < maxWidth) {
                fieldInfo[i].visible = true;
                visibleFieldCount++;
                usedWidth += fieldInfo[i].width;
                continue;
            }
            for(int j = i; j < fieldNum; j++)
                fieldInfo[j].visible = false;

            break;
        }

        if(visibleFieldCount <= 0)
            visibleFieldCount = 1;
        if(visibleFieldCount == fieldNum)
            zoomRate = (float)maxWidth / (float)usedWidth;
    }

    private void makeMenus() {
        menuPageMinLine = new MenuItem(super.m_applet.getShowString("MinLine") + "  F5");
        menuPageMinLine.setActionCommand("minline");
        menuPageMinLine.addActionListener(this);
        menuPageKLine = new MenuItem(super.m_applet.getShowString("Analysis"));
        menuPageKLine.setActionCommand("kline");
        menuPageKLine.addActionListener(this);
        menuQuote = new MenuItem(super.m_applet.getShowString("MultiQuote") + "  F2");
        menuQuote.setActionCommand("cmd_quote");
        menuQuote.addActionListener(this);
        menuMarket = new MenuItem(super.m_applet.getShowString("ClassedList") + "  F4");
        menuMarket.setActionCommand("cmd_market");
        menuMarket.addActionListener(this);
    }

    public void processMenuEvent(PopupMenu popupMenu, int x, int y) {
        popupMenu.removeAll();
        popupMenu.add(menuPageMinLine);
        popupMenu.add(menuPageKLine);
        popupMenu.addSeparator();
        popupMenu.add(menuQuote);
        popupMenu.add(menuMarket);
        processCommonMenuEvent(popupMenu, this);
        popupMenu.show(super.m_applet, x, y);
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if(cmd.indexOf("cmd_") >= 0) {
            String requestType = cmd.substring(4);
            if(requestType.equals("quote"))
                executeQuoteCommand();
            else
            if(requestType.equals("market"))
                executeMarketCommand();
        } else
        if(cmd.equals("minline"))
            super.m_applet.showPageMinLine();
        else
        if(cmd.equals("kline"))
            super.m_applet.showPageKLine();
        else
            super.actionPerformed(e);
    }

    private void executeQuoteCommand() {
        switch(iProductType) {
        case 1: // '\001'
            super.m_applet.UserCommand("60");
            break;

        default:
            super.m_applet.UserCommand("60");
            break;
        }
    }

    private void executeMarketCommand() {
        switch(iProductType) {
        case 1: // '\001'
            super.m_applet.UserCommand("80");
            break;

        default:
            super.m_applet.UserCommand("80");
            break;
        }
    }

}
