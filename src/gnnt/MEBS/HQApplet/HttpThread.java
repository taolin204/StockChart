// NMI's Java Code Viewer 6.0a
// www.trinnion.com/javacodeviewer

// Registered to Evaluation Copy                                      
// Generated PGFZKD AyTB 14 2007 15:44:19 

//source File Name:   HttpThread.java

package gnnt.MEBS.HQApplet;

import gnnt.MEBS.hq.ProductInfoVO;
import gnnt.util.service.HQVO.ProductInfoListVO;
import gnnt.util.service.HQVO.RequestUtil;
import java.awt.Component;
import java.io.*;
import java.net.*;
import java.util.Hashtable;
import java.util.Vector;
import java.util.zip.GZIPInputStream;

// Referenced classes of package gnnt.MEBS.HQApplet:
//            HQApplet, CodeTable, Packet_HttpRequest, KLineData, 
//            ProductData

public class HttpThread extends Thread {

    HQApplet m_applet;
    static final int TYPE_CODELIST = 0;
    static final int TYPE_OTHER = 1;
    private int iType;
    private Vector vPacket;

    public HttpThread(int type, HQApplet applet) {
        m_applet = applet;
        iType = type;
        if(iType == 1)
            vPacket = new Vector();
    }

    synchronized void AskForData(Packet_HttpRequest packet) {
        if(packet != null) {
            vPacket.addElement(packet);
            int iSize = vPacket.size();
            int iBuf = 1;
            if(iSize > 1) {
                for(int i = 0; i < iSize - 1; i++)
                    vPacket.removeElementAt(i);

            }
        }
        notify();
    }

    public void run() {
        if(iType == 0)
            GetCodeList();
        else
            GetHttpData();
    }

    private void GetCodeList() {
        for(boolean bSucceed = false; m_applet != null && m_applet.bRunning && !bSucceed;) {
            try {
                ProductInfoListVO list = RequestUtil.getProductInfoList(m_applet.strURLPath + "data/productinfo.dat");
                HQApplet _tmp = m_applet;
                if(HQApplet.bDebug != 0)
                    System.out.println("\u7801\u8868\u65F6\u95F4:" + list.date + " " + list.time);
                m_applet.m_iCodeDate = list.date;
                m_applet.m_iCodeTime = list.time;
                ProductInfoVO products[] = list.productInfos;
                m_applet.m_codeList.removeAllElements();
                m_applet.m_htProduct.clear();
                for(int i = 0; i < products.length; i++) {
                    m_applet.m_codeList.addElement(products[i].code);
                    CodeTable data = new CodeTable();
                    data.sName = products[i].name;
                    data.sPinyin = products[i].pyName;
                    data.status = products[i].status;
                    data.tradeSecNo = products[i].tradeSecNo;
                    data.fUnit = products[i].fUnit;
                    m_applet.m_htProduct.put(products[i].code, data);
                    if(data.status == 3 && m_applet.indexMainCode.length() == 0)
                        m_applet.indexMainCode = products[i].code;
                }

                bSucceed = true;
                m_applet.repaint();
            }
            catch(MalformedURLException malformedurlexception) { }
            catch(IOException ex) {
                HQApplet _tmp1 = m_applet;
                if(HQApplet.bDebug != 0)
                    ex.printStackTrace();
            }
            catch(Exception ex) {
                HQApplet _tmp2 = m_applet;
                if(HQApplet.bDebug != 0)
                    ex.printStackTrace();
            }
            if(!bSucceed)
                try {
                    Thread.sleep(1000L);
                }
                catch(InterruptedException interruptedexception) { }
        }

    }

    private void GetHttpData() {
        while(m_applet != null && m_applet.bRunning)  {
            try {
                Thread.sleep(300L);
            }
            catch(InterruptedException interruptedexception) { }
            int iSize = vPacket.size();
            if(iSize > 0) {
                Packet_HttpRequest request = (Packet_HttpRequest)vPacket.elementAt(iSize - 1);
                vPacket.removeElementAt(iSize - 1);
                switch(request.type) {
                case 0: // '\0'
                    GetDayLine(request);
                    break;

                case 1: // '\001'
                    Get5MinLine(request);
                    break;
                }
            } else {
                synchronized(this) {
                    try {
                        wait();
                    }
                    catch(InterruptedException interruptedexception1) { }
                }
            }
        }
    }

    private static byte[] getRepoent(String url) throws MalformedURLException, IOException {
        URL page = null;
        page = new URL(url);
        URLConnection urlc = page.openConnection();
        urlc.connect();
        int filesize = urlc.getContentLength();
        BufferedInputStream inputs = new BufferedInputStream(urlc.getInputStream());
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        for(byte by[] = new byte[1]; inputs.read(by) > 0; array.write(by));
        inputs.close();
        if(array.toByteArray().length != filesize)
            throw new MalformedURLException();
        else
            return array.toByteArray();
    }

