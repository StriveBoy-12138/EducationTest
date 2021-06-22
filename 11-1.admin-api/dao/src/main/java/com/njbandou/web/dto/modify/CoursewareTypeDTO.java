package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.CoursewareTypeEntity;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class CoursewareTypeDTO {
    private Integer pkId;
    private String name;

    private CoursewareTypeEntity set(CoursewareTypeEntity coursewareType){
        coursewareType.setName(name);
        coursewareType.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return coursewareType;
    }

    public CoursewareTypeEntity insert(){
        CoursewareTypeEntity coursewareType = set(new CoursewareTypeEntity());
        coursewareType.setDeleteFlag(0);
        coursewareType.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return coursewareType;
    }

    public CoursewareTypeEntity update(CoursewareTypeEntity coursewareType){
        return set(coursewareType);
    }
}
