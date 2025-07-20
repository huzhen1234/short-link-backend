package com.hutu.shortlinkaccount.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hutu.shortlinkaccount.domain.Traffic;
import com.hutu.shortlinkaccount.service.TrafficService;
import com.hutu.shortlinkaccount.mapper.TrafficMapper;
import org.springframework.stereotype.Service;

/**
* @author huzhen
* @description 针对表【traffic(流量包)】的数据库操作Service实现
* @createDate 2025-07-20 16:31:38
*/
@Service
public class TrafficServiceImpl extends ServiceImpl<TrafficMapper, Traffic>
    implements TrafficService{

}




