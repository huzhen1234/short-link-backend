package com.hutu.shortlinklink.service;

import com.hutu.shortlinklink.domain.pojo.GroupCodeMapping;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author hutu
* @description 针对表【group_code_mapping(短链表-商家维度)】的数据库操作Service
* @createDate 2025-07-30 11:48:07
*/
public interface GroupCodeMappingService extends IService<GroupCodeMapping> {

    GroupCodeMapping findByCodeAndGroupId(String shortLinkCode, Long accountNo);
}
