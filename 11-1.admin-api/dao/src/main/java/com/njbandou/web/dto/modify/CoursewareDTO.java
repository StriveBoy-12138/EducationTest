package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.CoursewareEntity;
import io.swagger.annotations.*;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
@ApiModel(value = "课件表单条数据dto",description = "从客户端，由用户传来的数据封装在此dto中")
@Data
public class CoursewareDTO {
    @ApiModelProperty(name ="pk_id",value = "int-课件表主键")
    private Integer pkId;
    @ApiModelProperty(name ="typeId",value = "int-课程分类表主键")
    private Integer typeId;
    @NotBlank(message = "标题不能为空")
    @ApiModelProperty(name ="title",value = "课件名称")
    private String title;
    @NotNull(message = "学习时长不能为空")
    @ApiModelProperty(name ="studyTime",value = "学习时长")
    private Integer studyTime;
    @ApiModelProperty(name ="introduction",value = "简介")
    private String introduction;
    @ApiModelProperty(name ="coursewareType",value = "课件类型(0:图文 1:文档 2:图片 3:音频 4:视频 5:外部链接)")
    private Integer coursewareType;
    @ApiModelProperty(name ="size",value = "大小")
    private Double size;
    @NotBlank(message = "正文不能为空")
    @ApiModelProperty(name ="content",value = "正文内容")
    private String content;
    @ApiModelProperty(name ="authorId",value = "创建人id即用户表pk_id")
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
