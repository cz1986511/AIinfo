package com.danlu.dleye.constants;

import com.danlu.dleye.manager.DleyeManager;
import com.danlu.dleye.manager.impl.InvokerManager;
import com.danlu.dleye.manager.impl.QuartzManager;
import com.danlu.dleye.manager.impl.SwitchManager;

public enum ManagerType {
    
    SWITCH(SwitchManager.class), INVOKER(InvokerManager.class), QUARTZ(QuartzManager.class);

    private Class<? extends DleyeManager> managerClass = null;

    public Class<? extends DleyeManager> getManagerClass() { return this.managerClass; }

    private ManagerType(Class<? extends DleyeManager> managerClass)
    {
      this.managerClass = managerClass;
    }

}
