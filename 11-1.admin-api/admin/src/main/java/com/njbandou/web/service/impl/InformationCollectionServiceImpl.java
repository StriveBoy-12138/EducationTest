package com.njbandou.web.service.impl;

import com.njbandou.web.dto.modify.InformationCollectionDTO;
import com.njbandou.web.dto.search.SearchInformationCollectionDTO;
import com.njbandou.web.entity.InformationCollectionEntity;
import com.njbandou.web.entity.TitleEntity;
import com.njbandou.web.exception.ServerException;
import com.njbandou.web.mapper.InformationCollectionMapper;
import com.njbandou.web.repository.InformationCollectionRepository;
import com.njbandou.web.service.InformationCollectionService;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.InformationCollectionResult;
import com.querydsl.core.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class InformationCollectionServiceImpl implements InformationCollectionService {
    @Autowired
    private InformationCollectionRepository informationCollectionRepository;
    @Autowired
    private InformationCollectionMapper informationCollectionMapper;

    @Override
    public Result getAll(SearchInformationCollectionDTO searchInformationCollectionDTO) {
        QueryResults<InformationCollectionResult> queryResult = informationCollectionMapper.selectAll(searchInformationCollectionDTO);
        return ResultUtil.success(queryResult.getResults());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(InformationCollectionDTO informationCollectionDTO) {
        if (informationCollectionRepository.findByRelationIdAndTitle(informationCollectionDTO.getRelationId(),informationCollectionDTO.getTitle()).isPresent()) {
            throw new ServerException("该信息采集项已存在");
        }

        InformationCollectionEntity informationCollection = informationCollectionDTO.insert();
        Optional<InformationCollectionEntity> informationCollectionEntity = informationCollectionRepository.findFirstByRelationIdAndDeleteFlagOrderBySortDesc(informationCollectionDTO.getRelationId(),0);
        if (informationCollectionEntity.isPresent()){
            informationCollection.setSort(informationCollectionEntity.get().getSort()+1);
        }else {
            informationCollection.setSort(1);
        }
        informationCollectionRepository.save(informationCollection);

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(InformationCollectionDTO informationCollectionDTO) {
        InformationCollectionEntity informationCollection = informationCollectionRepository.findById(informationCollectionDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));
        Optional<InformationCollectionEntity> otherInformationCollection = informationCollectionRepository.findByRelationIdAndTitle(informationCollectionDTO.getRelationId(),informationCollectionDTO.getTitle());
        if (otherInformationCollection.isPresent() && !otherInformationCollection.get().getPkId().equals(informationCollection.getPkId())){
            return ResultUtil.error("该标签已存在");
        }

        informationCollectionRepository.saveAndFlush(informationCollectionDTO.update(informationCollection));

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    public Result move(InformationCollectionDTO informationCollectionDTO) {
        InformationCollectionEntity informationCollection = informationCollectionRepository.findByRelationIdAndSort(informationCollectionDTO.getRelationId(),informationCollectionDTO.getSort())
                .orElseThrow(() -> new ServerException("对象不存在"));
        InformationCollectionEntity otherInformationCollection = informationCollectionDTO.getMove().equals("up") ? informationCollectionRepository.findFirstByRelationIdAndSortBeforeAndDeleteFlagOrderBySortDesc(informationCollectionDTO.getRelationId(),informationCollectionDTO.getSort(),0)
                .orElseThrow(() -> new ServerException("对象不存在")):
                informationCollectionRepository.findFirstByRelationIdAndSortAfterAndDeleteFlagOrderBySortAsc(informationCollectionDTO.getRelationId(),informationCollectionDTO.getSort(),0)
                        .orElseThrow(() -> new ServerException("对象不存在"));

        if (informationCollectionDTO.getMove().equals("up")){
            informationCollection.setSort(otherInformationCollection.getSort());
            otherInformationCollection.setSort(informationCollectionDTO.getSort());
        }else {
            informationCollection.setSort(otherInformationCollection.getSort());
            otherInformationCollection.setSort(informationCollectionDTO.getSort());
        }

        informationCollectionRepository.saveAndFlush(informationCollection);
        informationCollectionRepository.saveAndFlush(otherInformationCollection);

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(InformationCollectionDTO informationCollectionDTO) {
        informationCollectionRepository.deleteById(informationCollectionDTO.getPkId());
        return ResultUtil.successWithMessage("操作成功");
    }
}
