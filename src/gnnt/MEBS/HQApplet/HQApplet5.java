// NMI's Java Code Viewer 6.0a
// www.trinnion.com/javacodeviewer

// Registered to Evaluation Copy                                      
// Generated PGFZKD AyTB 14 2007 15:44:19 

//source File Name:   HQApplet.java

package gnnt.MEBS.HQApplet;

import java.awt.event.*;

// Referenced classes of package gnnt.MEBS.HQApplet:
//            HQApplet

final class HQApplet5 extends MouseMotionAdapter {

    final HQApplet this$0; /* synthetic field */

    HQApplet5(HQApplet hqapplet) {
        this$0 = hqapplet;
    }

    public void mouseMoved(MouseEvent e) {
        this$0.this_mouseMoved(e);
    }

    public void mouseDragged(MouseEvent e) {
        if(e.getModifiers() != 4)
            this$0.this_mouseDragged(e);
    }
}
