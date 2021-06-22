package com.njbandou.web.service.impl;

import com.njbandou.web.dto.modify.OrganizationDTO;
import com.njbandou.web.entity.OrganizationEntity;
import com.njbandou.web.exception.ServerException;
import com.njbandou.web.mapper.OrganizationMapper;
import com.njbandou.web.repository.OrganizationRepository;
import com.njbandou.web.service.OrganizationService;
import com.njbandou.web.utils.TreeBuilder;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.InviteOrganizationResult;
import com.njbandou.web.vo.item.OrganizationResult;
import com.querydsl.core.QueryResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public Result getAll(Integer[] exceptOrganizationIds) {
        QueryResults<OrganizationResult> queryResult = organizationMapper.selectAll(exceptOrganizationIds);
        List<OrganizationResult> treeList = new ArrayList<>();
        queryResult.getResults().forEach(organization -> {
            OrganizationResult treeNode = new OrganizationResult(organization.getPkId(), organization.getParentId(), organization.getParentId().equals(0), organization.getTitle(),
                    new ArrayList<>());
            treeList.add(treeNode);
        });
        List<OrganizationResult> results = TreeBuilder.buildTreeByLoop(treeList);

        return ResultUtil.success(results);
    }

    @Override
    public Result getInviteOrganization(Integer[] organizationIds) {
        QueryResults<InviteOrganizationResult> queryResult = organizationMapper.selectByIds(organizationIds);
        return ResultUtil.success(queryResult.getResults());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(OrganizationDTO organizationDTO) {
        if (organizationRepository.findByParentIdAndName(organizationDTO.getParentId(),organizationDTO.getName()).isPresent()) {
            throw new ServerException("组织架构名不能重复!");
        }

        organizationRepository.save(organizationDTO.insert());

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(OrganizationDTO organizationDTO) {
        OrganizationEntity organization = organizationRepository.findById(organizationDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));
        Optional<OrganizationEntity> otherOrganization = organizationRepository.findByParentIdAndName(organization.getParentId(),organizationDTO.getName());
        if (otherOrganization.isPresent() && !organization.getPkId().equals(otherOrganization.get().getPkId())) {
            return ResultUtil.error("组织架构名不能重复！");
        }

        organizationRepository.saveAndFlush(organizationDTO.update(organization));

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(OrganizationDTO organizationDTO) {
        organizationRepository.deleteById(organizationDTO.getPkId());
        return ResultUtil.successWithMessage("操作成功");
    }
}
