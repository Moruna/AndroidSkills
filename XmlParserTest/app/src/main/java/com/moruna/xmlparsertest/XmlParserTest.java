package com.moruna.xmlparsertest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class XmlParserTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xml_parser_test);

        ParserManager.getListByDom(this);

        ParserManager.getListByPull(this);

        new ParserManager().getListBySax(this);
    }
}
