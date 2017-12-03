package sem.fachlogik.assistentsteuerung.impl;

import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Tag;
import sem.datenhaltung.semmodel.entities.Wort;
import sem.datenhaltung.semmodel.services.ICRUDMail;
import sem.datenhaltung.semmodel.services.ICRUDManagerSingleton;
import sem.datenhaltung.semmodel.services.ICRUDTag;
import sem.datenhaltung.semmodel.services.ICRUDWort;
import sem.fachlogik.assistentsteuerung.core.Assistent2;
import sem.fachlogik.assistentsteuerung.services.IAssistentSteuerung;
import sem.fachlogik.grenzklassen.TagGrenz;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class IAssistentSteuerungImpl implements IAssistentSteuerung{

    private void loescheAlleTagsAusDB() throws IOException, SQLException {
        ICRUDTag crudTag = ICRUDManagerSingleton.getIcrudTagInstance();
        ICRUDWort crudWort = ICRUDManagerSingleton.getIcrudWordInstance();
        ArrayList<Tag> tags = crudTag.getAlleTags();

        for(Tag tag : tags){
            crudWort.deleteAlleWoerterMitTagId(tag.getTid());
            crudTag.deleteTag(tag.getTid());
        }

    }

    private ArrayList<TagGrenz> holeAlleTagsAusDB() throws IOException, SQLException {
        ICRUDTag crudTag = ICRUDManagerSingleton.getIcrudTagInstance();
        ICRUDWort crudWort = ICRUDManagerSingleton.getIcrudWordInstance();

        ArrayList<TagGrenz> tagGrenzs = new ArrayList<>();
        ArrayList<Tag> tags = crudTag.getAlleTags();

        for(Tag tag : tags){
            TagGrenz tagGrenz = new TagGrenz();
            tagGrenz.setName(tag.getName());
            tagGrenz.setTid(tag.getTid());
            ArrayList<String> woerterStrings = new ArrayList<>();
            ArrayList<Wort> woerter = crudWort.getAlleWoerterMitTagId(tag.getTid());
            for(Wort w : woerter){
                woerterStrings.add(w.getWort());
            }
            tagGrenz.setWoerter(woerterStrings);

            tagGrenzs.add(tagGrenz);
        }
        return  tagGrenzs;
    }
    @Override
    public void trainiereSEM(int numTopics, double alphasum, double beta) throws IOException, SQLException {
        Assistent2 assistent = Assistent2.getInstance();
        assistent.makeModel(numTopics,alphasum,beta);
        assistent.setNumIterations(50);

        ICRUDTag icrudTag = ICRUDManagerSingleton.getIcrudTagInstance();
        icrudTag.deleteAlleTags();

        ICRUDMail crudmail = ICRUDManagerSingleton.getIcrudMailInstance();
        ArrayList<EMail> emails = crudmail.getAlleEMails();

        List<String> emailtrain = new ArrayList<>();
        Map<String, List<String>> data = new TreeMap<>();

        for(EMail e : emails){
            emailtrain.add(e.getInhalt());
        }
        data.put("inhalt", emailtrain);
        assistent.train(data);

        ArrayList<Tag> tags = new ArrayList<>();

        Object[][] topwords = assistent.getTopWords(10);
        for(int i = 0; i < numTopics; i++){
            Tag tag = new Tag();
            int tid = icrudTag.createTag(tag);
            tag.setTid(tid);

            tags.add(tag);
        }
        for(int i = 0; i < emails.size(); i++){
            double[] topicverteilung = assistent.getTopicDistribution(i);
            double max = 0;
            int j = 0;
            int k = 0;
            for(double d : topicverteilung){
                if(d > max){
                    max = d;
                    k = j;
                }
                j++;
            }
            emails.get(i).setTid(k+1);
            crudmail.updateEMail(emails.get(i));
        }

    }

    @Override
    public void trainiereVorhandenSEM() throws IOException, SQLException {
        Assistent2 assistent = Assistent2.getInstance();
        assistent.loadModel();

        ICRUDMail crudmail = ICRUDManagerSingleton.getIcrudMailInstance();
        ArrayList<EMail> emails = crudmail.getAlleEMails();
        List<String> emailtrain = new ArrayList<>();
        List<String> emailtest = new ArrayList<>();
        Map<String, List<String>> data = new TreeMap<>();

        int i = 0;
        for(EMail e : emails){
            if(i%10 == 0){
                emailtest.add(e.getInhalt());
            }else{
                emailtrain.add(e.getInhalt());
            }
        }
        data.put("inhalt", emailtrain);
        assistent.evaluate(data);
        assistent.saveModel();
    }


}
