// NMI's Java Code Viewer 6.0a
// www.trinnion.com/javacodeviewer

// Registered to Evaluation Copy                                      
// Generated PGFZKD AyTB 14 2007 15:44:19 

//source File Name:   ReceiveThread.java

package gnnt.MEBS.HQApplet;

import gnnt.MEBS.hq.*;
import gnnt.util.service.HQVO.*;
import java.awt.Component;
import java.io.*;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Vector;

// Referenced classes of package gnnt.MEBS.HQApplet:
//            HQApplet, SendThread, ProductData, Page_MultiQuote, 
//            Packet_MultiQuote, Page_MarketStatus, Packet_MarketStatus, Common, 
//            CodeTable

public class ReceiveThread extends Thread {

    HQApplet m_applet;

    public ReceiveThread(HQApplet applet) {
        m_applet = applet;
    }

    public void run() {
        DataInputStream reader = null;
        while(m_applet != null && m_applet.bRunning)  {
            if(m_applet.socket == null) {
                reader = null;
                try {
                    Thread.sleep(500L);
                }
                catch(InterruptedException interruptedexception) { }
                continue;
            }
            try {
                if(reader == null)
                    reader = new DataInputStream(new BufferedInputStream(m_applet.socket.getInputStream()));
                byte cmd = reader.readByte();
                switch(cmd) {
                case 0: // '\0'
                    break;

                case 7: // '\007'
                    HQApplet _tmp = m_applet;
                    if(HQApplet.bDebug != 0)
                        System.out.println("Receive cmd: " + cmd + " \u5E02\u573A\u65E5\u671F");
                    int date = reader.readInt();
                    int time = reader.readInt();
                    int oldDate = m_applet.m_iDate;
                    int oldTime = m_applet.m_iTime;
                    if(m_applet.m_iDate == 0 || date != oldDate) {
                        m_applet.m_iDate = date;
                        m_applet.vProductData.removeAllElements();
                    }
                    m_applet.m_iTime = time;
                    m_applet.repaintBottom();
                    HQApplet _tmp1 = m_applet;
                    if(HQApplet.bDebug != 0)
                        System.out.println("Date:" + m_applet.m_iDate + " " + time);
                    if(oldDate != m_applet.m_iDate || oldTime != m_applet.m_iTime)
                        m_applet.repaint();
                    break;

                case 6: // '\006'
                    HQApplet _tmp2 = m_applet;
                    if(HQApplet.bDebug != 0)
                        System.out.println("Receive cmd: " + cmd + " \u4EA4\u6613\u8282\u65F6\u95F4");
                    m_applet.m_timeRange = CMDTradeTimeVO.getObj(reader);
                    break;

                case 1: // '\001'
                    HQApplet _tmp3 = m_applet;
                    if(HQApplet.bDebug != 0)
                        System.out.println("Receive cmd: " + cmd + " \u66F4\u65B0\u7801\u8868");
                    ReceiveCodeTable(reader);
                    break;

                case 2: // '\002'
                    HQApplet _tmp4 = m_applet;
                    if(HQApplet.bDebug != 0)
                        System.out.println("Receive cmd: " + cmd + " \u4E2A\u80A1\u884C\u60C5");
                    ReceiveStockQuote(reader);
                    break;

                case 3: // '\003'
                    HQApplet _tmp5 = m_applet;
                    if(HQApplet.bDebug != 0)
                        System.out.println("Receive cmd: " + cmd + " \u62A5\u4EF7\u6392\u540D");
                    ReceiveClassSort(reader);
                    break;

                case 8: // '\b'
                    HQApplet _tmp6 = m_applet;
                    if(HQApplet.bDebug != 0)
                        System.out.println("Receive cmd: " + cmd + " \u7EFC\u5408\u6392\u540D");
                    ReceiveMarketSort(reader);
                    break;

                case 5: // '\005'
                    HQApplet _tmp7 = m_applet;
                    if(HQApplet.bDebug != 0)
                        System.out.println("Receive cmd: " + cmd + " \u6210\u4EA4\u660E\u7EC6");
                    ReceiveBillData(reader);
                    break;

                case 4: // '\004'
                    HQApplet _tmp8 = m_applet;
                    if(HQApplet.bDebug != 0)
                        System.out.println("Receive cmd: " + cmd + " \u5206\u65F6\u6570\u636E");
                    ReceiveMinLineData(reader);
                    break;

                case 10: // '\n'
                    m_applet.m_iMinLineInterval = reader.readInt();
                    HQApplet _tmp9 = m_applet;
                    if(HQApplet.bDebug != 0)
                        System.out.println("Receive cmd: " + cmd + " \u5206\u65F6\u95F4\u9694:" + m_applet.m_iMinLineInterval);
                    if(m_applet.m_iMinLineInterval <= 0 || m_applet.m_iMinLineInterval > 60)
                        m_applet.m_iMinLineInterval = 60;
                    break;

                case 9: // '\t'
                default:
                    HQApplet _tmp10 = m_applet;
                    if(HQApplet.bDebug != 0)
                        System.out.println("Receive cmd: " + cmd + " \u975E\u6CD5\u6570\u636E");
                    m_applet.socket.close();
                    m_applet.socket = null;
                    m_applet.sendThread.AskForData(null);
                    break;
                }
                continue;
            }
            catch(EOFException e) {
label0:
                {
                    if(m_applet != null) {
                        HQApplet _tmp11 = m_applet;
                        if(HQApplet.bDebug == 0)
                            break label0;
                    }
                    e.printStackTrace();
                }
                try {
                    if(m_applet.socket != null)
                        m_applet.socket.close();
                    m_applet.socket = null;
                }
                catch(Exception exception) { }
                if(m_applet != null && m_applet.bRunning)
                    m_applet.sendThread.AskForData(null);
                continue;
            }
            catch(Exception e) {
label1:
                {
                    if(m_applet != null) {
                        HQApplet _tmp12 = m_applet;
                        if(HQApplet.bDebug == 0)
                            break label1;
                    }
                    System.out.println("Socket error ");
                    e.printStackTrace();
                }
                if(m_applet != null && m_applet.bRunning) {
                    m_applet.socket = null;
                    m_applet.sendThread.AskForData(null);
                }
            }
        }
        System.out.println("ReceiveThread Exit !");
    }

