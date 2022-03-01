package java.com.example.youngmall.product.service.impl;

import io.renren.common.utils.Query;
import org.springframework.stereotype.Service;

import java.com.example.youngmall.product.dao.CategoryDao;
import java.com.example.youngmall.product.entity.CategoryEntity;
import java.com.example.youngmall.product.service.CategoryService;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

}