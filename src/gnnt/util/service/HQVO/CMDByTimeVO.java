// NMI's Java Code Viewer 6.0a
// www.trinnion.com/javacodeviewer

// Registered to Evaluation Copy                                      
// Generated PGFZKD AyTB 14 2007 15:45:22 

//source File Name:   CMDByTimeVO.java

package gnnt.util.service.HQVO;

import gnnt.MEBS.hq.ProductDataVO;
import java.io.DataInputStream;
import java.io.IOException;

// Referenced classes of package gnnt.util.service.HQVO:
//            CMDVO, CMDSortVO

public class CMDByTimeVO extends CMDVO {

    public int time;

    public CMDByTimeVO() {
        super.cmd = 9;
    }

    public static ProductDataVO[] getObj(DataInputStream input) throws IOException {
        return CMDSortVO.getObj(input);
    }
}
