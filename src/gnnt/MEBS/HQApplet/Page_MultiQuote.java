// NMI's Java Code Viewer 6.0a
// www.trinnion.com/javacodeviewer

// Registered to Evaluation Copy                                      
// Generated PGFZKD AyTB 14 2007 15:44:19 

//source File Name:   Page_MultiQuote.java

package gnnt.MEBS.HQApplet;

import gnnt.MEBS.hq.ProductDataVO;
import gnnt.util.service.HQVO.CMDSortVO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.PrintStream;
import java.util.Hashtable;

// Referenced classes of package gnnt.MEBS.HQApplet:
//            Page_Main, MenuListener, HQApplet, SendThread, 
//            MultiQuoteItemInfo, Common, RHColor, Packet_MultiQuote, 
//            Arrays, CodeTable

class Page_MultiQuote extends Page_Main {

    static byte flag = 0;
    ProductDataVO backQuoteData[];
    private int iDynamicIndex;
    String strSortItem;
    int iHighlightIndex;
    int iStockRows;
    Font font;
    Font fontTitle;
    FontMetrics fm;
    int fontHeight;
    private static final int TITLE_GAP = 2;
    boolean bCanMove;
    byte currentStockType;
    byte sortBy;
    byte isDescend;
    int iStart;
    private int iEnd;
    boolean bShowUserStock;
    String iUserStockCode[];
    String backIUserStockCode[];
    Packet_MultiQuote packetInfo;
    ProductDataVO quoteData[];
    private Hashtable m_htItemInfo;
    private String m_strItems[];
    private int m_iStaticIndex;
    Menu menuStockRank;
    MenuItem menuMarket;
    MenuItem menuPageMinLine;
    MenuItem menuPageKLine;
    boolean m_bShowSortTag;

    void AskForDataOnTimer() {
        setSortStockRequestPacket();
    }

    private void setSortStockRequestPacket() {
        CMDSortVO packet = new CMDSortVO();
        packet.isDescend = isDescend;
        packet.sortBy = sortBy;
        if(iStart == iEnd)
            iEnd = iStart + 1;
        packet.start = iStart;
        packet.end = iEnd;
        HQApplet _tmp = super.m_applet;
        if(HQApplet.bDebug != 0)
            System.out.println("\u53D6\u62A5\u4EF7\u6392\u540DStart = " + iStart + "  End = " + iEnd);
        super.m_applet.sendThread.AskForData(packet);
    }

    private void setUserStockRequestPacket() {
    }

    public Page_MultiQuote(Rectangle _rc, HQApplet applet, byte currentStockType) {
        super(_rc, applet);
        strSortItem = "Code";
        iHighlightIndex = 1;
        font = new Font("\u5B8B\u4F53", 0, 16);
        fontTitle = new Font("\u5B8B\u4F53", 1, 16);
        bCanMove = true;
        sortBy = 0;
        isDescend = 0;
        iStart = 1;
        iEnd = 30;
        m_bShowSortTag = false;
        super.m_applet.iCurrentPage = 0;
        this.currentStockType = currentStockType;
        initStockFieldInfo();
        calculateRowsOfPage();
        iStart = 1;
        iEnd = iStockRows;
        flag = 0;
        setUserStockCode();
        if(this.currentStockType == 0) {
            initUserStockArray();
            bShowUserStock = true;
        }
        AskForDataOnTimer();
        makeMenus();
        setMenuEnable(this.currentStockType, false);
    }

