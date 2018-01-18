package sem.fachlogik.assistentsteuerung.impl;

import sem.datenhaltung.semmodel.entities.EMail;
import sem.datenhaltung.semmodel.entities.Tag;
import sem.datenhaltung.semmodel.entities.TagVerteilung;
import sem.datenhaltung.semmodel.entities.Wort;
import sem.datenhaltung.semmodel.services.*;
import sem.fachlogik.assistentsteuerung.core.Assistent2;
import sem.fachlogik.assistentsteuerung.services.IAssistentSteuerung;
import sem.fachlogik.grenzklassen.EMailGrenz;
import sem.fachlogik.grenzklassen.GrenzklassenKonvertierer;
import sem.fachlogik.grenzklassen.TagGrenz;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class IAssistentSteuerungImpl implements IAssistentSteuerung{


    @Override
    public double[] getTagVerteilung(EMailGrenz email){
        ICRUDTagVerteilung icrudTagVerteilung = ICRUDManagerSingleton.getIcrudTagVerteilungInstance();
        ArrayList<TagVerteilung> verteilungen = icrudTagVerteilung.getTagVerteilungByEmailId(email.getMid());
        double[] verteilung = new double[verteilungen.size()];
        for(int i = 0; i < verteilung.length; i++){
            verteilung[i] = verteilungen.get(i).getVerteilung();
        }
        return verteilung;
    }

    @Override
    public int getAnzahlTags(){
        return Assistent2.getInstance().getNumTopics();
    }

    @Override
    public TagGrenz getTagById(int id){
        Tag tag = ICRUDManagerSingleton.getIcrudTagInstance().getTagById(id);
        return tag != null? GrenzklassenKonvertierer.tagZuTagGrenz(tag) : null;
    }

    @Override
    public void trainiereSEM(int numTopics, double alphasum, double beta, int numItarations) {
        Assistent2 assistent = Assistent2.getInstance();
        assistent.makeModel(numTopics,alphasum,beta);
        assistent.setNumIterations(numItarations);
        ICRUDTag icrudTag = ICRUDManagerSingleton.getIcrudTagInstance();
        icrudTag.deleteAlleTags();

        ICRUDMail crudmail = ICRUDManagerSingleton.getIcrudMailInstance();
        ArrayList<EMail> emails = crudmail.getAlleEMails();

        List<String> emailtrain = new ArrayList<>();
        Map<String, List<String>> data = new TreeMap<>();

        for(int i = 0; i < emails.size(); i++){
            EMail e = emails.get(i);
            emailtrain.add(e.getInhalt());
        }
        data.put("inhalt", emailtrain);
        assistent.train(data);

        ICRUDWort icrudWort = ICRUDManagerSingleton.getIcrudWordInstance();

        Object[][] topwords = assistent.getTopWords(50);
        for(int i = 0; i < numTopics; i++){
            Tag tag = new Tag();
            tag.setName((String)topwords[i][0]);
            int tid = icrudTag.createTag(tag);
            Object[] topicTopWords  = topwords[i];
            for(Object s : topicTopWords){
                Wort w = new Wort();
                w.setWort((String)s);
                w.setTid(tid);
                icrudWort.createWort(w);
            }
        }

        ICRUDTagVerteilung icrudTagVerteilung = ICRUDManagerSingleton.getIcrudTagVerteilungInstance();
        icrudTagVerteilung.deleteAlleTagVerteilungen();
        for(int i = 0; i < emails.size(); i++){
            double[] topicverteilung = assistent.getTopicDistribution(i);
            double max = 0;
            int j = 0;
            int k = 0;
            for(double d : topicverteilung){
                TagVerteilung tagVerteilung = new TagVerteilung(d, emails.get(i).getMid());
                icrudTagVerteilung.createTagVerteilung(tagVerteilung);
                if(d > max){
                    max = d;
                    k = j;
                }
                j++;
            }
            emails.get(i).setTid(k+1);
            crudmail.updateEMail(emails.get(i));
        }
        assistent.saveModel();
    }

    @Override
    public ArrayList<TagGrenz> zeigeAlleTagsAn(){
        ICRUDTag crudtag = ICRUDManagerSingleton.getIcrudTagInstance();
        ArrayList<Tag> tags = crudtag.getAlleTags();

        ArrayList<TagGrenz> tagGrenzs = new ArrayList<>();

        for(Tag t : tags){
           tagGrenzs.add(GrenzklassenKonvertierer.tagZuTagGrenz(t));
        }
        return tagGrenzs;
    }

    @Override
    public boolean updateTagGrenz(TagGrenz tagGrenz) {
        Tag tag = GrenzklassenKonvertierer.tagGrenzZuTag(tagGrenz);

        ICRUDWort crudwort = ICRUDManagerSingleton.getIcrudWordInstance();
        crudwort.deleteAlleWoerterMitTagId(tag.getTid());
        ArrayList<String> woerter = tagGrenz.getWoerter();
        for(String s : woerter){
            Wort w = new Wort();
            w.setTid(tag.getTid());
            w.setWort(s);
            crudwort.createWort(w);
        }
        ICRUDManagerSingleton.getIcrudTagInstance().updateTag(tag);
        return true;
    }

    @Override
    public void wortZurStoplisteHinzufuegen(String wort, int tid) {
        try(FileWriter fw = new FileWriter("src/resources/stoplist_sonstige.txt", true)) {
            fw.write(wort + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<Wort> woerter = ICRUDManagerSingleton.getIcrudWordInstance().getAlleWoerterMitTagId(tid);
        for(Wort w : woerter){
            if(w.getWort().equals(wort)){
                ICRUDManagerSingleton.getIcrudWordInstance().deleteWort(w.getWid());
            }
        }
    }

    @Override
    public void wortVonStoplisteEntfernen(String wort) {
        throw new UnsupportedOperationException();
    }



}
