// NMI's Java Code Viewer 6.0a
// www.trinnion.com/javacodeviewer

// Registered to Evaluation Copy                                      
// Generated PGFZKD AyTB 14 2007 15:44:19 

//source File Name:   Draw_LastBill.java

package gnnt.MEBS.HQApplet;

import gnnt.MEBS.hq.BillDataVO;
import gnnt.MEBS.hq.ProductDataVO;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Vector;

// Referenced classes of package gnnt.MEBS.HQApplet:
//            HQApplet, RHColor, ProductData, Common

public class Draw_LastBill {

    public Draw_LastBill() {
    }

    static void Paint(Graphics g, Rectangle rc, ProductData product, HQApplet applet) {
        if(rc.height <= 0)
            return;
        g.setColor(HQApplet.rhColor.clGrid);
        g.drawRect(rc.x, rc.y, rc.width - 1, rc.height - 1);
        if(product == null || product.realData == null || product.vBill == null)
            return;
        int iProductType = applet.getProductType(product.sCode);
        int iPrecision = applet.GetPrecision(product.sCode);
        g.setFont(new Font("\u5B8B\u4F53", 0, 16));
        FontMetrics fm = g.getFontMetrics();
        int iShow = rc.height / fm.getHeight();
        int iSize = product.vBill.size();
        if(iSize <= 0)
            return;
        if(iSize < iShow)
            iShow = iSize;
        int y = rc.y + fm.getAscent();
        for(int i = iSize - iShow; i < iSize; i++) {
            if(i > product.vBill.size())
                break;
            BillDataVO billPre = null;
            if(i > 0)
                billPre = (BillDataVO)product.vBill.elementAt(i - 1);
            BillDataVO bill = (BillDataVO)product.vBill.elementAt(i);
            DecimalFormat df = new DecimalFormat("#,#0");
            String str = df.format(bill.time);
            if(str.length() != 8)
                str = "0" + str;
            str = str.replace(',', ':');
            g.setColor(HQApplet.rhColor.clNumber);
            int x = rc.x + 1;
            g.drawString(str, x, y);
            str = Common.FloatToString(bill.curPrice, iPrecision);
            if(bill.curPrice > product.realData.yesterBalancePrice)
                g.setColor(HQApplet.rhColor.clIncrease);
            else
            if(bill.curPrice < product.realData.yesterBalancePrice)
                g.setColor(HQApplet.rhColor.clDecrease);
            else
                g.setColor(HQApplet.rhColor.clEqual);
            x += fm.stringWidth("t") * 16 - fm.stringWidth(str);
            g.drawString(str, x, y);
            if(billPre == null)
                str = String.valueOf(bill.totalAmount);
            else
                str = String.valueOf((int)(bill.totalAmount - billPre.totalAmount));
            g.setColor(HQApplet.rhColor.clVolume);
            x = (rc.x + rc.width) - fm.charWidth('t') * 2 - fm.stringWidth(str);
            g.drawString(str, x, y);
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
                x = (rc.x + rc.width) - fm.charWidth('t') * 2;
                g.drawString(str, x, y);
            }
            y += fm.getHeight();
        }

    }
}
