package com.njbandou.web.service.impl;

import com.njbandou.web.dto.modify.ExaminationDTO;
import com.njbandou.web.dto.modify.PaperDTO;
import com.njbandou.web.dto.search.SearchPaperDTO;
import com.njbandou.web.entity.ExaminationPaperEntity;
import com.njbandou.web.entity.PaperEntity;
import com.njbandou.web.entity.UserEntity;
import com.njbandou.web.exception.ServerException;
import com.njbandou.web.mapper.PaperMapper;
import com.njbandou.web.repository.ExaminationPaperRepository;
import com.njbandou.web.repository.ExaminationRepository;
import com.njbandou.web.repository.PaperRepository;
import com.njbandou.web.repository.UserRepository;
import com.njbandou.web.service.PaperService;
import com.njbandou.web.vo.PageResult;
import com.njbandou.web.vo.Result;
import com.njbandou.web.vo.ResultUtil;
import com.njbandou.web.vo.item.PaperDetailResult;
import com.njbandou.web.vo.item.PaperResult;
import com.querydsl.core.QueryResults;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PaperServiceImpl implements PaperService {
    @Autowired
    private PaperRepository paperRepository;
    @Autowired
    private PaperMapper paperMapper;
    @Autowired
    private ExaminationRepository examinationRepository;
    @Autowired
    private ExaminationPaperRepository examinationPaperRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Result getPage(SearchPaperDTO searchPaperDTO) {
        QueryResults<PaperResult> queryResultDTO = paperMapper.selectByPage(searchPaperDTO);
        queryResultDTO.getResults().forEach(paper -> {
            paper.setTestedTime(examinationPaperRepository.countDistinctByPaperIdAndStatus(paper.getPkId(),2));
            paper.setInExam(examinationPaperRepository.countDistinctByPaperIdAndStatus(paper.getPkId(),1));
        });
        return ResultUtil.success(new PageResult<>(queryResultDTO.getResults(), queryResultDTO.getTotal()));
    }

    @Override
    public Result getPaper(Integer paperId) {
        PaperEntity paper = paperRepository.findById(paperId)
                .orElseThrow(() -> new ServerException("对象不存在"));

        return ResultUtil.success(new PaperDetailResult(paper));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(PaperDTO paperDTO) {
        if (paperRepository.findByTitle(paperDTO.getTitle()).isPresent()) {
            throw new ServerException("该试卷已存在");
        }

        // 新增试卷
        PaperEntity paper = paperDTO.insert();
        paperRepository.save(paper);
        // 新增考试设置
        ExaminationDTO examination = new ExaminationDTO();
        examinationRepository.save(examination.insert(paper.getPkId()));

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result update(PaperDTO paperDTO) {
        PaperEntity paperEntity = paperRepository.findById(paperDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));
        Optional<PaperEntity> otherPaper = paperRepository.findByTitle(paperDTO.getTitle());

        if (otherPaper.isPresent() && !paperEntity.getPkId().equals(otherPaper.get().getPkId())) {
            return ResultUtil.error("该试卷已存在！");
        }

        paperRepository.saveAndFlush(paperDTO.update(paperEntity));

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result setStatus(PaperDTO paperDTO) {
        PaperEntity paper = paperRepository.findById(paperDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));

        paper.setStatus(paperDTO.getStatus());
        paper.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        paperRepository.saveAndFlush(paper);

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result setUser(PaperDTO paperDTO) {
        PaperEntity paper = paperRepository.findById(paperDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));
        if (paperDTO.getIsSet()){
            if (StringUtils.isNotBlank(paper.getInviteUser()) && StringUtils.isBlank(paperDTO.getInviteUser())){
                throw new ServerException("该试卷已邀请学员");
            }else if (StringUtils.isNotBlank(paper.getInviteTitle()) && StringUtils.isBlank(paperDTO.getInviteTitle())){
                throw new ServerException("该试卷已邀请标签");
            }else if (StringUtils.isNotBlank(paper.getInviteOrganization()) && StringUtils.isBlank(paperDTO.getInviteOrganization())){
                throw new ServerException("该试卷已邀请组织架构");
            }else if (paper.getInviteAll().equals(0) && paperDTO.getInviteAll().equals(0)){
                throw new ServerException("该试卷已邀请全部");
            }
        }

        PaperEntity newPaper = paperDTO.setUser(paper);
        paperRepository.saveAndFlush(newPaper);

//        // 清空考试试卷
//        examinationPaperRepository.deleteByPaperId(newPaper.getPkId());
        // 添加考试试卷
        List<UserEntity> userList = new ArrayList<>();
        if (StringUtils.isNotBlank(newPaper.getInviteUser())){
            String[] users = newPaper.getInviteUser().split(",");
            Integer [] userIds = stringToInt(users);
            userList = userRepository.findByPkIdIn(userIds);
        }else if (StringUtils.isNotBlank(newPaper.getInviteTitle())){
            String[] titles = newPaper.getInviteTitle().split(",");
            Integer [] titleIds = stringToInt(titles);
            userList = userRepository.findByTitleIn(titleIds);
        }else if (StringUtils.isNotBlank(newPaper.getInviteOrganization())){
            String[] organizations = newPaper.getInviteOrganization().split(",");
            Integer [] organizationIds = stringToInt(organizations);
            userList = userRepository.findByOrganizationIdIn(organizationIds);
        }else if (newPaper.getInviteAll().equals(0)){
            userList = userRepository.findAll();
        }

        List<ExaminationPaperEntity> examinationPaperList = new ArrayList<>();
        userList.forEach(user -> {
            ExaminationPaperEntity examinationPaper = new ExaminationPaperEntity();
            examinationPaper.setUserId(user.getPkId());
            examinationPaper.setUserInfo("");
            examinationPaper.setPaperId(newPaper.getPkId());
            examinationPaper.setQuestions(newPaper.getQuestions());
            examinationPaper.setUserScore(0.0);
            examinationPaper.setFinishTime(Timestamp.valueOf("1999-01-01 04:00:00"));
            examinationPaper.setUseTime(0);
            examinationPaper.setIsJudge(1);
            examinationPaper.setIsMark(1);
            examinationPaper.setStatus(0);
            examinationPaper.setDeleteFlag(0);
            examinationPaper.setCreateTime(new Timestamp(System.currentTimeMillis()));
            examinationPaper.setUpdateTime(new Timestamp(System.currentTimeMillis()));

            examinationPaperList.add(examinationPaper);
        });
        examinationPaperRepository.saveAll(examinationPaperList);

        return ResultUtil.successWithMessage("操作成功",newPaper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result setQuestions(PaperDTO paperDTO) {
        PaperEntity paper = paperRepository.findById(paperDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));

        paperRepository.saveAndFlush(paperDTO.setPaperQuestions(paper));

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result setRules(PaperDTO paperDTO) {
        PaperEntity paper = paperRepository.findById(paperDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));

        paperRepository.saveAndFlush(paperDTO.setRule(paper));

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result placeFilePaper(PaperDTO paperDTO) {
        PaperEntity paper = paperRepository.findById(paperDTO.getPkId())
                .orElseThrow(() -> new ServerException("对象不存在"));

        paperRepository.saveAndFlush(paperDTO.placeFilePaper(paper));

        return ResultUtil.successWithMessage("操作成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Integer[] ids) {
        for (Integer id : ids) {
           paperRepository.deleteById(id);
        }

        return ResultUtil.successWithMessage("操作成功");
    }

    private Integer[] stringToInt(String [] strings){
        Integer[] integers = new Integer[strings.length];
        for (int i = 0;i < strings.length;i ++){
            integers[i] = Integer.valueOf(strings[i]);
        }
        return integers;
    }
}
