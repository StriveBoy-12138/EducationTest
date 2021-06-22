package com.njbandou.web.utils;

import com.njbandou.web.vo.item.OrganizationResult;

import java.util.ArrayList;
import java.util.List;

public class TreeBuilder {

    /**
     * 两层循环实现建树
     *
     * @param OrganizationTreeNodes 传入的树节点列表
     * @return
     */
    public static List<OrganizationResult> buildTreeByLoop(List<OrganizationResult> OrganizationTreeNodes) {

        List<OrganizationResult> trees = new ArrayList<>();
        for (OrganizationResult OrganizationTreeNode : OrganizationTreeNodes) {
            if (OrganizationTreeNode.getParentId() == 0) {
                trees.add(OrganizationTreeNode);
            }
            for (OrganizationResult it : OrganizationTreeNodes) {
                if (it.getParentId().equals(OrganizationTreeNode.getPkId())) {
                    if (OrganizationTreeNode.getChildren() == null) {
                        OrganizationTreeNode.setChildren(new ArrayList<>());
                    }
                    OrganizationTreeNode.getChildren().add(it);
                }
            }
        }
        return trees;
    }

    /**
     * 使用递归方法建树
     *
     * @param organizationTreeNodes
     * @return
     */
    public static List<OrganizationResult> buildTreeByRecursive(List<OrganizationResult> organizationTreeNodes) {
        List<OrganizationResult> trees = new ArrayList<>();
        for (OrganizationResult organizationTreeNode : organizationTreeNodes) {
            // 去除顶层限制
            if (organizationTreeNode.getParentId() == 0) {
                trees.add(findChildren(organizationTreeNode, organizationTreeNodes));
            }
        }
        return trees;
    }

    /**
     * 递归查找子节点
     *
     * @param OrganizationTreeNodes
     * @return
     */
    public static OrganizationResult findChildren(OrganizationResult OrganizationTreeNode,
                                                    List<OrganizationResult> OrganizationTreeNodes) {
        for (OrganizationResult it : OrganizationTreeNodes) {
            if (OrganizationTreeNode.getPkId().equals(it.getParentId())) {
                if (OrganizationTreeNode.getChildren() == null) {
                    OrganizationTreeNode.setChildren(new ArrayList<>());
                }
                OrganizationTreeNode.getChildren().add(findChildren(it, OrganizationTreeNodes));
            }
        }
        return OrganizationTreeNode;
    }
}
