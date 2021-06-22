package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.ClassDTO;
import com.njbandou.web.dto.search.SearchClassDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.ClassService;
import com.njbandou.web.vo.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/class")
public class ClassController {
    @Autowired
    private ClassService classService;

    @GetMapping(value = "/list")
    @RequiresPermissions("class:list")
    public Result list(@RequestParam(required = false, defaultValue = "0") Integer typeId,
                       @RequestParam(required = false, defaultValue = "") String title,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                       @RequestParam(required = false, defaultValue = "1") Integer page) {
        SearchClassDTO searchClassDTO = new SearchClassDTO();
        searchClassDTO.setTypeId(typeId);
        searchClassDTO.setTitle(title);
        searchClassDTO.setPage(page);
        searchClassDTO.setPageSize(pageSize);
        return classService.getPage(searchClassDTO);
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("class:add")
    public Result add(@RequestBody ClassDTO classDTO) {
        return classService.add(classDTO);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("class:update")
    public Result update(@Valid @RequestBody ClassDTO classDTO,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidateParamException(bindingResult);
        }

        return classService.update(classDTO);
    }

    @PostMapping(value = "/set/user")
    @RequiresPermissions("class:set")
    public Result setUser(@RequestBody ClassDTO classDTO) {

        return classService.setUser(classDTO);
    }

    @PostMapping(value = "/set/paper")
    @RequiresPermissions("class:set")
    public Result setPaper(@RequestBody ClassDTO classDTO) {

        return classService.setPaper(classDTO);
    }

    @PostMapping(value = "/status")
    @RequiresPermissions("class:set")
    public Result setStatus(@RequestBody ClassDTO classDTO) {

        return classService.setStatus(classDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("class:delete")
    public Result delete(@RequestBody Integer[] classIds) {
        return classService.delete(classIds);
    }
}