    void initStockFieldInfo() {
        m_htItemInfo = new Hashtable();
        m_htItemInfo.put("No", new MultiQuoteItemInfo("", 20, -1));
        m_htItemInfo.put("Name", new MultiQuoteItemInfo(super.m_applet.getShowString("Name"), 80, -1));
        m_htItemInfo.put("Code", new MultiQuoteItemInfo(super.m_applet.getShowString("Code"), 64, 0));
        m_htItemInfo.put("CurPrice", new MultiQuoteItemInfo(super.m_applet.getShowString("Newly"), 60, 1));
        m_htItemInfo.put("CurAmount", new MultiQuoteItemInfo(super.m_applet.getShowString("CurVol"), 60, -1));
        m_htItemInfo.put("SellPrice", new MultiQuoteItemInfo(super.m_applet.getShowString("SellPrice"), 60, -1));
        m_htItemInfo.put("SellAmount", new MultiQuoteItemInfo(super.m_applet.getShowString("SellVol"), 48, -1));
        m_htItemInfo.put("BuyPrice", new MultiQuoteItemInfo(super.m_applet.getShowString("BuyPrice"), 60, -1));
        m_htItemInfo.put("BuyAmount", new MultiQuoteItemInfo(super.m_applet.getShowString("BuyVol"), 48, -1));
        m_htItemInfo.put("TotalAmount", new MultiQuoteItemInfo(super.m_applet.getShowString("Volume"), 75, 9));
        m_htItemInfo.put("UpValue", new MultiQuoteItemInfo(super.m_applet.getShowString("ChangeValue"), 65, 2));
        m_htItemInfo.put("UpRate", new MultiQuoteItemInfo(super.m_applet.getShowString("ChangeRate"), 65, 3));
        m_htItemInfo.put("ReverseCount", new MultiQuoteItemInfo(super.m_applet.getShowString("Order"), 70, -1));
        m_htItemInfo.put("Balance", new MultiQuoteItemInfo(super.m_applet.getShowString("Balance"), 60, -1));
        m_htItemInfo.put("OpenPrice", new MultiQuoteItemInfo(super.m_applet.getShowString("Open"), 55, -1));
        m_htItemInfo.put("HighPrice", new MultiQuoteItemInfo(super.m_applet.getShowString("High"), 55, -1));
        m_htItemInfo.put("LowPrice", new MultiQuoteItemInfo(super.m_applet.getShowString("Low"), 55, -1));
        m_htItemInfo.put("YesterBalance", new MultiQuoteItemInfo(super.m_applet.getShowString("PreBalance"), 60, -1));
        m_htItemInfo.put("TotalMoney", new MultiQuoteItemInfo(super.m_applet.getShowString("Money"), 80, 6));
        m_htItemInfo.put("AmountRate", new MultiQuoteItemInfo(super.m_applet.getShowString("VolRate"), 50, 5));
        m_htItemInfo.put("ConsignRate", new MultiQuoteItemInfo(super.m_applet.getShowString("ConsignRate"), 50, 7));
        m_htItemInfo.put("Unit", new MultiQuoteItemInfo(super.m_applet.getShowString("Unit"), 40, -1));
        String strItemName = super.m_applet.getParameter("MultiQuoteName", "Name:;Code:;CurPrice:;CurAmount:;SellPrice:;SellAmount:;BuyPrice:;BuyAmount:;TotalAmount:;UpValue:;UpRate:;ReverseCount:;Balance:;OpenPrice:;HighPrice:;LowPrice:;YesterBalance:;TotalMoney:;AmountRate:;ConsignRate:;Unit:;");
        String strItemNames[] = Common.split(strItemName, 59);
        for(int i = 0; i < strItemNames.length; i++) {
            String str[] = Common.split(strItemNames[i], 58);
            if(str.length == 2 && str[1].length() > 0) {
                MultiQuoteItemInfo itemInfo = (MultiQuoteItemInfo)m_htItemInfo.get(str[0]);
                if(itemInfo != null)
                    itemInfo.name = str[1];
            }
        }

        String strItems = super.m_applet.getParameter("MultiQuoteItems", "No;Name;Code;CurPrice;CurAmount;SellPrice;SellAmount;BuyPrice;BuyAmount;TotalAmount;UpValue;ReverseCount;Balance;OpenPrice;HighPrice;LowPrice;YesterBalance;");
        m_strItems = Common.split(strItems, 59);
        m_iStaticIndex = Integer.parseInt(super.m_applet.getParameter("MultiQuoteStaticIndex", "3"));
        iDynamicIndex = m_iStaticIndex + 1;
    }

    private void initGraph(Graphics g) {
        fm = g.getFontMetrics(font);
        fontHeight = fm.getHeight();
        g.setFont(font);
    }

    private void calculateRowsOfPage() {
        int gap = 2;
        Graphics g = super.m_applet.getGraphics();
        fm = g.getFontMetrics(font);
        fontHeight = fm.getHeight();
        iStockRows = super.m_rc.height / (fontHeight + gap) - 1;
        if(iStockRows < 1)
            iStockRows = 25;
        iEnd = (iStart + iStockRows) - 1;
    }

    void Paint(Graphics g) {
        bCanMove = true;
        initGraph(g);
        calculateRowsOfPage();
        paintTitleItems(g);
        paintQuoteData(g);
        super.m_applet.EndPaint();
        paintHighlightBar();
    }

    void paintTitleItems(Graphics g) {
        int x = super.m_rc.x;
        int y = super.m_rc.y + fm.getAscent();
        g.setColor(HQApplet.rhColor.clMultiQuote_TitleBack);
        g.fillRect(super.m_rc.x, super.m_rc.y, super.m_rc.width, fontHeight);
        for(int i = 0; i < m_strItems.length; i++) {
            if(i > m_iStaticIndex && i < iDynamicIndex)
                continue;
            MultiQuoteItemInfo info = (MultiQuoteItemInfo)m_htItemInfo.get(m_strItems[i]);
            if(info == null)
                continue;
            String strName = info.name;
            if(m_bShowSortTag && strSortItem.equals(m_strItems[i])) {
                if(isDescend == 1) {
                    g.setColor(HQApplet.rhColor.clIncrease);
                    strName = strName + "\u2193";
                } else {
                    g.setColor(HQApplet.rhColor.clHighlight);
                    strName = strName + "\u2191";
                }
            } else {
                g.setColor(HQApplet.rhColor.clItem);
            }
            x += info.width;
            int strLen = fm.stringWidth(strName);
            g.drawString(strName, x - strLen, y);
            if(x > super.m_rc.x + super.m_rc.width)
                break;
        }

    }

