package com.tonpower.crm.settings.domain;

/**
 * @description:
 * @author: li377650260
 * @date: 2021/6/7 22:02
 */
public class DicType {
    private String code;
    private String name;
    private String description;

    public DicType() {
    }

    @Override
    public String toString() {
        return "DicTypeDao{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
