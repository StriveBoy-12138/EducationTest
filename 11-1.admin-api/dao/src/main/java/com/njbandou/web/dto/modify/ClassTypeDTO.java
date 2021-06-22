package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.ClassTypeEntity;
import lombok.Data;

import java.sql.Timestamp;
@Data
public class ClassTypeDTO {
    private Integer pkId;
    private String name;

    private ClassTypeEntity set(ClassTypeEntity classType){
        classType.setName(name);
        classType.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return classType;
    }

    public ClassTypeEntity insert(){
        ClassTypeEntity classType = set(new ClassTypeEntity());
        classType.setDeleteFlag(0);
        classType.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return classType;
    }

    public ClassTypeEntity update(ClassTypeEntity classType){
        return set(classType);
    }
}