    void paintQuoteData(Graphics g) {
        if(bShowUserStock && iUserStockCode.length == 0) {
            paintPromptMessage(g);
            return;
        }
        if(packetInfo == null || quoteData == null)
            return;
        g.setFont(font);
        if(!bShowUserStock && (packetInfo.iStart != iStart || packetInfo.isDescend != isDescend || packetInfo.sortBy != getSortByField(strSortItem) || packetInfo.currentStockType != currentStockType))
            return;
        if(bShowUserStock) {
            Arrays.sort(quoteData, strSortItem);
            if(isDescend == 0) {
                int size = quoteData.length;
                int count = size / 2;
                for(int i = 0; i < count; i++) {
                    ProductDataVO tmp = quoteData[i];
                    quoteData[i] = quoteData[size - i - 1];
                    quoteData[size - i - 1] = tmp;
                }

            }
        }
        int gap = 2;
        int x = super.m_rc.x;
        int y = super.m_rc.y + fontHeight + 2 + fm.getAscent();
        int count = iStockRows;
        if(quoteData.length < iStockRows)
            count = quoteData.length;
        for(int i = 0; i < count; i++) {
            int num = iStart + i;
            ProductDataVO data = quoteData[i];
            if(data.code == null) {
                System.out.println("Code = null");
            } else {
                CodeTable stockTable = (CodeTable)super.m_applet.m_htProduct.get(data.code);
                String stockName = "-";
                if(stockTable != null) {
                    if(stockTable.sName != null)
                        stockName = stockTable.sName;
                    else
                        System.out.println("stockTable.sName = null");
                } else {
                    System.out.println(" stockTable = null ");
                }
                int iPrecision = super.m_applet.GetPrecision(data.code);
                float close = data.yesterBalancePrice;
                int strLen = 0;
                for(int j = 0; j < m_strItems.length; j++) {
                    if(j > m_iStaticIndex && j < iDynamicIndex)
                        continue;
                    MultiQuoteItemInfo info = (MultiQuoteItemInfo)m_htItemInfo.get(m_strItems[j]);
                    if(info == null)
                        continue;
                    x += info.width;
                    if(m_strItems[j].equals("No")) {
                        String strText = String.valueOf(num);
                        g.setColor(HQApplet.rhColor.clNumber);
                        strLen = fm.stringWidth(strText);
                        g.drawString(strText, x - strLen, y);
                    } else
                    if(m_strItems[j].equals("Name")) {
                        String strText = stockName;
                        g.setColor(HQApplet.rhColor.clProductName);
                        strLen = fm.stringWidth(strText);
                        g.drawString(strText, x - strLen, y);
                    } else
                    if(m_strItems[j].equals("Code")) {
                        String strText = data.code;
                        g.setColor(HQApplet.rhColor.clProductName);
                        strLen = fm.stringWidth(strText);
                        g.drawString(strText, x - strLen, y);
                    } else
                    if(m_strItems[j].equals("CurPrice"))
                        paintNumber(g, data.curPrice, "", m_strItems[j], iPrecision, close, x, y);
                    else
                    if(m_strItems[j].equals("Balance"))
                        paintNumber(g, data.balancePrice, "", m_strItems[j], iPrecision, close, x, y);
                    else
                    if(m_strItems[j].equals("UpValue")) {
                        float up = close == 0.0F || data.curPrice == 0.0F ? 0.0F : data.curPrice - close;
                        paintNumber(g, up, "", m_strItems[j], 0, close, x, y);
                    } else
                    if(m_strItems[j].equals("UpRate")) {
                        float upRate = close <= 0.0F || data.curPrice <= 0.0F ? 0.0F : ((data.curPrice - close) / close) * 100F;
                        paintNumber(g, upRate, "", m_strItems[j], 2, 0.0F, x, y);
                    } else
                    if(m_strItems[j].equals("YesterBalance"))
                        paintNumber(g, data.yesterBalancePrice, "", m_strItems[j], iPrecision, close, x, y);
                    else
                    if(m_strItems[j].equals("OpenPrice"))
                        paintNumber(g, data.openPrice, "", m_strItems[j], iPrecision, close, x, y);
                    else
                    if(m_strItems[j].equals("BuyPrice"))
                        paintNumber(g, data.buyPrice[0], "", m_strItems[j], iPrecision, close, x, y);
                    else
                    if(m_strItems[j].equals("SellPrice"))
                        paintNumber(g, data.sellPrice[0], "", m_strItems[j], iPrecision, close, x, y);
                    else
                    if(m_strItems[j].equals("BuyAmount"))
                        paintNumber(g, data.buyAmount[0], "", m_strItems[j], 0, close, x, y);
                    else
                    if(m_strItems[j].equals("SellAmount"))
                        paintNumber(g, data.sellAmount[0], "", m_strItems[j], 0, close, x, y);
                    else
                    if(m_strItems[j].equals("HighPrice"))
                        paintNumber(g, data.highPrice, "", m_strItems[j], iPrecision, close, x, y);
                    else
                    if(m_strItems[j].equals("LowPrice"))
                        paintNumber(g, data.lowPrice, "", m_strItems[j], iPrecision, close, x, y);
                    else
                    if(m_strItems[j].equals("TotalAmount"))
                        paintNumber(g, data.totalAmount, "", m_strItems[j], 0, close, x, y);
                    else
                    if(m_strItems[j].equals("TotalMoney"))
                        paintNumber(g, data.totalMoney, "", m_strItems[j], iPrecision, close, x, y);
                    else
                    if(m_strItems[j].equals("ReverseCount"))
                        paintNumber(g, data.reserveCount, "", m_strItems[j], 0, close, x, y);
                    else
                    if(m_strItems[j].equals("CurAmount"))
                        paintNumber(g, data.curAmount, "", m_strItems[j], 0, close, x, y);
                    else
                    if(m_strItems[j].equals("AmountRate"))
                        paintNumber(g, data.amountRate, "", m_strItems[j], 2, close, x, y);
                    else
                    if(m_strItems[j].equals("ConsignRate"))
                        paintNumber(g, data.consignRate, "", m_strItems[j], 2, close, x, y);
                    else
                    if(m_strItems[j].equals("Unit") && super.m_applet.m_strMarketName.equalsIgnoreCase("Anhui")) {
                        String strText;
                        if(data.code.startsWith("G"))
                            strText = "\u5355";
                        else
                        if(data.code.startsWith("X"))
                            strText = "\u6279";
                        else
                            strText = "";
                        g.setColor(HQApplet.rhColor.clProductName);
                        strLen = fm.stringWidth(strText);
                        g.drawString(strText, x - strLen, y);
                    }
                    if(x > super.m_rc.x + super.m_rc.width)
                        break;
                }

                x = super.m_rc.x;
                y += fontHeight + gap;
            }
        }

    }

