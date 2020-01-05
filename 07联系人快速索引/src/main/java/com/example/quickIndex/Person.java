package com.example.quickIndex;

/**
 * Created by lenovo on 2020/1/4.
 */
public class Person {
    String name;
    String pinyin;

    public Person(String name) {
        this.name = name;
        this.pinyin = PinYinUtils.getPinYin(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}