    private void ReceiveStockQuote(DataInputStream reader) throws IOException {
        ProductDataVO data[] = CMDQuoteVO.getObj(reader);
        String sCode = "";
        for(int i = 0; i < data.length; i++) {
            sCode = data[i].code;
            ProductData stock = m_applet.GetProductData(sCode);
            if(stock == null) {
                if(m_applet.vProductData.size() > 50)
                    m_applet.vProductData.removeElementAt(50);
                stock = new ProductData();
                stock.sCode = sCode;
                stock.realData = data[i];
                m_applet.vProductData.insertElementAt(stock, 0);
            } else {
                stock.realData = data[i];
            }
        }

        if(data.length > 0 && (2 == m_applet.iCurrentPage || 1 == m_applet.iCurrentPage) && m_applet.strCurrentCode.equals(sCode))
            m_applet.repaint();
        if(data.length > 0 && m_applet.m_bShowIndexAtBottom == 1 && m_applet.indexMainCode.length() > 0 && data[0].code.equalsIgnoreCase(m_applet.indexMainCode))
            m_applet.repaintBottom();
    }

    private void ReceiveClassSort(DataInputStream reader) throws IOException {
        byte sortBy = reader.readByte();
        byte isDescend = reader.readByte();
        int totalCount = reader.readInt();
        int start = reader.readInt();
        ProductDataVO data[] = CMDSortVO.getObj(reader);
        if(m_applet.iCurrentPage != 0)
            return;
        Page_MultiQuote page = (Page_MultiQuote)m_applet.mainGraph;
        if(page.sortBy != sortBy || page.isDescend != isDescend || page.iStart != start)
            return;
        page.packetInfo = new Packet_MultiQuote();
        page.packetInfo.currentStockType = 1;
        page.packetInfo.sortBy = sortBy;
        page.packetInfo.isDescend = isDescend;
        page.packetInfo.iStart = start;
        page.packetInfo.iEnd = start + data.length;
        page.packetInfo.iCount = totalCount;
        HQApplet _tmp = m_applet;
        if(HQApplet.bDebug != 0)
            System.out.println("totalCount = " + totalCount);
        page.quoteData = data;
        m_applet.repaint();
    }