    private void paintPromptMessage(Graphics g) {
        String prompt = "\u8BF7\u7528\u6CE8\u518C\u8D26\u6237\u767B\u5F55\u540E\u5728\u9875\u9762\u4E2D\u8BBE\u5B9A\u81EA\u9009\u80A1";
        Font font = new Font("\u5B8B\u4F53", 0, 16);
        FontMetrics fm = g.getFontMetrics(font);
        int promptWidth = fm.stringWidth(prompt);
        int lines = promptWidth / (super.m_rc.width - 8);
        g.setFont(font);
        g.setColor(HQApplet.rhColor.clProductName);
        if(promptWidth % (super.m_rc.width - 8) > 0)
            lines++;
        int y = (super.m_rc.height - fm.getHeight() * lines - 20) / 2 + 20 + fm.getAscent();
        int x = 4;
        int beginIndex = 0;
        int lineChars = (super.m_rc.width - 8) / 16;
        while(beginIndex < prompt.length())  {
            int endIndex = beginIndex + lineChars;
            String strLine = "";
            if(endIndex > prompt.length()) {
                strLine = prompt.substring(beginIndex);
                beginIndex = prompt.length();
            } else {
                strLine = prompt.substring(beginIndex, endIndex);
                beginIndex = endIndex;
            }
            x = (super.m_rc.width - 8 - fm.stringWidth(strLine)) / 2 + 4;
            g.drawString(strLine, x, y);
            y += fm.getHeight();
        }
    }

    void paintNumber(Graphics g, double num, String strSuffix, String itemName, int iPrecision, float close, 
            int x, int y) {
        StringBuffer buffer = new StringBuffer();
        if(itemName.equals("TotalAmount") || itemName.equals("CurAmount") || itemName.equals("BuyAmount") || itemName.equals("SellAmount") || itemName.equals("AmountRate"))
            g.setColor(HQApplet.rhColor.clVolume);
        else
        if(itemName.equals("ReverseCount"))
            g.setColor(HQApplet.rhColor.clReserve);
        else
        if(itemName.equals("TotalMoney"))
            g.setColor(HQApplet.rhColor.clNumber);
        else
        if(itemName.equals("ConsignRate"))
            g.setColor(HQApplet.rhColor.clNumber);
        else
        if(itemName.equals("YesterBalance"))
            g.setColor(HQApplet.rhColor.clEqual);
        else
        if(itemName.equals("UpValue")) {
            if(num > 0.0D) {
                buffer.append("+");
                g.setColor(HQApplet.rhColor.clIncrease);
            } else
            if(num == 0.0D)
                g.setColor(HQApplet.rhColor.clEqual);
            else
                g.setColor(HQApplet.rhColor.clDecrease);
        } else
        if(num > (double)close)
            g.setColor(HQApplet.rhColor.clIncrease);
        else
        if(num == (double)close || num == 0.0D)
            g.setColor(HQApplet.rhColor.clEqual);
        else
            g.setColor(HQApplet.rhColor.clDecrease);
        if(itemName.equals("UpRate")) {
            if(num == -100D || num == 0.0D) {
                buffer.append("\u2014");
            } else {
                buffer.append(Common.FloatToString(num, iPrecision));
                buffer.append("%");
            }
        } else
        if(num == 0.0D)
            buffer.append("\u2014");
        else
            buffer.append(Common.FloatToString(num, iPrecision));
        buffer.append(strSuffix);
        int strLen = fm.stringWidth(buffer.toString());
        g.drawString(buffer.toString(), x - strLen, y);
    }

    void paintHighlightBar() {
        if(quoteData != null && iHighlightIndex > quoteData.length) {
            iHighlightIndex = quoteData.length;
            if(iHighlightIndex < 1)
                iHighlightIndex = 1;
        }
        Graphics g = super.m_applet.getGraphics();
        int gap = 2;
        int oldY = super.m_rc.y + iHighlightIndex * (fontHeight + gap);
        g.setColor(HQApplet.rhColor.clBackGround);
        g.setXORMode(HQApplet.rhColor.clHighlight);
        g.fillRect(super.m_rc.x, oldY, super.m_rc.width, fontHeight);
        g.setPaintMode();
    }

