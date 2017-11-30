package sem.systemfunktionen.assistent.impl;

import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Tag;
import sem.fachlogik.grenzklassen.EMailGrenz;
import sem.fachlogik.grenzklassen.TagGrenz;
import sem.systemfunktionen.assistent.core.Assistent;
import sem.systemfunktionen.assistent.core.Data;
import sem.systemfunktionen.assistent.services.IAssistentMailService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IAssistentMailServiceImpl implements IAssistentMailService {

    @Override
    public Tag analysiereEmail(EMailGrenz eMail) {
        return null;
    }

    @Override
    public void trainiereSEM(ArrayList<EMailGrenz> emails) {
        Assistent assistent = Assistent.getInstance();
        try {
            assistent.loadModel();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        List<Data> data = new ArrayList<>();

        for(EMailGrenz email : emails){
            Data d = new Data();
            d.content = email.getInhalt();
        }

        assistent.trainModel(data);

        for(int i = 0; i < emails.size(); i++){
            Data d = data.get(i);
            EMailGrenz e = emails.get(i);
            double max = d.topicDistribion[0];
            int position = 0;
            for(int j = 1; j < d.topicDistribion.length; j++){
                if(max < d.topicDistribion[j]){
                    max = d.topicDistribion[j];
                    position = j;
                }
            }
            TagGrenz tag = new TagGrenz();
            tag.setNumIndex(position);
            e.setTag(tag);
            try {
                assistent.saveModel();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
