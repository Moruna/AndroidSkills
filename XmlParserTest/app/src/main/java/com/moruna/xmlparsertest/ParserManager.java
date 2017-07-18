package com.moruna.xmlparsertest;

import android.content.Context;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Author: Moruna
 * Date: 2017-07-18
 * Copyright (c) 2017,dudu Co.,Ltd. All rights reserved.
 */
public class ParserManager {
    private static final String TAG = "ParserManager";
    public static final String ITEM = "item";
    public static final String PACKAGE = "package";
    public static final String NAME = "name";


    /**
     * DOM解析方式
     *
     * @param context
     * @return
     */
    public static List<AppItem> getListByDom(Context context) {
        List<AppItem> list = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            InputStream is = context.getAssets().open("apps.xml");
            Document document = factory.newDocumentBuilder().parse(is);
            // 获取根节点
            Element element = document.getDocumentElement();
            NodeList nodeList = element.getChildNodes();
            for (int i = 0; i < nodeList.getLength(); i++) {
                // 获取一个节点
                if (nodeList.item(i).getNodeName().equals(ITEM)) {
                    NodeList childList = nodeList.item(i).getChildNodes();
                    AppItem appItem = new AppItem();

                    for (int j = 0; j < childList.getLength(); j++) {
                        // 获取一个子节点
                        Node node = childList.item(j);
                        if (node.getNodeName().equals(PACKAGE)) {
                            appItem.setPkg(node.getFirstChild().getNodeValue());
                        } else if (node.getNodeName().equals(NAME)) {
                            appItem.setName(node.getFirstChild().getNodeValue());
                        }
                        list.add(appItem);
                    }
                }
            }
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * PULL解析方式（android推荐）
     *
     * @param context
     * @return
     */
    public static List<AppItem> getListByPull(Context context) {
        List<AppItem> list = null;
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
//            XmlPullParser parser = Xml.newPullParser();//或者直接
            parser.setInput(context.getAssets().open("apps.xml"), "UTF-8");
            int eventType = parser.getEventType();
            AppItem appItem = null;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        list = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals(ITEM)) {
                            appItem = new AppItem();
                        }
                        if (parser.getName().equals(NAME)) {
                            appItem.setName(parser.nextText());
                        }
                        if (parser.getName().equals(PACKAGE)) {
                            appItem.setPkg(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(ITEM)) {
                            list.add(appItem);
                            Log.e(TAG, "getListByPull:" + appItem.name + ":" + appItem.pkg);
                            appItem = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * SAX解析方式
     *
     * @param context
     * @return
     */
    public List<AppItem> getListBySax(Context context) {
        List<AppItem> list = null;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            InputStream is = context.getAssets().open("apps.xml");
            SaxHandler saxHandler = new SaxHandler();
            parser.parse(is, saxHandler);
            list = saxHandler.getList();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            e.printStackTrace();
        }
        return list;
    }

    private class SaxHandler extends DefaultHandler {
        private List<AppItem> list;
        private AppItem appItem;
        private String tag;

        @Override
        public void startDocument() throws SAXException {
            super.startDocument();
            list = new ArrayList<>();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if (qName.equals(ITEM)) {
                appItem = new AppItem();
            }
            tag = localName;
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            if (tag.equals(NAME)) {
                appItem.setName(new String(ch, start, length));
            } else if (tag.equals(PACKAGE)) {
                appItem.setPkg(new String(ch, start, length));
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if (qName.equals(ITEM)) {
                list.add(appItem);
                Log.e(TAG, "getListBySax:" + appItem.name + ":" + appItem.pkg);
            }
            tag = "";//一定要清空，不然已经赋值的appItem会被覆盖
        }

        private List<AppItem> getList() {
            return list;
        }
    }
}
