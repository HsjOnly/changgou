package com.itheima.changgou.goods.controller;

import com.github.pagehelper.PageInfo;
import com.itheima.changgou.costant.MessageConstants;
import com.itheima.changgou.costant.StatusCode;
import com.itheima.changgou.entity.Result;
import com.itheima.changgou.goods.pojo.Brand;
import com.itheima.changgou.goods.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/brand")
public class BrandContronller {

    @Autowired
    private BrandService brandService;

    /*
     * 查询所有的品牌
     * 包名: com.itheima.changgou.goods.controller
     * 作者: Narcissu
     * 日期：2019/12/28 21:26
     */
    @GetMapping
    public Result<List<Brand>> listAllBrand(HttpServletRequest httpServletRequest) {
        List<Brand> brands = brandService.listAllBrand();
        Result<List<Brand>> result = new Result<>(true, StatusCode.OK, "OK", brands);
        return result;
    }

    /*
     * 根据ID查询品牌
     * 包名: com.itheima.changgou.goods.controller
     * 作者: Narcissu
     * 日期：2019/12/28 21:26
     */
    @GetMapping("/{id}")
    public Result<Brand> getBrandById(@PathVariable("id") Integer id) {
        Brand brand = brandService.getBrandById(id);
        return new Result<>(true,StatusCode.OK, "OK", brand);
    }

    /*
     * 新增品牌
     * 包名: com.itheima.changgou.goods.controller
     * 作者: Narcissu
     * 日期：2019/12/28 21:26
     */
    @PostMapping
    public Result insertBrand(@RequestBody Brand brand) {
        brandService.insertBrand(brand);
        return new Result<>(true,StatusCode.OK,"OK");
    }

    /*
     * 更新品牌
     * 包名: com.itheima.changgou.goods.controller
     * 作者: Narcissu
     * 日期：2019/12/28 21:38
     */
    @PutMapping
    public Result updateBrand(@RequestBody Brand brand) {
        brandService.updateBrand(brand);
        return new Result<>(true, StatusCode.OK, "OK");
    }

    /*
     * 删除品牌
     * 包名: com.itheima.changgou.goods.controller
     * 作者: Narcissu
     * 日期：2019/12/28 21:42
     */
    @DeleteMapping
    public Result deleteBrand(@RequestBody Brand brand) {
        brandService.deleteBrand(brand);
        return new Result<>(true, StatusCode.OK, "OK");
    }

    /*
     * 根据条件查询品牌
     * 包名: com.itheima.changgou.goods.controller
     * 作者: Narcissu
     * 日期：2019/12/28 21:47
     */
    @PostMapping("/listBrandByCondition")
    public Result<List<Brand>> listBrandByCondition(@RequestBody Brand brand) {
        List<Brand> brands = brandService.listBrandByCondition(brand);
        return new Result<>(true, StatusCode.OK, MessageConstants.LIST_BRAND_BY_CONDITION_SUCCESS, brands);
    }

    /*
     * 分页查询品牌
     * 包名: com.itheima.changgou.goods.controller
     * 作者: Narcissu
     * 日期：2019/12/29 15:29
     */
    @GetMapping("/listBrandByPage/{currentPage}/{pageSize}")
    public Result<PageInfo<Brand>> listBrandByPage(@PathVariable Integer currentPage,@PathVariable Integer pageSize) {
        PageInfo<Brand> pageInfo = brandService.listBrandByPage(currentPage, pageSize);
        return new Result<>(true, StatusCode.OK, MessageConstants.LIST_BRAND_BY_PAGE_SUCCESS,pageInfo);
    }

    /*
     * 按条件分页查询品牌
     * 包名: com.itheima.changgou.goods.controller
     * 作者: Narcissu
     * 日期：2019/12/29 15:53
     */
    @PostMapping("/listBrandByConditionAndPage")
    public Result<PageInfo<Brand>> listBrandByConditionAndPage(@RequestBody Brand brand,
                                                                Integer currentPage,
                                                                Integer pageSize) {
        PageInfo<Brand> pageInfo = brandService.listBrandByConditionAndPage(brand, currentPage, pageSize);
        return new Result<>(true, StatusCode.OK, MessageConstants.LIST_BRAND_BY_CONDITION_AND_PAGE, pageInfo);
    }

    /*
     * 根据分类ID获取品牌
     * 包名: com.itheima.changgou.goods.controller
     * 作者: Narcissu
     * 日期：2019/12/30 17:29
     */
    @GetMapping("/category/{id}")
    public Result<List<Brand>> listBrandByCategoryID(@PathVariable("id") Integer categoryId) {
        List<Brand> brands = brandService.listBrandByCategoryID(categoryId);
        return new Result<>(true, StatusCode.OK, MessageConstants.LIST_BRAND_BY_CATEGORY_ID_SUCCESS, brands);
    }
}
