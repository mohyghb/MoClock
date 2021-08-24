package com.moh.moclock.MoGoogleSearch.Google.AnswerBox.DifferentTypes;

import java.util.ArrayList;

import com.moh.moclock.MoGoogleSearch.Google.AnswerBox.AnswerBox;

public class TranslatorAnswerBox extends AnswerBox {


    public static final String SYLLABLES_CODE = "<div class=\"xhf7k\"><span data-dobid=\"hdw\">";
    //public static final String PHONETIC_CODE = "<span class=\"lr_dct_ph XpoqFe\">/<span>";
    public static final String PHONETIC_CODE = "<span class=\"XpoqFe\">/<span>";
    public static final String TYPE_CODE = "<div class=\"lr_dct_sf_h oylZkd\"><i><span>";
    public static final String DEFINITIONS_CODE = "<div style=\"display:inline\" data-dobid=\"dfn\"><span>";
    public static final String EXAMPLES_CODE = "<span class=\"vmod\"><div class=\"vk_gy\">";
    public static final String ANT_SYN_CODE = "<table class=\"vk_tbl vk_gy\"><tr><td class=\"lr_dct_nyms_ttl\" style=\"padding-right:3px\">";

    public static final String SYN_CODE = "synonyms:";
    public static final String ANT_CODE = "antonyms:";
    public static final String ANT_SYN_WORDS_CODE = "<span class=\"SDZsVb\" data-term-for-update=\"";


    String syllables;
    String phonetic;

    ArrayList<String> type;
    ArrayList<String> definitions;
    ArrayList<String> examples;

    ArrayList<String> synonyms;
    ArrayList<String> antonyms;

    public TranslatorAnswerBox(String data) {
        super(data);
        this.initValues();
    }

    private void initValues()
    {
        this.syllables = extractData(SYLLABLES_CODE);
        this.phonetic = extractData(PHONETIC_CODE);

        this.type = extractDataList(TYPE_CODE);
        this.definitions = extractDataList(DEFINITIONS_CODE);
        this.examples = extractDataList(EXAMPLES_CODE);

        this.synonyms = new ArrayList<>();
        this.antonyms = new ArrayList<>();
        this.extractSynonymsAntonyms();
    }



    private void extractSynonymsAntonyms()
    {
        String[] split = getData().split(ANT_SYN_CODE);
        super.setDivisions(new String[]{"\""});

        // we do not care about index = 1;
        // since there is no data in there
        // so we start from i = 1;
        for(int i = 1;i<split.length;i++) {
            String data = split[i];
            super.setData(data);
            if (data.startsWith(SYN_CODE)) {
                // this data has synonyms
                this.synonyms.addAll(super.extractDataList(ANT_SYN_WORDS_CODE));
            }else if(data.startsWith(ANT_CODE)){
                // this data has antynoms
                this.antonyms.addAll(super.extractDataList(ANT_SYN_WORDS_CODE));
            }
        }

    }

    @Override
    public String toString() {
        return "TranslatorAnswerBox{"+ "\n" +
                "syllables='" + syllables + '\n' +
                ", phonetic='" + phonetic + '\n' +
                ", type=" + type + "\n" +
                ", definitions=" + definitions +  "\n"+
                ", examples=" + examples + "\n" +
                ", synonyms=" + synonyms + "\n" +
                ", antonyms=" + antonyms + "\n"+
                '}';
    }

    @Override
    public void getResult() {
        System.out.print(this.toString());
//        return String.format("Syllables: %s\nPhonetic:" +
//                " %s\nType: %s\nDefinitions:" +
//                " %s\nexamples: %s\nsynonyms: %s\nantynoms: %s\n",this.syllables,this.phonetic,this.type.toString(),this.definitions.toString(),this.examples.toString(),this.synonyms.toString(),this.antonyms.toString());
    }
}
