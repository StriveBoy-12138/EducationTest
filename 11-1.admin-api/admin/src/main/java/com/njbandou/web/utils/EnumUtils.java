package com.njbandou.web.utils;

import com.njbandou.web.enums.QuestionTypeEnum;

public class EnumUtils {
    public static String getQuestionType(Integer value){
        String type = null;
        switch (value){
            case 0:type = QuestionTypeEnum.SINGLE_CHOICE.getName();break;
            case 1:type = QuestionTypeEnum.MULTIPLE_CHOICE.getName();break;
            case 2:type = QuestionTypeEnum.FILL_BLANKS.getName();break;
            case 3:type = QuestionTypeEnum.JUDGE.getName();break;
            case 4:type = QuestionTypeEnum.BRIEF_ANSWER.getName();break;
            case 5:type = QuestionTypeEnum.COMBINATION.getName();break;
        }
        return type;
    }
}
