package java.com.example.youngmall.product.service.impl;

import io.renren.common.utils.Query;
import org.springframework.stereotype.Service;

import java.com.example.youngmall.product.dao.SpuCommentDao;
import java.com.example.youngmall.product.entity.SpuCommentEntity;
import java.com.example.youngmall.product.service.SpuCommentService;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;



@Service("spuCommentService")
public class SpuCommentServiceImpl extends ServiceImpl<SpuCommentDao, SpuCommentEntity> implements SpuCommentService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuCommentEntity> page = this.page(
                new Query<SpuCommentEntity>().getPage(params),
                new QueryWrapper<SpuCommentEntity>()
        );

        return new PageUtils(page);
    }

}