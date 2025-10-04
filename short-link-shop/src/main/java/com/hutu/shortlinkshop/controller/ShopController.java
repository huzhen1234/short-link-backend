package com.hutu.shortlinkshop.controller;

import com.hutu.shortlinkcommon.common.BaseResponse;
import com.hutu.shortlinkcommon.util.ResultUtils;
import com.hutu.shortlinkshop.domain.Product;
import com.hutu.shortlinkshop.service.impl.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ShopController {
    private final ProductServiceImpl productService;


    /**
     * 获取商品列表
     */
    @GetMapping("list")
    public BaseResponse<List<Product>> list(){
        return ResultUtils.success(productService.list());
    }

    /**
     * 根据id获取商品详情
     */
    @GetMapping("detail/{id}")
    public BaseResponse<Product> detail(@PathVariable("id") Long id){
        return ResultUtils.success(productService.getById(id));
    }
}
