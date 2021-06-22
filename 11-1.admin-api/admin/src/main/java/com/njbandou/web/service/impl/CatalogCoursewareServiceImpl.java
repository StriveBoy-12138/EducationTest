package com.njbandou.web.service.impl;

import com.njbandou.web.dto.modify.CatalogCoursewareDTO;
import com.njbandou.web.dto.search.SearchCatalogCoursewareDTO;
import com.njbandou.web.entity.CatalogCoursewareEntity;
import com.njbandou.web.exception.ServerException;
import com.njbandou.web.mapper.CatalogCoursewareMapper;
import com.njbandou.web.repository.CatalogCoursewareRepository;
import com.njbandou.web.service.CatalogCoursewareService;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.CatalogCoursewareResult;
import com.querydsl.core.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
public class CatalogCoursewareServiceImpl implements CatalogCoursewareService {
    @Autowired
    private CatalogCoursewareRepository  catalogCoursewareRepository;
    @Autowired
    private CatalogCoursewareMapper  catalogCoursewareMapper;

    @Override
    public Result getAll(SearchCatalogCoursewareDTO searchCatalogCoursewareDTO) {
        QueryResults<CatalogCoursewareResult> queryResultDTO = catalogCoursewareMapper.selectAll(searchCatalogCoursewareDTO);
        return ResultUtil.success(queryResultDTO.getResults());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(List<CatalogCoursewareDTO> catalogCoursewareList) {
        catalogCoursewareRepository.deleteByCatalogId(catalogCoursewareList.get(0).getCatalogId());
        catalogCoursewareList.forEach(catalogCoursewareDTO -> {
            CatalogCoursewareEntity catalogCourseware = catalogCoursewareDTO.insert();
            Optional<CatalogCoursewareEntity> catalogCoursewareEntity = catalogCoursewareRepository.findFirstByCatalogIdAndDeleteFlagOrderBySortDesc(catalogCoursewareDTO.getCatalogId(),0);
            if (catalogCoursewareEntity.isPresent()){
                catalogCourseware.setSort(catalogCoursewareEntity.get().getSort()+1);
            }else {
                catalogCourseware.setSort(1);
            }
            catalogCoursewareRepository.save(catalogCourseware);
        });

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result move(CatalogCoursewareDTO catalogCoursewareDTO) {
        CatalogCoursewareEntity catalogCourseware = catalogCoursewareRepository.findByCatalogIdAndSort(catalogCoursewareDTO.getCatalogId(),catalogCoursewareDTO.getSort())
                .orElseThrow(() -> new ServerException("对象不存在"));
        CatalogCoursewareEntity otherCatalogCourseware = catalogCoursewareDTO.getMove().equals("up") ? catalogCoursewareRepository.findFirstByCatalogIdAndSortBeforeAndDeleteFlagOrderBySortDesc(catalogCoursewareDTO.getCatalogId(),catalogCoursewareDTO.getSort(),0)
                .orElseThrow(() -> new ServerException("对象不存在")):
                catalogCoursewareRepository.findFirstByCatalogIdAndSortAfterAndDeleteFlagOrderBySortAsc(catalogCoursewareDTO.getCatalogId(),catalogCoursewareDTO.getSort(),0)
                        .orElseThrow(() -> new ServerException("对象不存在"));

        if (catalogCoursewareDTO.getMove().equals("up")){
            catalogCourseware.setSort(otherCatalogCourseware.getSort());
            otherCatalogCourseware.setSort(catalogCoursewareDTO.getSort());
        }else {
            catalogCourseware.setSort(otherCatalogCourseware.getSort());
            otherCatalogCourseware.setSort(catalogCoursewareDTO.getSort());
        }

        catalogCoursewareRepository.saveAndFlush(catalogCourseware);
        catalogCoursewareRepository.saveAndFlush(otherCatalogCourseware);

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(CatalogCoursewareDTO catalogCoursewareDTO) {
        catalogCoursewareRepository.deleteById(catalogCoursewareDTO.getPkId());

        return ResultUtil.successWithMessage("操作成功");
    }
}
