package sem.datenhaltung.semmodel.crud.services;

import sem.datenhaltung.semmodel.crud.impl.ICRUDMailImpl;
import sem.datenhaltung.semmodel.crud.impl.IMailLocalServiceImpl;

public class ICRUDManagerSingleton {

    private static IMailLocalServiceImpl mailLocalServiceInstance;
    private static ICRUDMailImpl crudMailInstance;

    private ICRUDManagerSingleton(){}

    public static ICRUDMail getICRUDMail() {
        if(crudMailInstance == null){
            crudMailInstance = new ICRUDMailImpl();
        }
        return crudMailInstance;
    }


    public static IMailLocalService getIMailLocalService() {
        if(mailLocalServiceInstance == null){
            mailLocalServiceInstance = new IMailLocalServiceImpl();
        }
        return mailLocalServiceInstance;
    }
}
