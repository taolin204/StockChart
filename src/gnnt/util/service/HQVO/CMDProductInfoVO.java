// NMI's Java Code Viewer 6.0a
// www.trinnion.com/javacodeviewer

// Registered to Evaluation Copy                                      
// Generated PGFZKD AyTB 14 2007 15:45:22 

//source File Name:   CMDProductInfoVO.java

package gnnt.util.service.HQVO;

import gnnt.MEBS.hq.ProductInfoVO;
import java.io.DataInputStream;
import java.io.IOException;

// Referenced classes of package gnnt.util.service.HQVO:
//            CMDVO, ProductInfoListVO

public class CMDProductInfoVO extends CMDVO {

    public int date;
    public int time;

    public CMDProductInfoVO() {
        super.cmd = 1;
    }

    public static ProductInfoListVO getObj(DataInputStream input) throws IOException {
        ProductInfoListVO productInfoList = new ProductInfoListVO();
        productInfoList.date = input.readInt();
        productInfoList.time = input.readInt();
        int length = input.readInt();
        ProductInfoVO productInfos[] = new ProductInfoVO[length];
        for(int i = 0; i < productInfos.length; i++) {
            productInfos[i] = new ProductInfoVO();
            productInfos[i].code = input.readUTF();
            productInfos[i].status = input.readInt();
            productInfos[i].fUnit = input.readFloat();
            productInfos[i].name = input.readUTF();
            productInfos[i].pyName = new String[input.readInt()];
            for(int j = 0; j < productInfos[i].pyName.length; j++)
                productInfos[i].pyName[j] = input.readUTF();

            productInfos[i].tradeSecNo = new int[input.readInt()];
            for(int j = 0; j < productInfos[i].tradeSecNo.length; j++)
                productInfos[i].tradeSecNo[j] = input.readInt();

            if(productInfos[i].fUnit <= 0.0F)
                productInfos[i].fUnit = 1.0F;
        }

        productInfoList.productInfos = productInfos;
        return productInfoList;
    }
}