    void repaintHighlightBar(int iNewPos) {
        Graphics m_graphics = super.m_applet.getGraphics();
        int gap = 2;
        int oldY = super.m_rc.y + iHighlightIndex * (fontHeight + gap);
        int newY = super.m_rc.y + iNewPos * (fontHeight + gap);
        m_graphics.setColor(HQApplet.rhColor.clBackGround);
        m_graphics.setXORMode(HQApplet.rhColor.clHighlight);
        m_graphics.fillRect(super.m_rc.x, oldY, super.m_rc.width, fontHeight);
        m_graphics.fillRect(super.m_rc.x, newY, super.m_rc.width, fontHeight);
        iHighlightIndex = iNewPos;
        m_graphics.setPaintMode();
    }

    boolean KeyPressed(KeyEvent e) {
        int iKeyCode = e.getKeyCode();
        boolean bNeedRepaint = false;
        switch(iKeyCode) {
        case 38: // '&'
            bNeedRepaint = Key_UP_Pressed();
            break;

        case 40: // '('
            bNeedRepaint = Key_DOWN_Pressed();
            break;

        case 37: // '%'
            bNeedRepaint = Key_LEFT_Pressed();
            break;

        case 39: // '\''
            bNeedRepaint = Key_RIGHT_Pressed();
            break;

        case 34: // '"'
            bNeedRepaint = Key_PAGEDOWN_Pressed();
            break;

        case 33: // '!'
            bNeedRepaint = Key_PAGEUP_Pressed();
            break;

        case 10: // '\n'
            bNeedRepaint = Key_ENTER_Pressed();
            break;
        }
        return bNeedRepaint;
    }

    boolean MouseLeftClicked(int x, int y) {
        if(y > fontHeight) {
            selectProduct(x, y);
        } else {
            int iLeft = super.m_rc.x;
            for(int i = 0; i < m_strItems.length; i++) {
                if(i > m_iStaticIndex && i < iDynamicIndex)
                    continue;
                MultiQuoteItemInfo info = (MultiQuoteItemInfo)m_htItemInfo.get(m_strItems[i]);
                if(info == null)
                    continue;
                if(x > iLeft && x < iLeft + info.width) {
                    changeSortField(m_strItems[i]);
                    break;
                }
                iLeft += info.width;
                if(iLeft > super.m_rc.x + super.m_rc.width)
                    break;
            }

        }
        return false;
    }

    private void selectProduct(int x, int y) {
        if(packetInfo != null && quoteData != null) {
            int gap = 2;
            int xx = super.m_rc.x;
            int yy = super.m_rc.y + fontHeight + gap;
            int count = quoteData.length;
            if(count > iStockRows)
                count = iStockRows;
            for(int i = 0; i < count; i++) {
                if(x > xx && x < xx + super.m_rc.width && y > yy && y < yy + fontHeight + gap) {
                    if(i + 1 != iHighlightIndex)
                        repaintHighlightBar(i + 1);
                    break;
                }
                yy += fontHeight + gap;
            }

        }
    }

    boolean MouseLeftDblClicked(int x, int y) {
        if(packetInfo != null && quoteData != null) {
            int gap = 2;
            int xx = super.m_rc.x;
            int yy = super.m_rc.y + fontHeight;
            int count = iStockRows;
            if(iStockRows > quoteData.length)
                count = quoteData.length;
            for(int i = 0; i < count; i++) {
                if(x > xx && x < xx + super.m_rc.width && y > yy && y < yy + fontHeight) {
                    String sCode = quoteData[i].code;
                    super.m_applet.QueryStock(sCode);
                    return true;
                }
                yy += fontHeight + gap;
            }

        }
        return false;
    }

    boolean MouseMoved(int x, int y) {
        if(y <= 0 || y >= fontHeight) {
            super.m_applet.setCursor(new Cursor(0));
            return false;
        }
        int iLeft = super.m_rc.x;
        for(int i = 0; i < m_strItems.length; i++) {
            if(i > m_iStaticIndex && i < iDynamicIndex)
                continue;
            MultiQuoteItemInfo info = (MultiQuoteItemInfo)m_htItemInfo.get(m_strItems[i]);
            if(info == null)
                continue;
            if(x > iLeft && x < iLeft + info.width && info.sortID != -1) {
                super.m_applet.setCursor(new Cursor(12));
                return false;
            }
            iLeft += info.width;
            if(iLeft > super.m_rc.x + super.m_rc.width)
                break;
        }

        super.m_applet.setCursor(new Cursor(0));
        return false;
    }

