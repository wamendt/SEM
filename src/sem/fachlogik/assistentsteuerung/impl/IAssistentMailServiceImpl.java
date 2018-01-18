package sem.fachlogik.assistentsteuerung.impl;


import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Tag;
import sem.datenhaltung.semmodel.entities.TagVerteilung;
import sem.datenhaltung.semmodel.services.ICRUDMail;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;
import sem.datenhaltung.semmodel.services.ICRUDTag;
import sem.datenhaltung.semmodel.services.ICRUDTagVerteilung;
import sem.fachlogik.assistentsteuerung.core.Assistent2;
import sem.fachlogik.assistentsteuerung.services.IAssistentMailService;
import sem.fachlogik.grenzklassen.EMailGrenz;
import sem.fachlogik.grenzklassen.GrenzklassenKonvertierer;
import sem.fachlogik.grenzklassen.TagGrenz;

import java.util.*;

public class IAssistentMailServiceImpl implements IAssistentMailService{
    @Override
    public Tag analysiereEmail(EMail email) {
        Assistent2 assistent = Assistent2.getInstance();
        assistent.loadModel();

        double[] verteilung = assistent.evaluate(email.getInhalt());
        ICRUDTagVerteilung icrudTagVerteilung = ICRUDManagerSingleton.getIcrudTagVerteilungInstance();
        double max = 0;
        int j = 0;
        int k = 0;
        for(double d : verteilung){
            TagVerteilung tagVerteilung = new TagVerteilung(d, email.getMid());
            icrudTagVerteilung.createTagVerteilung(tagVerteilung);
            if(d > max){
                max = d;
                k = j;
            }
            j++;
        }
        ICRUDTag icrudTag = ICRUDManagerSingleton.getIcrudTagInstance();
        Tag tag = icrudTag.getTagById(k+1);
        email.setTid(tag.getTid());
        ICRUDMail icrudMail = ICRUDManagerSingleton.getIcrudMailInstance();
        icrudMail.updateEMail(email);
        assistent.saveModel();
        return tag;
    }
}