    private void ReceiveMarketSort(DataInputStream reader) throws IOException {
        int num = reader.readInt();
        gnnt.util.service.HQVO.MarketStatusVO values[] = CMDMarketSortVO.getObj(reader);
        if(5 != m_applet.iCurrentPage) {
            return;
        } else {
            Page_MarketStatus page = (Page_MarketStatus)m_applet.mainGraph;
            page.packetInfo = new Packet_MarketStatus();
            page.packetInfo.iCount = num;
            page.statusData = values;
            m_applet.repaint();
            return;
        }
    }

    private void ReceiveBillData(DataInputStream reader) throws IOException {
        String code = reader.readUTF();
        byte type = reader.readByte();
        int time = reader.readInt();
        BillDataVO data[] = CMDBillVO.getObj(reader);
        ProductData stock = m_applet.GetProductData(code);
        if(stock == null) {
            if(m_applet.vProductData.size() > 50)
                m_applet.vProductData.removeElementAt(50);
            stock = new ProductData();
            stock.sCode = code;
            m_applet.vProductData.insertElementAt(stock, 0);
        }
        switch(type) {
        default:
            break;

        case 0: // '\0'
            MakeLastBills(stock, time, data);
            break;

        case 1: // '\001'
            stock.vBill = new Vector();
            for(int i = 0; i < data.length; i++)
                stock.vBill.addElement(data[i]);

            if(data.length != 0 && 1 == m_applet.iCurrentPage && m_applet.strCurrentCode.equals(code))
                m_applet.repaint();
            break;
        }
    }

    private void ReceiveMinLineData(DataInputStream reader) throws IOException {
        String code = reader.readUTF();
        byte type = reader.readByte();
        int time = reader.readInt();
        MinDataVO values[] = CMDMinVO.getObj(reader);
        ProductData stock = m_applet.GetProductData(code);
        if(stock == null) {
            if(m_applet.vProductData.size() > 50)
                m_applet.vProductData.removeElementAt(50);
            stock = new ProductData();
            stock.sCode = code;
            m_applet.vProductData.insertElementAt(stock, 0);
        }
        stock.vMinLine = new Vector();
        int jMin = 0;
        for(int i = 0; i < values.length; i++) {
            int iIndex = Common.GetMinLineIndexFromTime(values[i].time, m_applet.m_timeRange, m_applet.m_iMinLineInterval);
            for(int j = jMin; j < iIndex; j++) {
                MinDataVO min = new MinDataVO();
                if(j > 0) {
                    min.curPrice = ((MinDataVO)stock.vMinLine.elementAt(j - 1)).curPrice;
                    min.totalAmount = ((MinDataVO)stock.vMinLine.elementAt(j - 1)).totalAmount;
                    min.totalMoney = ((MinDataVO)stock.vMinLine.elementAt(j - 1)).totalMoney;
                    min.averPrice = ((MinDataVO)stock.vMinLine.elementAt(j - 1)).averPrice;
                    min.reserveCount = ((MinDataVO)stock.vMinLine.elementAt(j - 1)).reserveCount;
                } else
                if(stock.realData != null) {
                    min.curPrice = stock.realData.yesterBalancePrice;
                    min.averPrice = stock.realData.yesterBalancePrice;
                }
                stock.vMinLine.addElement(min);
            }

            if(iIndex >= stock.vMinLine.size() - 1) {
                MinDataVO min = null;
                if(iIndex == stock.vMinLine.size() - 1) {
                    min = (MinDataVO)stock.vMinLine.lastElement();
                } else {
                    min = new MinDataVO();
                    stock.vMinLine.addElement(min);
                }
                min.curPrice = values[i].curPrice;
                min.totalAmount = values[i].totalAmount;
                min.reserveCount = values[i].reserveCount;
                min.averPrice = values[i].averPrice;
                jMin = iIndex + 1;
            }
        }

        if((2 == m_applet.iCurrentPage || 1 == m_applet.iCurrentPage) && m_applet.strCurrentCode.equals(stock.sCode))
            m_applet.repaint();
    }

