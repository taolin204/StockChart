// NMI's Java Code Viewer 6.0a
// www.trinnion.com/javacodeviewer

// Registered to Evaluation Copy                                      
// Generated PGFZKD AyTB 14 2007 15:44:19 

//source File Name:   KLineData.java

package gnnt.MEBS.HQApplet;


public class KLineData {

    public long date;
    public float openPrice;
    public float closePrice;
    public float highPrice;
    public float lowPrice;
    public float balancePrice;
    public long totalAmount;
    public double totalMoney;
    public int reserveCount;

    public KLineData() {
    }

    public String toString() {
        return "\r\ndate:" + date + "\r\nopenPrice:" + openPrice + "\r\nhighPrice:" + highPrice + "\r\nlowPrice:" + lowPrice + "\r\nclosePrice:" + closePrice + "\r\ntotalAmount:" + totalAmount + "\r\ntotalMoney:" + totalMoney;
    }
}