    boolean Key_UP_Pressed() {
        if(!bCanMove)
            return false;
        if(currentStockType == 0) {
            iUserStockKey_Up();
            return false;
        }
        if(quoteData != null && packetInfo != null)
            if(iHighlightIndex > 1) {
                repaintHighlightBar(iHighlightIndex - 1);
            } else {
                int pageSize = iStockRows;
                if(quoteData.length < iStockRows)
                    pageSize = quoteData.length;
                if(iStart > 1) {
                    iEnd = iStart;
                    iStart = (iEnd - iStockRows) + 1;
                    if(iStart < 0)
                        iStart = 1;
                    iEnd = (iStart + iStockRows) - 1;
                    iHighlightIndex = iStockRows;
                    if(iEnd > packetInfo.iCount) {
                        iEnd = packetInfo.iCount;
                        iHighlightIndex = (iEnd - iStart) + 1;
                    }
                    AskForDataOnTimer();
                    bCanMove = false;
                }
            }
        return false;
    }

    private void iUserStockKey_Up() {
        if(quoteData == null)
            return;
        if(flag == 0) {
            backQuoteData = quoteData;
            flag = 1;
        }
        if(iHighlightIndex > 1) {
            repaintHighlightBar(iHighlightIndex - 1);
            HQApplet _tmp = super.m_applet;
            if(HQApplet.bDebug != 0)
                System.out.println("iHighlightIndex - 1 = " + (iHighlightIndex - 1));
            return;
        }
        int pageSize = iStockRows;
        if(backQuoteData.length < iStockRows)
            pageSize = backQuoteData.length;
        if(iStart > 1) {
            iEnd = iStart;
            if(pageSize == 1)
                iStart = iStart - pageSize;
            else
                iStart = (iStart - pageSize) + 1;
            if(iStart < 0)
                iStart = 1;
            iEnd = (iStart + iStockRows) - 1;
            iHighlightIndex = iStockRows;
            if(iEnd > backQuoteData.length) {
                iEnd = backQuoteData.length;
                iHighlightIndex = (iEnd - iStart) + 1;
            }
            int len = (iEnd - iStart) + 1;
            if(len > backQuoteData.length - iStart)
                len = backQuoteData.length - iStart;
            HQApplet _tmp1 = super.m_applet;
            if(HQApplet.bDebug != 0)
                System.out.println(" len = " + len);
            String StockCode[] = new String[len];
            int i = 0;
            for(int j = iStart - 1; i < len; j++) {
                StockCode[i] = backQuoteData[j].code;
                i++;
            }

            iUserStockCode = StockCode;
            AskForDataOnTimer();
            bCanMove = false;
        }
    }

    boolean Key_DOWN_Pressed() {
        if(!bCanMove)
            return false;
        if(currentStockType == 0) {
            iUserStockKey_DOWN();
            return false;
        }
        if(quoteData != null && packetInfo != null) {
            int lastStockNum = packetInfo.iCount;
            int firstStockNumPerPage = packetInfo.iStart;
            int pageSize = iStockRows;
            if(quoteData.length < iStockRows)
                pageSize = quoteData.length;
            if(iHighlightIndex < pageSize)
                repaintHighlightBar(iHighlightIndex + 1);
            else
            if((iStart + pageSize) - 1 < lastStockNum) {
                iStart += pageSize - 1;
                iEnd = (iStart + iStockRows) - 1;
                if(iEnd > lastStockNum)
                    iEnd = lastStockNum;
                iHighlightIndex = 1;
                AskForDataOnTimer();
                bCanMove = false;
            }
        }
        return false;
    }

    private void iUserStockKey_DOWN() {
        if(quoteData == null)
            return;
        if(flag == 0) {
            backQuoteData = quoteData;
            flag = 1;
        }
        int lastStockNum = backQuoteData.length;
        int pageSize = iStockRows;
        if(backQuoteData.length < iStockRows)
            pageSize = quoteData.length;
        if(iHighlightIndex < pageSize)
            repaintHighlightBar(iHighlightIndex + 1);
        else
        if((iStart + pageSize) - 1 < lastStockNum) {
            if(pageSize == 1) {
                iStart = iStart + pageSize;
                iEnd = iStart + iStockRows;
            } else {
                iStart = (iStart + pageSize) - 1;
                iEnd = (iStart + iStockRows) - 1;
            }
            if(iEnd > lastStockNum)
                iEnd = lastStockNum;
            iHighlightIndex = 1;
            int len = (iEnd - iStart) + 1;
            HQApplet _tmp = super.m_applet;
            if(HQApplet.bDebug != 0)
                System.out.println(" len = " + len);
            String StockCode[] = new String[len];
            int i = 0;
            for(int j = iStart - 1; i < len; j++) {
                StockCode[i] = backQuoteData[j].code;
                HQApplet _tmp1 = super.m_applet;
                if(HQApplet.bDebug != 0)
                    System.out.println(" StockCode[i] = " + StockCode[i]);
                i++;
            }

            iUserStockCode = StockCode;
            AskForDataOnTimer();
            bCanMove = false;
        }
    }

    boolean Key_PAGEUP_Pressed() {
        if(currentStockType == 0) {
            iUserStockPageUp();
            return false;
        }
        int pageSize = iStockRows;
        if(quoteData != null && packetInfo != null) {
            int lastStockNum = packetInfo.iCount;
            int firstStockNumPerPage = packetInfo.iStart;
            if(firstStockNumPerPage > 1) {
                iEnd = iStart;
                if(pageSize == 1)
                    iStart = iStart - pageSize;
                else
                    iStart = (iStart - pageSize) + 1;
                if(iStart < 0)
                    iStart = 1;
                iEnd = (iStart + iStockRows) - 1;
                AskForDataOnTimer();
            }
        }
        return false;
    }

