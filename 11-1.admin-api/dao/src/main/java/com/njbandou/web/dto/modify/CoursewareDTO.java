package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.CoursewareEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Data
public class CoursewareDTO {
    private Integer pkId;
    private Integer typeId;
    @NotBlank(message = "标题不能为空")
    private String title;
    @NotNull(message = "学习时长不能为空")
    private Integer studyTime;
    private String introduction;
    private Integer coursewareType;
    private Double size;
    @NotBlank(message = "正文不能为空")
    private String content;
    private Integer authorId;

    private CoursewareEntity set(CoursewareEntity courseware){
        courseware.setTypeId(typeId);
        courseware.setTitle(title);
        courseware.setStudyTime(studyTime);
        courseware.setIntroduction(introduction);
        courseware.setSize(size);
        courseware.setContent(content);
        courseware.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return courseware;
    }

    public CoursewareEntity insert(){
        CoursewareEntity courseware = set(new CoursewareEntity());
        courseware.setCoursewareType(coursewareType);
        courseware.setAuthorId(authorId);
        courseware.setDeleteFlag(0);
        courseware.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return courseware;
    }

    public CoursewareEntity update(CoursewareEntity courseware){
        return set(courseware);
    }
}
