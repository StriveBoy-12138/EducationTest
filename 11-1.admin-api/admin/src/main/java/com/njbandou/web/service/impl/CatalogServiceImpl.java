package com.njbandou.web.service.impl;

import com.njbandou.web.dto.modify.CatalogDTO;
import com.njbandou.web.dto.search.SearchCatalogDTO;
import com.njbandou.web.entity.CatalogEntity;
import com.njbandou.web.exception.ServerException;
import com.njbandou.web.mapper.CatalogMapper;
import com.njbandou.web.repository.CatalogRepository;
import com.njbandou.web.service.CatalogService;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.CatalogResult;
import com.querydsl.core.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
public class CatalogServiceImpl implements CatalogService {
    @Autowired
    private CatalogRepository catalogRepository;
    @Autowired
    private CatalogMapper catalogMapper;

    @Override
    public Result getAll(SearchCatalogDTO searchCatalogDTO) {
        QueryResults<CatalogResult> queryResultDTO = catalogMapper.selectAll(searchCatalogDTO);
        return ResultUtil.success(queryResultDTO.getResults());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(CatalogDTO catalogDTO) {
        if (catalogRepository.findByClassIdAndTitle(catalogDTO.getClassId(),catalogDTO.getTitle()).isPresent()) {
            throw new ServerException("该目录已存在");
        }

        CatalogEntity catalog = catalogDTO.insert();
        Optional<CatalogEntity> catalogEntity = catalogRepository.findFirstByClassIdAndDeleteFlagOrderBySortDesc(catalogDTO.getClassId(),0);
        if (catalogEntity.isPresent()){
            catalog.setSort(catalogEntity.get().getSort()+1);
        }else {
            catalog.setSort(1);
        }
        catalogRepository.save(catalog);

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(CatalogDTO catalogDTO) {
        CatalogEntity catalog = catalogRepository.findById(catalogDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));
        Optional<CatalogEntity> otherCatalog = catalogRepository.findByClassIdAndTitle(catalogDTO.getClassId(),catalogDTO.getTitle());
        if (otherCatalog.isPresent() && !otherCatalog.get().getPkId().equals(catalog.getPkId())){
            return ResultUtil.error("该目录已存在");
        }

        catalogRepository.saveAndFlush(catalogDTO.update(catalog));

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result move(CatalogDTO catalogDTO) {
        CatalogEntity catalog = catalogRepository.findByClassIdAndSort(catalogDTO.getClassId(),catalogDTO.getSort())
                .orElseThrow(() -> new ServerException("对象不存在"));
        CatalogEntity otherCatalog = catalogDTO.getMove().equals("up") ? catalogRepository.findFirstByClassIdAndSortBeforeAndDeleteFlagOrderBySortDesc(catalogDTO.getClassId(),catalogDTO.getSort(),0)
                .orElseThrow(() -> new ServerException("对象不存在")):
                catalogRepository.findFirstByClassIdAndSortAfterAndDeleteFlagOrderBySortAsc(catalogDTO.getClassId(),catalogDTO.getSort(),0)
                        .orElseThrow(() -> new ServerException("对象不存在"));

        if (catalogDTO.getMove().equals("up")){
            catalog.setSort(otherCatalog.getSort());
            otherCatalog.setSort(catalogDTO.getSort());
        }else {
            catalog.setSort(otherCatalog.getSort());
            otherCatalog.setSort(catalogDTO.getSort());
        }

        catalogRepository.saveAndFlush(catalog);
        catalogRepository.saveAndFlush(otherCatalog);

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(CatalogDTO catalogDTO) {
        catalogRepository.deleteById(catalogDTO.getPkId());
        return ResultUtil.successWithMessage("操作成功");
    }
}