    public static KLineData[] getHistoryData(String url) throws MalformedURLException, IOException {
        ByteArrayInputStream arrayInput = new ByteArrayInputStream(getRepoent(url));
        GZIPInputStream gzin = new GZIPInputStream(arrayInput);
        DataInputStream input = new DataInputStream(gzin);
        KLineData hisStatus[] = new KLineData[input.readInt()];
        for(int i = 0; i < hisStatus.length; i++) {
            hisStatus[i] = new KLineData();
            int date = input.readInt();
           // if(date.length() > 6)
            if(date>6)
                hisStatus[i].date = 0x2e7f0c2d00L + (long)date;
            else
                hisStatus[i].date = date + 0x130b7d0;
            hisStatus[i].openPrice = input.readFloat();
            hisStatus[i].highPrice = input.readFloat();
            hisStatus[i].lowPrice = input.readFloat();
            hisStatus[i].closePrice = input.readFloat();
            hisStatus[i].balancePrice = input.readFloat();
            hisStatus[i].totalAmount = input.readLong();
            hisStatus[i].totalMoney = input.readFloat();
            hisStatus[i].reserveCount = input.readInt();
        }

        return hisStatus;
    }

    private void GetDayLine(Packet_HttpRequest request) {
        try {
            String strURL = m_applet.strURLPath + "data/day/" + request.sCode.trim() + ".day.zip";
            HQApplet _tmp = m_applet;
            if(HQApplet.bDebug != 0)
                System.out.println("Get Day : " + strURL);
            KLineData hisStatus[] = getHistoryData(strURL);
            ProductData product = m_applet.GetProductData(request.sCode);
            if(product == null) {
                if(m_applet.vProductData.size() > 50)
                    m_applet.vProductData.removeElementAt(50);
                product = new ProductData();
                product.sCode = request.sCode;
                product.dayKLine = hisStatus;
                m_applet.vProductData.insertElementAt(product, 0);
            } else {
                product.dayKLine = hisStatus;
            }
            if(hisStatus.length > 0) {
                HQApplet _tmp1 = m_applet;
                if(2 == m_applet.iCurrentPage && m_applet.strCurrentCode.equals(request.sCode))
                    m_applet.repaint();
            }
        }
        catch(MalformedURLException ex) {
            HQApplet _tmp2 = m_applet;
            if(HQApplet.bDebug != 0)
                System.err.print(ex.toString());
        }
        catch(IOException ex) {
            HQApplet _tmp3 = m_applet;
            if(HQApplet.bDebug != 0)
                ex.printStackTrace();
        }
        catch(Exception ex) {
            HQApplet _tmp4 = m_applet;
            if(HQApplet.bDebug != 0)
                ex.printStackTrace();
        }
    }

    private void Get5MinLine(Packet_HttpRequest request) {
        try {
            KLineData hisStatus[] = getHistoryData(m_applet.strURLPath + "data/5min/" + request.sCode + ".5min.zip");
            ProductData stock = m_applet.GetProductData(request.sCode);
            if(stock == null) {
                if(m_applet.vProductData.size() > 50)
                    m_applet.vProductData.removeElementAt(50);
                stock = new ProductData();
                stock.sCode = request.sCode;
                stock.min5KLine = hisStatus;
                m_applet.vProductData.insertElementAt(stock, 0);
            } else {
                stock.min5KLine = hisStatus;
            }
            for(int iIndex = 0; iIndex < stock.min5KLine.length; iIndex++)
                if(stock.min5KLine[iIndex].balancePrice <= 0.0F)
                    if(stock.min5KLine[iIndex].totalAmount > 0L)
                        stock.min5KLine[iIndex].balancePrice = (float)(stock.min5KLine[iIndex].totalMoney / (double)stock.min5KLine[iIndex].totalAmount);
                    else
                    if(iIndex > 0)
                        stock.min5KLine[iIndex].balancePrice = stock.min5KLine[iIndex - 1].balancePrice;
                    else
                        stock.min5KLine[iIndex].balancePrice = stock.min5KLine[iIndex].closePrice;

            if(hisStatus.length > 0 && 2 == m_applet.iCurrentPage && m_applet.strCurrentCode.equals(request.sCode))
                m_applet.repaint();
        }
        catch(MalformedURLException ex) {
            HQApplet _tmp = m_applet;
            if(HQApplet.bDebug != 0)
                ex.printStackTrace();
        }
        catch(IOException ex) {
            HQApplet _tmp1 = m_applet;
            if(HQApplet.bDebug != 0)
                ex.printStackTrace();
        }
        catch(Exception ex) {
            HQApplet _tmp2 = m_applet;
            if(HQApplet.bDebug != 0)
                ex.printStackTrace();
        }
    }

    public static void quickSort(ProductInfoVO order[], int left, int right) {
        if(left < right) {
            ProductInfoVO tmp = order[left];
            int i = left;
            for(int j = right; i < j;) {
                while(i < j && order[j].code.compareTo(tmp.code) > 0) 
                    j--;
                if(i < j)
                    order[i++] = order[j];
                for(; i < j && order[i].code.compareTo(tmp.code) <= 0; i++);
                if(i < j)
                    order[j--] = order[i];
            }

            order[i] = tmp;
            quickSort(order, left, i - 1);
            quickSort(order, i + 1, right);
        }
    }
}
