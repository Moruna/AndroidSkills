package com.moruna.xmlparsertest;

/**
 * Author: Moruna
 * Date: 2017-07-18
 * Copyright (c) 2017,dudu Co.,Ltd. All rights reserved.
 */
public class AppItem {
    public String pkg;
    public String name;

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getPkg() {
        return pkg;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return pkg + "-" + name;
    }
}
