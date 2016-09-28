// NMI's Java Code Viewer 6.0a
// www.trinnion.com/javacodeviewer

// Registered to Evaluation Copy                                      
// Generated PGFZKD AyTB 14 2007 15:45:22 

//source File Name:   CMDTradeTimeVO.java

package gnnt.util.service.HQVO;

import gnnt.MEBS.hq.TradeTimeVO;
import java.io.DataInputStream;
import java.io.IOException;

// Referenced classes of package gnnt.util.service.HQVO:
//            CMDVO

public class CMDTradeTimeVO extends CMDVO {

    public CMDTradeTimeVO() {
        super.cmd = 6;
    }

    public static TradeTimeVO[] getObj(DataInputStream input) throws IOException {
        TradeTimeVO values[] = new TradeTimeVO[input.readInt()];
        for(int i = 0; i < values.length; i++) {
            values[i] = new TradeTimeVO();
            values[i].orderID = input.readInt();
            values[i].beginTime = input.readInt();
            values[i].endTime = input.readInt();
        }

        return values;
    }
}