    private void iUserStockPageUp() {
        if(quoteData == null)
            return;
        int pageSize = iStockRows;
        if(iStart > 1) {
            iEnd = iStart;
            if(pageSize == 1)
                iStart = iStart - pageSize;
            else
                iStart = (iStart - pageSize) + 1;
            HQApplet _tmp = super.m_applet;
            if(HQApplet.bDebug != 0) {
                System.out.println("this.iStart = " + iStart);
                System.out.println("this.iEnd = " + iEnd);
            }
            if(iStart < 0)
                iStart = 1;
            iEnd = (iStart + pageSize) - 1;
            int len = (iEnd - iStart) + 1;
            if(len > backQuoteData.length - iStart)
                len = backQuoteData.length - iStart;
            HQApplet _tmp1 = super.m_applet;
            if(HQApplet.bDebug != 0)
                System.out.println(" len = " + len);
            String StockCode[] = new String[len];
            int i = 0;
            for(int j = iStart - 1; i < len; j++) {
                StockCode[i] = backQuoteData[j].code;
                i++;
            }

            iUserStockCode = StockCode;
            AskForDataOnTimer();
        }
    }

    boolean Key_PAGEDOWN_Pressed() {
        if(currentStockType == 0) {
            iUserStockPageDown();
            return false;
        }
        if(quoteData != null && packetInfo != null) {
            int lastStockNum = packetInfo.iCount;
            int pageSize = iStockRows;
            if(iStockRows > quoteData.length)
                pageSize = quoteData.length;
            if((iStart + pageSize) - 1 < lastStockNum) {
                if(pageSize == 1) {
                    iStart = iStart + pageSize;
                    iEnd = iStart + iStockRows;
                } else {
                    iStart = (iStart + pageSize) - 1;
                    iEnd = (iStart + iStockRows) - 1;
                }
                if(iEnd > lastStockNum)
                    iEnd = lastStockNum;
                if(iHighlightIndex > (lastStockNum - iStart) + 1)
                    iHighlightIndex = 1;
                AskForDataOnTimer();
            } else {
                System.out.println(" NO PAGE");
            }
        } else {
            System.out.println(" No data ");
        }
        return false;
    }

    private void iUserStockPageDown() {
        if(quoteData == null)
            return;
        if(flag == 0) {
            backQuoteData = quoteData;
            flag = 1;
        }
        int lastStockNum = backQuoteData.length;
        int pageSize = iStockRows;
        System.out.println(" backQuoteData.length = " + backQuoteData.length);
        if(iStockRows > backQuoteData.length)
            pageSize = backQuoteData.length;
        System.out.println(" pageSize = " + pageSize);
        if((iStart + pageSize) - 1 < lastStockNum) {
            if(pageSize == 1) {
                iStart = iStart + pageSize;
                iEnd = iStart + iStockRows;
            } else {
                iStart = (iStart + pageSize) - 1;
                iEnd = (iStart + iStockRows) - 1;
            }
            if(iEnd > lastStockNum)
                iEnd = lastStockNum;
            if(iHighlightIndex > (lastStockNum - iStart) + 1)
                iHighlightIndex = 1;
            int len = (iEnd - iStart) + 1;
            System.out.println(" len = " + len);
            String StockCode[] = new String[len];
            int i = 0;
            for(int j = iStart - 1; i < len; j++) {
                StockCode[i] = backQuoteData[j].code;
                System.out.println(" StockCode[i] = " + StockCode[i]);
                i++;
            }

            iUserStockCode = StockCode;
            AskForDataOnTimer();
        }
    }

    boolean Key_LEFT_Pressed() {
        if(iDynamicIndex == m_iStaticIndex + 1) {
            return false;
        } else {
            iDynamicIndex--;
            return true;
        }
    }

    boolean Key_RIGHT_Pressed() {
        boolean bNeedRepaint = false;
        if(iDynamicIndex < m_strItems.length - 1) {
            iDynamicIndex++;
            bNeedRepaint = true;
        }
        return bNeedRepaint;
    }

    boolean Key_ENTER_Pressed() {
        if(quoteData != null && packetInfo != null && iHighlightIndex > 0 && iHighlightIndex <= quoteData.length) {
            String sCode = quoteData[iHighlightIndex - 1].code;
            super.m_applet.QueryStock(sCode);
            return true;
        } else {
            return false;
        }
    }

    byte getSortByField(String strSortItem) {
        MultiQuoteItemInfo info = (MultiQuoteItemInfo)m_htItemInfo.get(strSortItem);
        if(info == null)
            return 0;
        else
            return (byte)info.sortID;
    }

