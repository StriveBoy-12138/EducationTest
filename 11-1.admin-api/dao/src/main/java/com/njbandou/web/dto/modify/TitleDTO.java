package com.njbandou.web.dto.modify;

import com.njbandou.web.entity.TitleEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Data
public class TitleDTO {
    private Integer pkId;

    @NotBlank(message = "标签名称不能为空")
    private String name;

    private Integer sort;

    private String move;

    private TitleEntity set(TitleEntity title){
        title.setName(name);
        title.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return title;
    }

    public TitleEntity insert(){
        TitleEntity title = set(new TitleEntity());
        title.setDeleteFlag(0);
        title.setCreateTime(new Timestamp(System.currentTimeMillis()));

        return title;
    }

    public TitleEntity update(TitleEntity title){
        return set(title);
    }
}
