package com.njbandou.web.enums;

public enum QuestionTypeEnum {
    SINGLE_CHOICE("单选题", 0), MULTIPLE_CHOICE("多选题", 1),
    FILL_BLANKS("填空题", 1), JUDGE("判断题", 1),
    BRIEF_ANSWER("简答题", 1), COMBINATION("组合题", 1);

    QuestionTypeEnum(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    private String name;
    private Integer value;

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }
}
