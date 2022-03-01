package java.com.example.youngmall.product.service.impl;

import io.renren.common.utils.Query;
import org.springframework.stereotype.Service;

import java.com.example.youngmall.product.dao.ProductAttrValueDao;
import java.com.example.youngmall.product.entity.ProductAttrValueEntity;
import java.com.example.youngmall.product.service.ProductAttrValueService;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;



@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

}