    void printQuoteData() {
        if(quoteData == null) {
            System.err.println("QuoteData is NULL!!!");
            return;
        }
        for(int i = 0; i < quoteData.length; i++) {
            ProductDataVO stock = quoteData[i];
            float uprate = ((stock.curPrice - stock.yesterBalancePrice) / stock.yesterBalancePrice) * 100F;
            System.err.println(i + "\t\u6DA8\u5E45" + "\t\u6700\u65B0" + "\t\u524D\u6536" + "\t\u5F00\u76D8" + "\t\u6700\u9AD8" + "\t\u6700\u4F4E" + "\t\u6210\u4EA4\u91CF" + "\t\u6210\u4EA4\u91D1\u989D" + "\t\u73B0\u624B" + "\t\u91CF\u6BD4" + "\t\u59D4\u6BD4");
            System.out.print("\t" + uprate + "%");
            System.out.print("\t" + stock.curPrice);
            System.out.print("\t" + stock.closePrice);
            System.out.print("\t" + stock.openPrice);
            System.out.print("\t" + stock.highPrice);
            System.out.print("\t" + stock.lowPrice);
            System.out.print("\t" + stock.totalAmount);
            System.out.print("\t" + stock.totalMoney);
            System.out.print("\t" + stock.curAmount);
            System.out.print("\t" + stock.amountRate);
            System.out.print("\t" + stock.consignRate);
        }

    }

    void printPacketInfo() {
        if(packetInfo == null) {
            System.err.println("PacketInfo is NULL!!!");
            return;
        } else {
            return;
        }
    }

    void processMenuEvent(PopupMenu popupMenu, int x, int y) {
        selectProduct(x, y);
        popupMenu.removeAll();
        popupMenu.add(menuStockRank);
        popupMenu.addSeparator();
        popupMenu.add(menuPageMinLine);
        popupMenu.add(menuPageKLine);
        popupMenu.addSeparator();
        popupMenu.add(menuMarket);
        processCommonMenuEvent(popupMenu, this);
        popupMenu.show(super.m_applet, x, y);
    }

    void makeMenus() {
        menuStockRank = new Menu(super.m_applet.getShowString("SortBy"));
        menuMarket = new MenuItem(super.m_applet.getShowString("ClassedList") + "  F4");
        menuPageMinLine = new MenuItem(super.m_applet.getShowString("MinLine") + "  F5");
        menuPageKLine = new MenuItem(super.m_applet.getShowString("Analysis"));
        menuMarket.setActionCommand("cmd_80");
        menuMarket.addActionListener(this);
        for(int i = 0; i < m_strItems.length; i++) {
            MultiQuoteItemInfo info = (MultiQuoteItemInfo)m_htItemInfo.get(m_strItems[i]);
            if(info != null && info.sortID != -1) {
                MenuItem menuItem = new MenuItem(info.name);
                menuItem.setActionCommand("Sort_" + m_strItems[i]);
                menuItem.addActionListener(this);
                menuStockRank.add(menuItem);
            }
        }

        menuPageMinLine.setActionCommand("minline");
        menuPageMinLine.addActionListener(this);
        menuPageKLine.setActionCommand("kline");
        menuPageKLine.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if(cmd.indexOf("cmd_") >= 0)
            executeCommand();
        else
        if(cmd.indexOf("Sort_") >= 0) {
            changeSortField(cmd.substring(5));
            if(HQApplet.bDebug != 0)
                System.out.println("cmd ====" + cmd);
        } else
        if(cmd.indexOf("STOCK_") >= 0)
            super.m_applet.ShowMutilQuote(getStockType(cmd));
        else
        if(cmd.equals("minline")) {
            if(iHighlightIndex > 0 && iHighlightIndex <= quoteData.length) {
                ProductDataVO stockData = quoteData[iHighlightIndex - 1];
                String scode = String.valueOf(stockData.code);
                super.m_applet.showPageMinLine(scode);
            }
        } else
        if(cmd.equals("kline")) {
            if(iHighlightIndex > 0 && iHighlightIndex <= quoteData.length) {
                ProductDataVO stockData = quoteData[iHighlightIndex - 1];
                String scode = String.valueOf(stockData.code);
                super.m_applet.showPageKLine(scode);
            }
        } else
        if(cmd.equals("userstock"))
            super.m_applet.ShowMutilQuote((byte)0);
        else
            super.actionPerformed(e);
    }

    private void changeSortField(String strSortItem) {
        MultiQuoteItemInfo info = (MultiQuoteItemInfo)m_htItemInfo.get(strSortItem);
        if(info == null)
            return;
        if(info.sortID == -1)
            return;
        m_bShowSortTag = true;
        if(this.strSortItem.equals(strSortItem)) {
            isDescend = ((byte)(isDescend != 1 ? 1 : 0));
        } else {
            isDescend = 0;
            sortBy = (byte)info.sortID;
            this.strSortItem = strSortItem;
        }
        quoteData = backQuoteData;
        AskForDataOnTimer();
    }

    private byte getStockType(String name) {
        return ((byte)(!name.equals("PRODUCT_COMMON") ? -1 : 0));
    }

    void setMenuEnable(byte stockType, boolean b) {
        switch(stockType) {
        case 0: // '\0'
        default:
            return;
        }
    }

    private void setUserStockCode() {
    }

    private void initUserStockArray() {
        int size = iUserStockCode.length;
        quoteData = new ProductDataVO[size];
        for(int i = 0; i < size; i++) {
            quoteData[i] = new ProductDataVO();
            quoteData[i].code = iUserStockCode[i];
        }

    }

    private void executeCommand() {
        switch(currentStockType) {
        case 1: // '\001'
            super.m_applet.UserCommand("80");
            break;
        }
    }

}