    private void MakeLastBills(ProductData stock, int time, BillDataVO values[]) {
        if(values.length <= 0)
            return;
        if(time == 0) {
            stock.vBill = new Vector();
            for(int i = 0; i < values.length; i++)
                stock.vBill.addElement(values[i]);

            if(4 == m_applet.iCurrentPage)
                m_applet.repaint();
            return;
        }
        if(stock.vBill == null)
            stock.vBill = new Vector();
        if(stock.vBill.size() > 0 && ((BillDataVO)stock.vBill.lastElement()).time >= values[0].time)
            return;
        for(int i = 0; i < values.length; i++)
            stock.vBill.addElement(values[i]);

        if(stock.vMinLine == null)
            stock.vMinLine = new Vector();
        for(int i = 0; i < values.length; i++) {
            int iIndex = Common.GetMinLineIndexFromTime(values[i].time, m_applet.m_timeRange, m_applet.m_iMinLineInterval);
            if(iIndex < stock.vMinLine.size() - 1)
                break;
            MinDataVO min = null;
            if(iIndex == stock.vMinLine.size() - 1) {
                min = (MinDataVO)stock.vMinLine.elementAt(iIndex);
            } else {
                for(int j = stock.vMinLine.size(); j <= iIndex; j++) {
                    min = new MinDataVO();
                    if(j > 0) {
                        min.curPrice = ((MinDataVO)stock.vMinLine.elementAt(j - 1)).curPrice;
                        min.totalAmount = ((MinDataVO)stock.vMinLine.elementAt(j - 1)).totalAmount;
                        min.totalMoney = ((MinDataVO)stock.vMinLine.elementAt(j - 1)).totalMoney;
                        min.reserveCount = ((MinDataVO)stock.vMinLine.elementAt(j - 1)).reserveCount;
                        min.averPrice = ((MinDataVO)stock.vMinLine.elementAt(j - 1)).averPrice;
                    }
                    stock.vMinLine.addElement(min);
                }

            }
            min.curPrice = values[i].curPrice;
            min.totalAmount = values[i].totalAmount;
            min.totalMoney = values[i].totalMoney;
            min.reserveCount = values[i].reserveCount;
            min.averPrice = values[i].balancePrice;
        }

        if(values[values.length - 1].time > m_applet.m_iTime)
            m_applet.m_iTime = values[values.length - 1].time;
        if((2 == m_applet.iCurrentPage || 1 == m_applet.iCurrentPage) && m_applet.strCurrentCode.equals(stock.sCode))
            m_applet.repaint();
    }

    private void ReceiveCodeTable(DataInputStream reader) throws IOException {
        ProductInfoListVO list = CMDProductInfoVO.getObj(reader);
        for(int i = 0; i < list.productInfos.length; i++) {
            boolean bFound = false;
            for(int j = 0; j < m_applet.m_codeList.size(); j++) {
                if(!list.productInfos[i].code.equalsIgnoreCase((String)m_applet.m_codeList.elementAt(j)))
                    continue;
                bFound = true;
                break;
            }

            if(!bFound)
                m_applet.m_codeList.addElement(list.productInfos[i].code);
            CodeTable codeTable = new CodeTable();
            codeTable.sName = list.productInfos[i].name;
            codeTable.sPinyin = list.productInfos[i].pyName;
            codeTable.status = list.productInfos[i].status;
            codeTable.fUnit = list.productInfos[i].fUnit;
            codeTable.tradeSecNo = list.productInfos[i].tradeSecNo;
            m_applet.m_htProduct.put(list.productInfos[i].code, codeTable);
            m_applet.m_iCodeDate = list.date;
            m_applet.m_iCodeTime = list.time;
        }

    }
}
