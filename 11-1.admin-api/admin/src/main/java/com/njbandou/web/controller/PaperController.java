package com.njbandou.web.controller;

import com.njbandou.web.dto.modify.PaperDTO;
import com.njbandou.web.dto.search.SearchPaperDTO;
import com.njbandou.web.exception.InvalidateParamException;
import com.njbandou.web.service.PaperService;
import com.njbandou.web.vo.Result;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/paper")
public class PaperController {
    @Autowired
    private PaperService paperService;

    @GetMapping(value = "/list")
    @RequiresPermissions("paper:list")
    public Result list(@RequestParam(required = false, defaultValue = "0") Integer placeFile,
                       @RequestParam(required = false, defaultValue = "0") Integer typeId,
                       @RequestParam(required = false, defaultValue = "-1") Integer status,
                       @RequestParam(required = false, defaultValue = "-1") Integer isOpen,
                       @RequestParam(required = false, defaultValue = "-1") Integer isFixed,
                       @RequestParam(required = false, defaultValue = "") String title,
                       @RequestParam(required = false, defaultValue = "10") Integer pageSize,
                       @RequestParam(required = false, defaultValue = "1") Integer page) {
        SearchPaperDTO searchPaperDTO = new SearchPaperDTO();
        searchPaperDTO.setPlaceFile(placeFile);
        searchPaperDTO.setTypeId(typeId);
        searchPaperDTO.setTitle(title);
        searchPaperDTO.setStatus(status);
        searchPaperDTO.setIsOpen(isOpen);
        searchPaperDTO.setIsFixed(isFixed);
        searchPaperDTO.setPage(page);
        searchPaperDTO.setPageSize(pageSize);
        return paperService.getPage(searchPaperDTO);
    }

    @PostMapping(value = "/add")
    @RequiresPermissions("paper:add")
    public Result add(@Valid @RequestBody PaperDTO paperDTO,
                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidateParamException(bindingResult);
        }
        return paperService.add(paperDTO);
    }

    @GetMapping(value = "/get")
    @RequiresPermissions("paper:list")
    public Result get(@RequestParam(required = false, defaultValue = "0") Integer paperId) {
        return paperService.getPaper(paperId);
    }

    @PostMapping(value = "/update")
    @RequiresPermissions("paper:update")
    public Result update(@Valid @RequestBody PaperDTO paperDTO,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidateParamException(bindingResult);
        }

        return paperService.update(paperDTO);
    }

    @PostMapping(value = "/set/status")
    @RequiresPermissions("paper:status")
    public Result setStatus(@RequestBody PaperDTO paperDTO) {
        return paperService.setStatus(paperDTO);
    }

    @PostMapping(value = "/set/user")
    @RequiresPermissions("paper:set")
    public Result setUser(@RequestBody PaperDTO paperDTO) {

        return paperService.setUser(paperDTO);
    }

    @PostMapping(value = "/set/questions")
    @RequiresPermissions("paper:set")
    public Result setQuestions(@RequestBody PaperDTO paperDTO) {

        return paperService.setQuestions(paperDTO);
    }

    @PostMapping(value = "/set/rules")
    @RequiresPermissions("paper:set")
    public Result setRules(@RequestBody PaperDTO paperDTO) {

        return paperService.setRules(paperDTO);
    }

    @PostMapping(value = "/placeFile")
    @RequiresPermissions("paper:place")
    public Result placeFilePaper(@RequestBody PaperDTO paperDTO) {

        return paperService.placeFilePaper(paperDTO);
    }

    @PostMapping(value = "/delete")
    @RequiresPermissions("paper:delete")
    public Result delete(@RequestBody Integer[] paperIds) {
        return paperService.delete(paperIds);
    }
}
