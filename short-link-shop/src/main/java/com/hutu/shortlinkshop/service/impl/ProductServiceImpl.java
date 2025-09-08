package com.hutu.shortlinkshop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hutu.shortlinkshop.domain.Product;
import com.hutu.shortlinkshop.service.ProductService;
import com.hutu.shortlinkshop.mapper.ProductMapper;
import org.springframework.stereotype.Service;

/**
* @author huzhen
* @description 针对表【product(短链产品表)】的数据库操作Service实现
* @createDate 2025-09-08 23:32:45
*/
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product>
    implements ProductService{

}




